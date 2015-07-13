package service.middleware.linkage.framework.serialization;

import linkage.common.JsonUtils;
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
		return JsonUtils.toJson(request);
	}
	
	/**
	 * deserialize request domain
	 * @return
	 */
	public static ServiceRequest deserializeRequest(String receiveData) {
		return JsonUtils.fromJson(receiveData, ServiceRequest.class);
	}
	
	/**
	 * serialize Response domain
	 * @return
	 */
	public static String serializeResponse(ServiceResponse response) {
		return JsonUtils.toJson(response);
	}
	
	/**
	 * deserialize Response domain
	 * @return
	 */
	public static ServiceResponse deserializeResponse(String receiveData) {
		return JsonUtils.fromJson(receiveData, ServiceResponse.class);
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
