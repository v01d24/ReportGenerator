package com.v01d24.reportgenerator.operation;

import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

import com.v01d24.reportgenerator.DocumentProcessor;

public class DocumentStructure extends AbstractDocumentOperation {

	public DocumentStructure(DocumentProcessor documentProcessor) {
		super(documentProcessor);
	}

	private StringBuffer sBuilder = null;

	@Override
	public void init() {
		sBuilder = new StringBuffer();
	}
	
	@Override
	public void openTag(Element element) {
		sBuilder.append("Open name: ");
		sBuilder.append(element.getName());
		sBuilder.append("\nAttributes:");
		List<Attribute> attributes = element.getAttributes();
		for (Attribute attribute: attributes) {
			sBuilder.append("{");
			sBuilder.append(attribute.getName());
			sBuilder.append(":");
			sBuilder.append(attribute.getValue());
			sBuilder.append("} ");
		}
		sBuilder.append("\n");
		sBuilder.append(element.getValue());
		sBuilder.append("\n\n");
	}

	@Override
	public void closeTag(Element element) {
		sBuilder.append("Close name: ");
		sBuilder.append(element.getName());
		sBuilder.append("\n\n");
	}

	public String getString() {
		return sBuilder.toString();
	}

}
