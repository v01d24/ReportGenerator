package com.v01d24.reportgenerator.components;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;

import com.v01d24.reportgenerator.constants.Style;


public class SelectInput extends AbstractSingleLineInput {

	protected JSpinner select;

	public SelectInput(String label, String[] choices) {
		super(label);
		SpinnerListModel listModel = new SpinnerListModel(choices);
		select = new JSpinner(listModel);
		select.setPreferredSize(Style.SINGLE_LINE_INPUT_SIZE);
		add(select);
	}

	@Override
	public String getValue() {
		return (String) select.getValue();
	}
	
	@Override
	public void setValue(String value) {
		select.setValue(value);
	}
}
