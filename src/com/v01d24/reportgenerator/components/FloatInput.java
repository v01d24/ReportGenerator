package com.v01d24.reportgenerator.components;



public class FloatInput extends StringInput {

	public FloatInput(String label) {
		super(label);
	}

	public float getFloatValue() {
		return Float.valueOf(txtInput.getText());
	}
}
