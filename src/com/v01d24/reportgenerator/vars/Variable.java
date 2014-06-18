package com.v01d24.reportgenerator.vars;


import org.jdom.Element;
import org.json.JSONArray;
import org.json.JSONObject;

import com.v01d24.reportgenerator.components.DateInput;
import com.v01d24.reportgenerator.components.FloatInput;
import com.v01d24.reportgenerator.components.IVariableWidget;
import com.v01d24.reportgenerator.components.IntegerInput;
import com.v01d24.reportgenerator.components.SelectInput;
import com.v01d24.reportgenerator.components.StringInput;
import com.v01d24.reportgenerator.components.TextInput;

public class Variable {

	enum TYPE {STRING, TEXT, INTEGER, FLOAT, MEASURED, DATE, SELECT}
	
	public final TYPE type;
	public final String name;
	public final String label;
	private String value;
	public final JSONObject jOptions;
	public final Element holder;
	private IVariableWidget widget;
	
	public Variable(String variableName, JSONObject jOptions, Element holder) {
		name = variableName;
		label = jOptions.optString("label", variableName);
		this.jOptions = jOptions;
		String typeString = jOptions.optString("type", "STRING").toUpperCase();
		type = TYPE.valueOf(typeString);
		if (type == null) {
			System.out.println("Unsupported type: " + typeString);
		}
		value = jOptions.optString("default");
		this.holder = holder;
		System.out.println(variableName + " " + type);
	}

	public String getValue() {
		return value;
	}
	
	public void refreshValue() {
		if (widget != null) {
			value = widget.getValue();
		}
	}
	
	public IVariableWidget getWidget() {
		if (widget == null) {
			switch (type) {
			case TEXT:
				System.out.println("Widget: TextInput");
				widget = new TextInput(label);
				break;
			case INTEGER:
				System.out.println("Widget: IntegerInput");
				widget = new IntegerInput(label);
				break;
			case FLOAT:
				System.out.println("Widget: FloatInput");
				widget = new FloatInput(label);
				break;
			case DATE:
				System.out.println("Widget: DateInput");
				widget = new DateInput(label);
				break;
			case SELECT:
				System.out.println("Widget: SelectInput");
				JSONArray jChoices = jOptions.getJSONArray("choices");
				int length = jChoices.length();
				String[] choices = new String[length];
				for (int i = 0; i < length; i++) {
					choices[i] = jChoices.getString(i);
				}
				widget = new SelectInput(label, choices);
				break;
			default:
				System.out.println("Widget: StringInput");
				widget = new StringInput(label);
				break;
			}
		}
		return widget;
	}
}
