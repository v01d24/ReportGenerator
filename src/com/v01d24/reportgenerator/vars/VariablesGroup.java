package com.v01d24.reportgenerator.vars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.jdom.Element;
import org.json.JSONObject;

import com.v01d24.reportgenerator.vars.Variable.TYPE;

public class VariablesGroup {

	private List<String> variablesNames;
	private Map<String, Variable> variablesMap;

	public VariablesGroup(String groupName) {
		variablesNames = new ArrayList<String>();
		variablesMap = new HashMap<String, Variable>();
	}

	public void addVariable(Variable variable) {
		variablesNames.add(variable.name);
		variablesMap.put(variable.name, variable);
	}
	
	public List<String> getNamesList() {
		return variablesNames;
	}
	
	public Variable getVariableByName(String variableName) {
		return variablesMap.get(variableName);
	}
}
