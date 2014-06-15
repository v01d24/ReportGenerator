package com.v01d24.reportgenerator.components;

import javax.swing.JTextField;

import com.v01d24.reportgenerator.constants.Style;


public class StringInput extends AbstractSingleLineInput {

	protected JTextField txtInput;

	public StringInput(String label) {
		super(label);
		txtInput = new JTextField();
		txtInput.setPreferredSize(Style.SINGLE_LINE_INPUT_SIZE);
		add(txtInput);
	}

	@Override
	public String getValue() {
		return txtInput.getText();
	}
	
	@Override
	public void setValue(String value) {
		txtInput.setText(value);
	}
}
