package com.v01d24.reportgenerator.operation;

import java.util.regex.Pattern;
import org.jdom.Element;
import com.v01d24.reportgenerator.DocumentProcessor;

public class VariablesScanner extends AbstractDocumentOperation {

	public VariablesScanner(DocumentProcessor documentProcessor) {
		super(documentProcessor);
	}

	@Override
	public void openTag(Element element) {
		String elementName = element.getName();
		if (elementName == "h" || elementName == "p") {
			documentProcessor.addVariables(element);
		}
		
	}

}
