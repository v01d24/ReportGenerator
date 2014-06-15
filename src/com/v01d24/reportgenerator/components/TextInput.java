package com.v01d24.reportgenerator.components;

import javax.swing.JPanel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;

import com.v01d24.reportgenerator.constants.Style;

public class TextInput extends JPanel implements IVariableWidget {
	
	private JTextArea textArea;
	
	public TextInput(String label) {
		System.out.println("TextInput constructor");
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setPreferredSize(Style.MULTI_LINE_WIDGET_SIZE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblLabel = new JLabel(label);
		GridBagConstraints gbc_lblLabel = new GridBagConstraints();
		gbc_lblLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblLabel.gridx = 0;
		gbc_lblLabel.gridy = 0;
		add(lblLabel, gbc_lblLabel);
		
		textArea = new JTextArea();
		textArea.setRows(5);
		textArea.setLineWrap(true);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		add(textArea, gbc_textArea);
	}

	public String getValue() {
		return textArea.getText();
	}
}
