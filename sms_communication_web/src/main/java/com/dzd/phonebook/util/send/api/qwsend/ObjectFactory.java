package com.dzd.phonebook.util.send.api.qwsend;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.ca.ws.impl package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _SendResponse_QNAME = new QName("http://impl.ws.zyz.com/", "sendResponse");
	private final static QName _Send_QNAME = new QName("http://impl.ws.zyz.com/", "send");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.ca.ws.impl
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Send }
	 * 
	 */
	public Send createSend() {
		return new Send();
	}

	/**
	 * Create an instance of {@link SendResponse }
	 * 
	 */
	public SendResponse createSendResponse() {
		return new SendResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SendResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://impl.ws.zyz.com/", name = "sendResponse")
	public JAXBElement<SendResponse> createSendResponse(SendResponse value) {
		return new JAXBElement<SendResponse>(_SendResponse_QNAME,
				SendResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Send }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://impl.ws.zyz.com/", name = "send")
	public JAXBElement<Send> createSend(Send value) {
		return new JAXBElement<Send>(_Send_QNAME, Send.class, null, value);
	}

}
