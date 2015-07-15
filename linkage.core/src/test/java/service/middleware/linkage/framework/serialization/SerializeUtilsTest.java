//package service.middleware.linkage.framework.serialization;
//
//import static org.junit.Assert.assertTrue;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import org.junit.Test;
//
//import service.middleware.linkage.framework.access.domain.ServiceRequest;
//import service.middleware.linkage.framework.access.domain.ServiceResponse;
//
//public class SerializeUtilsTest {
//
//	/**
//	 * ��requestʵ�����л�Ϊ�ַ���
//	 * @return
//	 */
//	@Test
//	public void testSerializeRequest() {
//		ServiceRequest request = new ServiceRequest();
//		request.setMethodName("test23%^Method&");
//		request.setServiceName("<testServiceNmae>");
//		request.setGroup("testGroup");
//		request.setRequestID("10000");
//		request.setVersion("@@!#$test.1.0");
//		List<Object> args = new LinkedList<Object>();
//		args.add("arg1");
//		args.add("arg2&*^%");
//		args.add("arg3");
//		request.setArgs(args);
//		String serializeStr = SerializationUtils.serializeRequest(request);
//		System.out.println("serializeStr : " + serializeStr);
//		ServiceRequest result = SerializationUtils.deserializeRequest(serializeStr);
//		assertTrue("result.getServiceName() not equals to <testServiceNmae> realvalue is " + result.getServiceName(),  result.getServiceName().equals("<testServiceNmae>"));
//		assertTrue("result.getMethodName()  not equals to test23%^Method& realvalue is " + result.getMethodName(),  result.getMethodName().equals("test23%^Method&"));
//		assertTrue("result.getGroup()       not equals to testGroup realvalue is " + result.getGroup(),  result.getGroup().equals("testGroup"));
//		assertTrue("result.getVersion()     not equals to @@!#$test.1.0 realvalue is " + result.getVersion(),  result.getVersion().equals("@@!#$test.1.0"));
//		assertTrue("result.setRequestID()   not equals to 10000 realvalue is " + result.getRequestID(),  result.getRequestID().equals("10000"));
//		assertTrue("result.getArgs().get(1) not equals to arg2&*^% realvalue is " + result.getArgs().get(1),  result.getArgs().get(1).equals("arg2&*^%"));
//	}
//
//	/**
//	 * ��ResponseEntityʵ�����л�Ϊ�ַ���
//	 * @return
//	 */
//	@Test
//	public void testSerializeResponse() {
//		ServiceResponse response = new ServiceResponse();
//		response.setRequestID("100001212");
//		response.setJsonResult("sdsjdlfkj$@^!*#!4457@$$");
//		String serializeStr = SerializationUtils.serializeResponse(response);
//		System.out.println("serializeStr : " + serializeStr);
//		ServiceResponse result = SerializationUtils.deserializeResponse(serializeStr);
//		assertTrue("result.getJsonResult() not equals to sdsjdlfkj$@^!*#!4457@$$ realvalue is " + result.getJsonResult(),  result.getJsonResult().equals("sdsjdlfkj$@^!*#!4457@$$"));
//		assertTrue("result.setRequestID()   not equals to 100001212 realvalue is " + result.getRequestID(),  result.getRequestID().equals("100001212"));
//	}
//}
