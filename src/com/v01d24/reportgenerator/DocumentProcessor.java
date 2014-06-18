package com.v01d24.reportgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jopendocument.dom.ODSingleXMLDocument;
import org.json.JSONObject;

import com.v01d24.reportgenerator.constants.Strings;
import com.v01d24.reportgenerator.operation.DocumentToHtml;
import com.v01d24.reportgenerator.operation.AbstractDocumentOperation;
import com.v01d24.reportgenerator.operation.VariablesScanner;
import com.v01d24.reportgenerator.vars.Variable;
import com.v01d24.reportgenerator.vars.VariablesGroup;

public class DocumentProcessor {

	private File file;
	private ODSingleXMLDocument workingDocument;
	private Map<String, Variable> variablesMap;
	private Map<String, VariablesGroup> groupsMap;
	
	private final static Pattern VARIABLE_PATTERN;
	static {
		VARIABLE_PATTERN = Pattern.compile("\\{%([_A-Za-z][_0-9A-Za-z]*)=(\\{.+?\\})%\\}");
	}

	public DocumentProcessor() {
		variablesMap = new HashMap<String, Variable>();
		groupsMap = new HashMap<String, VariablesGroup>();
	}
	
	//File operations
	public void resetFile() throws JDOMException, IOException {
		workingDocument = ODSingleXMLDocument.createFromPackage(file);
		findVariables();

	}

	public void setFile(File file) throws JDOMException, IOException {
		this.file = file;
		resetFile();
	}

	public void saveToFile(File file) throws IOException {
		ODSingleXMLDocument copy = workingDocument.clone();
		workingDocument.saveAs(file);
	}

	public String translate(String s) {
		Matcher m = VARIABLE_PATTERN.matcher(s);
		StringBuffer sb = new StringBuffer(s.length());
		while (m.find()) {
			String variableName = m.group(1);
			Variable variable = variablesMap.get(variableName);
			if (variable != null) {
				String value = variable.getValue();
				if (value == null || value.isEmpty()) {
					value = "<font color=red>#" + variable.label + "#</font>";
				}
				else {
					value = "<font color=blue>" + value + "</font>";
				}
				m.appendReplacement(sb, Matcher.quoteReplacement(value));
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private void performOperation(Element element, AbstractDocumentOperation documentOperation) {
		List<Element> list = (List<Element>)element.getChildren();
		for (Element childElement: list) {
			documentOperation.openTag(childElement);
			performOperation(childElement, documentOperation);
			documentOperation.closeTag(childElement);
		}
	}

	private void startOperation(AbstractDocumentOperation documentOperation) {
		documentOperation.init();
		performOperation(workingDocument.getBody(), documentOperation);
	}

	public String getHtml() {
		DocumentToHtml docToHtml = new DocumentToHtml(this);
		startOperation(docToHtml);
		return docToHtml.getHtml();
	}

/*
	public void addVariable(String groupName, String variableName, JSONObject jOptions, Element holder) {
		VariablesGroup variablesGroup = (VariablesGroup) groupsMap.get(groupName);
		if (variablesGroup == null) {
			variablesGroup = new VariablesGroup(groupName);
			groupsMap.put(groupName, variablesGroup);
		}
		Variable variable = (Variable) variablesMap.get(variableName);
		if (variable == null) {
			variable = new Variable(variableName, jOptions, holder);
			variablesMap.put(variableName, variable);
		}
	}
*/
	public void addVariables(Element element) {
		Matcher m = VARIABLE_PATTERN.matcher(element.getValue());
		while (m.find()) {
			String variableName = m.group(1);
			Variable variable = variablesMap.get(variableName);
			if (variable == null) {
				String jOptionsString = m.group(2);
				JSONObject jOptions = new JSONObject(jOptionsString);
				String groupName = jOptions.optString("group", Strings.DEFAULT_GROUP);
				VariablesGroup variablesGroup = (VariablesGroup) groupsMap.get(groupName);
				if (variablesGroup == null) {
					variablesGroup = new VariablesGroup(groupName);
					groupsMap.put(groupName, variablesGroup);
				}
				variable = new Variable(variableName, jOptions, element);
				variablesGroup.addVariable(variable);	
				variablesMap.put(variableName, variable);
			}
		}
	}
	
	//Find all variables in opened document
	private void findVariables() {
		VariablesScanner variablesScanner = new VariablesScanner(this);
		startOperation(variablesScanner);
	}

	//Find all variables in opened document
	private void setVariables() {
		Collection<Variable> variables = variablesMap.values();
		for (Variable variable: variables) {
			String tagText = variable.holder.getText();
			translate(tagText);
		}
	}
	
	//Get variables info
	public Set<String> getGroupsNames() {
		return groupsMap.keySet();
	}
		
	public VariablesGroup getGroupByName(String groupName) {
		return groupsMap.get(groupName);
	}

	
	public void refreshValues() {
		for (String variableName: variablesMap.keySet()) {
			Variable variable = variablesMap.get(variableName);
			variable.refreshValue();
		}
	}

}
