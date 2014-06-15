package com.v01d24.reportgenerator.components;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import com.v01d24.reportgenerator.constants.Style;


public class FormattedInput extends AbstractSingleLineInput {
	
	protected JFormattedTextField txtInput;

	public FormattedInput(String label, MaskFormatter formatter) {
		super(label);
		txtInput = new JFormattedTextField(formatter);
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
