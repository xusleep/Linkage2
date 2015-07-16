package test.service;

public class CalculatorImpl implements Calculator {

	@Override
	public int add(int a, int b) {
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return (a + b);
	}

	@Override
	public String add1(String a, String b, String c) {
		// TODO Auto-generated method stub
		return "" + (Integer.parseInt(a) + Integer.parseInt(b) + Integer.parseInt(c));
	}

}
