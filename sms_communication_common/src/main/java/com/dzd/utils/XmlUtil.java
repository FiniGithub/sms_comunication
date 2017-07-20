package com.dzd.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XML常用类的整理，包括几大常用功能:<br>
 * <ol>
 * <li>规格化XML（HTML转换为XML）</li>
 * <li>其他</li>
 * </ol>
 *
 * @date 2013-4-30
 * @author MipatchTeam#chenc
 *
 */
public class XmlUtil {

	/**
	 * 将HTML或者其他文字转换为标准XML document
	 *
	 * @param string
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public static Document formatToDoc(String string) throws IOException, SAXException {
		DOMParser parser = new DOMParser();
		// 设置网页的默认编码
		parser.setProperty( "http://cyberneko.org/html/properties/default-encoding", "gb2312");
		/*
		 * The Xerces HTML DOM implementation does not support namespaces
		 * and cannot represent XHTML documents with namespace information.
		 * Therefore, in order to use the default HTML DOM implementation
		 * with NekoHTML's DOMParser to parse XHTML documents, you must turn
		 * off namespace processing.
		 */
		parser.setFeature("http://xml.org/sax/features/namespaces", false);

		BufferedReader in = new BufferedReader(new InputStreamReader( new ByteArrayInputStream(string.getBytes())));
		parser.parse(new InputSource(in));
		in.close();
		return parser.getDocument();
	}

	public static Document xmlToDoc(String string) throws IOException, SAXException, ParserConfigurationException  {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		BufferedReader in = new BufferedReader(new InputStreamReader( new ByteArrayInputStream(string.getBytes())));
		Document document = db.parse(new InputSource(in));
		in.close();

		return document;
	}
	/**
	 * 打印出节点的Xml
	 *
	 * @param node
	 * @return
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	public static String printNodeXml(Node node) {
		try {
			StringWriter writer = new StringWriter();
			TransformerFactory.newInstance().newTransformer().transform(new DOMSource(node), new StreamResult(writer));
			return writer.toString();
		} catch (Exception e) {
			return "";
		}
	}

}
