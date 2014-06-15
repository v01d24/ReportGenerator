package com.v01d24.reportgenerator.components;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import com.v01d24.reportgenerator.constants.Style;

import java.awt.Color;

public abstract class AbstractSingleLineInput extends JPanel implements IVariableWidget {

	/**
	 * Create the panel without input.
	 */
	public AbstractSingleLineInput(String label) {
		setBorder(new LineBorder(Style.BORDER_COLOR));
		setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		setPreferredSize(Style.SINGLE_LINE_WIDGET_SIZE);
		JLabel lblLabel = new JLabel(label);
		lblLabel.setPreferredSize(Style.LABEL_SIZE);
		add(lblLabel);
	}

	abstract public String getValue();
	abstract public void setValue(String value);
}
