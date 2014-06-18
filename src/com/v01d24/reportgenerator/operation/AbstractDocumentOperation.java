package com.v01d24.reportgenerator.operation;

import org.jdom.Element;

import com.v01d24.reportgenerator.DocumentProcessor;

public abstract class AbstractDocumentOperation {

	protected DocumentProcessor documentProcessor = null;
	
	public AbstractDocumentOperation(DocumentProcessor documentProcessor) {
		this.documentProcessor = documentProcessor;
	}

	public void init() {}

	public void openTag(Element element) {}

	public void closeTag(Element element) {}

}
