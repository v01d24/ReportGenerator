package com.v01d24.reportgenerator.components;



public class IntegerInput extends StringInput {

	public IntegerInput(String label) {
		super(label);
	}

	public int getIntegerValue() {
		return Integer.valueOf(txtInput.getText());
	}
}
