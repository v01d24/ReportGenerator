package com.v01d24.reportgenerator.operation;

import org.jdom.Element;
import org.jopendocument.dom.ODSingleXMLDocument;

import com.v01d24.reportgenerator.DocumentProcessor;

public class DocumentToHtml extends AbstractDocumentOperation {

	public DocumentToHtml(DocumentProcessor documentProcessor) {
		super(documentProcessor);
	}

	private StringBuffer sBuilder = null;

	@Override
	public void init() {
		sBuilder = new StringBuffer();
	}

	@Override
	public void openTag(Element element) {
		String elementName = element.getName();
		if (elementName == "h") {
			String tag = null;
			String outlineLevel = element.getAttributeValue("outline-level");
			if (outlineLevel != null) {
				tag = "h" + outlineLevel;
			}
			else {
				tag = "h4";
			}
			sBuilder.append("<" + tag + ">");
			sBuilder.append(documentProcessor.translate(element.getValue()));
			sBuilder.append("</" + tag + ">");
		}
		else if (elementName == "p") {
			sBuilder.append("<p>");
			sBuilder.append(documentProcessor.translate(element.getValue()));
			sBuilder.append("</p>");
		}
		else if (elementName == "tab") {
			sBuilder.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		}
		else if (elementName == "list") {
			sBuilder.append("<ol>");
		}
		else if (elementName == "list-item") {
			sBuilder.append("<li>");
		}
		else if (elementName == "table") {
			sBuilder.append("<table border=1 cellspacing=0>");
		}
		else if (elementName == "table-row") {
			sBuilder.append("<tr>");
		}
		else if (elementName == "table-cell") {
			sBuilder.append("<td");
			String colspan = element.getAttributeValue("number-columns-spanned");
			if (colspan != null) {
				sBuilder.append(" colspan=" + colspan);
			}
			String rowspan = element.getAttributeValue("number-rows-spanned");
			if (rowspan != null) {
				sBuilder.append(" rowspan=" + rowspan);
			}
			sBuilder.append(">");
		}
	}

	@Override
	public void closeTag(Element element) {
		String elementName = element.getName();
		if (elementName == "list") {
			sBuilder.append("</ol>");
		}
		else if (elementName == "list-item") {
			sBuilder.append("</li>");
		}
		else if (elementName == "table") {
			sBuilder.append("</table>");
		}
		else if (elementName == "table-row") {
			sBuilder.append("</tr>");
		}
		else if (elementName == "table-cell") {
			sBuilder.append("</td>");
		}
	}

	public String getHtml() {
		return sBuilder.toString();
	}
}
