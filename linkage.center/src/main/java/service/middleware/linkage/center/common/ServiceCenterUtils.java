//package service.middleware.linkage.center.common;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringBufferInputStream;
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import service.middleware.linkage.framework.access.domain.ServiceRegisterEntry;
//
//public final class ServiceCenterUtils {
//
//	/**
//	 * ��ServiceInformationʵ�����л�Ϊ�ַ���
//	 *
//	 * @param objServiceInformation
//	 * @return
//	 */
//	public static String serializeServiceInformation(
//			ServiceRegisterEntry objServiceInformation) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<serviceInformation>");
//		sb.append("<address>");
//		sb.append(objServiceInformation.getAddress());
//		sb.append("</address>");
//		sb.append("<port>");
//		sb.append(objServiceInformation.getPort());
//		sb.append("</port>");
//		sb.append("<serviceName>");
//		sb.append(objServiceInformation.getServiceName());
//		sb.append("</serviceName>");
//		sb.append("<serviceMethod>");
//		sb.append(objServiceInformation.getServiceMethod());
//		sb.append("</serviceMethod>");
//		sb.append("<serviceVersion>");
//		sb.append(objServiceInformation.getServiceVersion());
//		sb.append("</serviceVersion>");
//		sb.append("</serviceInformation>");
//		return sb.toString();
//	}
//
//	/**
//	 * ��requestʵ�����л�Ϊ�ַ���
//	 *
//	 * @param request
//	 * @return
//	 */
//	public static ServiceRegisterEntry deserializeServiceInformation(
//			String receiveData) {
//		try {
//			InputStream is = new StringBufferInputStream(receiveData);
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			Document document = db.parse(is);
//			NodeList childs = document.getChildNodes().item(0).getChildNodes();
//			String address = "";
//			String port = "";
//			String serviceName = "";
//			String serviceMethod = "";
//			String serviceVersion = "";
//			for (int i = 0; i < childs.getLength(); i++) {
//				Node node = childs.item(i);
//				if (node.getNodeName().equals("address")) {
//					address = node.getTextContent();
//				} else if (node.getNodeName().equals("port")) {
//					port = node.getTextContent();
//				} else if (node.getNodeName().equals("serviceName")) {
//					serviceName = node.getTextContent();
//				} else if (node.getNodeName().equals("serviceMethod")) {
//					serviceMethod = node.getTextContent();
//				} else if (node.getNodeName().equals("serviceVersion")) {
//					serviceVersion = node.getTextContent();
//				}
//			}
//			ServiceRegisterEntry objServiceInformation = new ServiceRegisterEntry(
//					address, Integer.parseInt(port), serviceName,
//					serviceMethod, serviceVersion);
//			return objServiceInformation;
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * ��requestʵ�����л�Ϊ�ַ���
//	 *
//	 * @param request
//	 * @return
//	 */
//	public static List<ServiceRegisterEntry> deserializeServiceInformationList(
//			String receiveData) {
//		try {
//			System.out.println("deserializeServiceInformationList :"
//					+ receiveData);
//			List<ServiceRegisterEntry> list = new LinkedList<ServiceRegisterEntry>();
//			InputStream is = new StringBufferInputStream(receiveData);
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			Document document = db.parse(is);
//			NodeList childs = document.getChildNodes().item(0).getChildNodes();
//			String address = "";
//			String port = "";
//			String serviceName = "";
//			String serviceMethod = "";
//			String serviceVersion = "";
//			for (int i = 0; i < childs.getLength(); i++) {
//				Node node = childs.item(i);
//				if (node.getNodeName().equals("serviceInformation")) {
//					NodeList childs1 = node.getChildNodes();
//					for (int j = 0; j < childs1.getLength(); j++) {
//						Node node1 = childs1.item(j);
//						if (node1.getNodeName().equals("address")) {
//							address = node1.getTextContent();
//						} else if (node1.getNodeName().equals("port")) {
//							port = node1.getTextContent();
//						} else if (node1.getNodeName().equals("serviceName")) {
//							serviceName = node1.getTextContent();
//						} else if (node1.getNodeName().equals("serviceMethod")) {
//							serviceMethod = node1.getTextContent();
//						} else if (node1.getNodeName().equals("serviceVersion")) {
//							serviceVersion = node1.getTextContent();
//						}
//					}
//					ServiceRegisterEntry objServiceInformation = new ServiceRegisterEntry(
//							address, Integer.parseInt(port), serviceName,
//							serviceMethod, serviceVersion);
//					list.add(objServiceInformation);
//				}
//			}
//			return list;
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/**
//	 * ��requestʵ�����л�Ϊ�ַ���
//	 *
//	 * @param request
//	 * @return
//	 */
//	public static String serializeServiceInformationList(
//			List<ServiceRegisterEntry> SERVICE_REGISTER_ENTRY_LIST) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<SERVICE_REGISTER_ENTRY_LIST>");
//		for (ServiceRegisterEntry objServiceInformation : SERVICE_REGISTER_ENTRY_LIST) {
//			sb.append("<serviceInformation>");
//			sb.append("<address>");
//			sb.append(objServiceInformation.getAddress());
//			sb.append("</address>");
//			sb.append("<port>");
//			sb.append(objServiceInformation.getPort());
//			sb.append("</port>");
//			sb.append("<serviceName>");
//			sb.append(objServiceInformation.getServiceName());
//			sb.append("</serviceName>");
//			sb.append("<serviceMethod>");
//			sb.append(objServiceInformation.getServiceMethod());
//			sb.append("</serviceMethod>");
//			sb.append("<serviceVersion>");
//			sb.append(objServiceInformation.getServiceVersion());
//			sb.append("</serviceVersion>");
//			sb.append("</serviceInformation>");
//		}
//		sb.append("</SERVICE_REGISTER_ENTRY_LIST>");
//		return sb.toString();
//	}
//}
