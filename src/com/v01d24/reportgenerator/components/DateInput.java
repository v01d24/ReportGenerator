package com.v01d24.reportgenerator.components;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.MaskFormatter;



public class DateInput extends FormattedInput {

	private static MaskFormatter formatter;
	static {
		try {
			formatter = new MaskFormatter("##.##.####");
		} catch (ParseException e) {
			System.out.println(e);
			formatter = new MaskFormatter();
		}
		formatter.setPlaceholderCharacter('_');
	}

	public DateInput(String label) {
		super(label, formatter);
	}
	
	public Date getDateValue() {
		SimpleDateFormat sdf = new SimpleDateFormat("%d.%m.%Y");
		try {
			return sdf.parse(txtInput.getText());
		} catch (ParseException e) {
			return null;
		}
	}
}
