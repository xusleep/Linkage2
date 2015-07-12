package service.middleware.linkage.framework.serialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import service.middleware.linkage.framework.access.domain.ServiceRequest;
import service.middleware.linkage.framework.access.domain.ServiceResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

public class SerializationUtils {
	private static Logger logger = LoggerFactory.getLogger(SerializationUtils.class);
	/**
	 * serialize request domain
	 * @param request
	 * @return
	 */
	public static String serializeRequest(ServiceRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("<request>");
		sb.append("<requestid>");
		sb.append(request.getRequestID());
		sb.append("</requestid>");
		sb.append("<serviceName>");
		sb.append(escapeForXML(request.getServiceName()));
		sb.append("</serviceName>");
		sb.append("<methodName>");
		sb.append(escapeForXML(request.getMethodName()));
		sb.append("</methodName>");
		sb.append("<version>");
		sb.append(escapeForXML(request.getVersion()));
		sb.append("</version>");
		sb.append("<group>");
		sb.append(escapeForXML(request.getGroup()));
		sb.append("</group>");
		sb.append("<list>");
		if(request.getArgs() != null){
			for (int i = 0; i < request.getArgs().size(); i++) {
				sb.append("<arg>");
				sb.append(escapeForXML(request.getArgs().get(i)));
				sb.append("</arg>");
			}
		}
		sb.append("</list>");
		sb.append("</request>");
		return sb.toString();
	}
	
	/**
	 * deserialize request domain
	 * @param request
	 * @return
	 */
	public static ServiceRequest deserializeRequest(String receiveData) {
		try {
			InputStream is = new StringBufferInputStream(receiveData);
			ServiceRequest request = new ServiceRequest();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			NodeList childs = document.getChildNodes().item(0).getChildNodes(); 
			for(int i = 0; i < childs.getLength(); i++){
				Node node = childs.item(i);
				if(node.getNodeName().equals("requestid")){
					request.setRequestID(node.getTextContent());
				}
				else if(node.getNodeName().equals("serviceName")){
					request.setServiceName(node.getTextContent());
				}
				else if(node.getNodeName().equals("methodName")){
					request.setMethodName(node.getTextContent());
				}
				else  if(node.getNodeName().equals("version")){
					request.setVersion(node.getTextContent());
				}
				else if(node.getNodeName().equals("group")){
					request.setGroup(node.getTextContent());
				}
				else if(node.getNodeName().equals("list")){
					NodeList childs1 = node.getChildNodes();
					for(int j = 0; j < childs1.getLength(); j++){
						Node listNode = childs1.item(j);
						if(listNode.getNodeName().equals("arg")){
							request.getArgs().add(listNode.getTextContent());
						}
					}
				}
			}
			return request;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * serialize Response domain
	 * @param request
	 * @return
	 */
	public static String serializeResponse(ServiceResponse response) {
		StringBuilder sb = new StringBuilder();
		sb.append("<response>");
		sb.append("<requestid>");
		sb.append(response.getRequestID());
		sb.append("</requestid>");
		sb.append("<result>");
		sb.append(escapeForXML(response.getResult()));
		sb.append("</result>");
		sb.append("</response>");
		return sb.toString();
	}
	
	/**
	 * deserialize Response domain
	 * @return
	 */
	public static ServiceResponse deserializeResponse(String receiveData) {
		try {
			logger.debug("received data : " + receiveData);
			InputStream is = new StringBufferInputStream(receiveData);
			ServiceResponse response = new ServiceResponse();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			NodeList childs = document.getChildNodes().item(0).getChildNodes(); 
			for(int i = 0; i < childs.getLength(); i++){
				Node node = childs.item(i);
				if(node.getNodeName().equals("result")){
					response.setResult(node.getTextContent());
				}
				else if(node.getNodeName().equals("requestid")){
					response.setRequestID(node.getTextContent());
				}
			}
			return response;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	

    /**
     * Escapes all necessary characters in the String so that it can be used
     * in an XML doc.
     *
     * @param string the string to escape.
     * @return the string with appropriate characters escaped.
     */
    public static final String escapeForXML ( String string )
    {

        // Check if the string is null or zero length -- if so, return
        // what was sent in.
        if ( ( string == null ) || ( string.length() == 0 ) )
        {
            return string;
        }

        char[]       sArray = string.toCharArray();
        StringBuffer buf    = new StringBuffer( sArray.length );
        char         ch;

        // according to http://www.w3.org/TR/REC-xml/#charsets
        // a valid xml character is defined as:
        // #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
        for ( int i = 0; i < sArray.length; i++ )
        {
            ch = sArray [ i ];

            if ( ch == '<' )
            {
                buf.append( "&lt;" );
            }
            else if ( ch == '&' )
            {
                buf.append( "&amp;" );
            }
            else if ( ch == '>' )
            {
                buf.append( "&gt;" );
            }
            else if ( ch == '"' )
            {
                buf.append( "&quot;" );
            }
            else if ( ch == '\'' )
            {
                buf.append( "&apos;" );
            }
            else if ( ch == '\n' )
            {
                buf.append( ch );
            }
            else if ( ch == '\r' )
            {
                buf.append( ch );
            }
            else if ( ch == '\t' )
            {
                buf.append( ch );
            }
            else if ( (int)ch < 0x20 )
            {
                buf.append( " " );
            }
            else
            {
                buf.append( ch );
            }
        }

        return buf.toString();
    }
}
