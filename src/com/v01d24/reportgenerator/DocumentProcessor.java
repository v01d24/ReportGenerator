package com.v01d24.reportgenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
		findVariables(workingDocument.getBody());

	}

	public void setFile(File file) throws JDOMException, IOException {
		this.file = file;
		resetFile();
	}

	public void saveToFile(File file) throws IOException {
		workingDocument.saveAs(file);
	}
	
	//Dictionary operations
	/*public void updateDictionary(String key, String Value) {
		variablesMap.put(key, Value);
	}

	public void removeDictionaryKey(String key) {
		variablesMap.remove(key);
	}

	public void resetDictionary() {
		variablesMap.clear();
	}*/

	public String translate(String s) {
		Matcher m = VARIABLE_PATTERN.matcher(s);
		StringBuffer sb = new StringBuffer(s.length());
		while (m.find()) {
			String variableName = m.group(1);
			Variable variable = variablesMap.get(variableName);
			if (variable != null) {
				String value = variable.getValue();
				if (value == null || value.isEmpty()) {
					value = "<font color=red>#" + variable.getLabel() + "#</font>";
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
	
	//Simple conversion to HTML
	private void elementToHtml(Element element, StringBuilder sBuilder) {
		String elementName = element.getName();
		if (elementName == "h") {
			String tag = null;
			String outlineLevel = element.getAttributeValue("outline-level");
			if (outlineLevel != null) {
				tag = "h" + outlineLevel;
			}
			else {
				tag = "h4";
			}
			sBuilder.append("<" + tag + ">");
			sBuilder.append(translate(element.getValue()));
			sBuilder.append("</" + tag + ">");
		}
		else if (elementName == "p") {
			sBuilder.append("<p>");
			sBuilder.append(translate(element.getValue()));
			sBuilder.append("</p>");
		}
		else if (elementName == "tab") {
			sBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		else if (elementName == "list") {
			sBuilder.append("<ol>");
			iterateChildsToHtml(element, sBuilder);
			sBuilder.append("</ol>");
		}
		else if (elementName == "list-item") {
			sBuilder.append("<li>");
			iterateChildsToHtml(element, sBuilder);
			sBuilder.append("</li>");
		}
		else if (elementName == "table") {
			sBuilder.append("<table border=1 cellspacing=0>");
			iterateChildsToHtml(element, sBuilder);
			sBuilder.append("</table>");
		}
		else if (elementName == "table-row") {
			sBuilder.append("<tr>");
			iterateChildsToHtml(element, sBuilder);
			sBuilder.append("</tr>");
		}
		else if (elementName == "table-cell") {
			sBuilder.append("<td");
			String colspan = element.getAttributeValue("number-columns-spanned");
			//System.out.println("colspan " + colspan);
			if (colspan != null) {
				sBuilder.append(" colspan=" + colspan);
			}
			String rowspan = element.getAttributeValue("number-rows-spanned");
			//System.out.println("rowspan " + rowspan);
			if (rowspan != null) {
				sBuilder.append(" rowspan=" + rowspan);
			}
			sBuilder.append(">");
			iterateChildsToHtml(element, sBuilder);
			sBuilder.append("</td>");
			return;
		}
		else {
			iterateChildsToHtml(element, sBuilder);
		}
	}

	private void iterateChildsToHtml(Element element, StringBuilder sBuilder) {
		List<Element> list = (List<Element>)element.getChildren();
		for (Element childElement: list) {
			elementToHtml(childElement, sBuilder);
		}
	}

	public String getHtml() {
		StringBuilder sBuilder = new StringBuilder();
		elementToHtml(workingDocument.getBody(), sBuilder);
		return sBuilder.toString();
	}

	//Find all variables in opened document
	private void findVariables(Element element) {
		String elementName = element.getName();
		if (elementName == "h") {
			addVariables(element);
		}
		else if (elementName == "p") {
			addVariables(element);
		}
		else {
			List<Element> list = (List<Element>)element.getChildren();
			for (Element childElement: list) {
				findVariables(childElement);
			}
		}
	}
	
	//Get variables info
	public Set<String> getGroupsNames() {
		return groupsMap.keySet();
	}

	public void addVariables(Element element) {
		Matcher m = VARIABLE_PATTERN.matcher(element.getValue());
		while (m.find()) {
			String variableName = m.group(1);
			Variable variable = variablesMap.get(variableName);
			if (variable == null) {
				String jOptionsString = m.group(2);
				JSONObject jOptions = new JSONObject(jOptionsString);
				String groupName = jOptions.optString("group", "Без группы");
				VariablesGroup variablesGroup = (VariablesGroup) groupsMap.get(groupName);
				if (variablesGroup == null) {
					variablesGroup = new VariablesGroup(groupName);
					groupsMap.put(groupName, variablesGroup);
				}
				variable = variablesGroup.addVariable(variableName, jOptions,  element);	
				variablesMap.put(variableName, variable);
			}
		}
	}
		
	public VariablesGroup getGroupByName(String groupName) {
		return groupsMap.get(groupName);
	}
	//Simple structure string
	/*
	private void readElement(Element element, StringBuilder sBuilder, int index) {
		sBuilder.append(index);
		sBuilder.append(". Name: ");
		sBuilder.append(element.getName());
		sBuilder.append("\nAttributes:");
		List<Attribute> attributes = element.getAttributes();
		for (Attribute attribute: attributes) {
			sBuilder.append("{");
			sBuilder.append(attribute.getName());
			sBuilder.append(":");
			sBuilder.append(attribute.getValue());
			sBuilder.append("} ");
		}
		sBuilder.append("\n");
		sBuilder.append(element.getValue());
		sBuilder.append("\n\n");
		List<Element> list = (List<Element>)element.getChildren();
		for (Element childElement: list) {
			readElement(childElement, sBuilder, index + 1);
		}
	}

	public String getText() {
		StringBuilder sBuilder = new StringBuilder();
		readElement(workingDocument.getBody(), sBuilder, 1);
		return sBuilder.toString();
	}
	*/
	
	public void refreshValues() {
		for (String variableName: variablesMap.keySet()) {
			Variable variable = variablesMap.get(variableName);
			variable.refreshValue();
		}
		
	}
}
