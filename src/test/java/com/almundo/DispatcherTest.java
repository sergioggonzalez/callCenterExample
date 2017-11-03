package com.almundo;

import com.almundo.Employer.EmployerType;
import com.almundo.Call.State;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DispatcherTest {
	
	private Call c1;
	private Call c2;
	private Call c3;
	private Call c4;
	private Employer e1;
	private Employer e2;
	private Employer e3;
	private Employer e4;
	private Employer e5;
	private Employer e6;
	private Employer e7;
	private Dispatcher dispatcher;

	Queue<Call> calls;

	@Before
	public void setUp() {
		
		c1 = new Call("Call 1");
		c2 = new Call("Call 2");
		c3 = new Call("Call 3");
		c4 = new Call("Call 4");

		calls = new ConcurrentLinkedQueue<Call>();
		calls.add(c1);
		calls.add(c2);
		calls.add(c3);
		calls.add(c4);

		dispatcher = new Dispatcher();
		e1 = new Employer(EmployerType.DIRECTOR, "Director 1");
		e2 = new Employer(EmployerType.SUPERVISOR, "Supervisor 1");
		e3 = new Employer(EmployerType.OPERADOR, "Operador 1");
		e4 = new Employer(EmployerType.OPERADOR, "Operador 2");
		e5 = new Employer(EmployerType.OPERADOR, "Operador 3");
		e6 = new Employer(EmployerType.OPERADOR, "operador 4");
		e7 = new Employer(EmployerType.OPERADOR, "operador 5");




		dispatcher = new Dispatcher();
		dispatcher.addEmployer(e1);
		dispatcher.addEmployer(e2);
		dispatcher.addEmployer(e4);
		dispatcher.addEmployer(e5);
		dispatcher.addEmployer(e1);
		dispatcher.addEmployer(e2);
		dispatcher.addEmployer(e3);
		dispatcher.addEmployer(e6);
		dispatcher.addEmployer(e7);
	}

	@Test
	public void takeCallTest() {
		calls.forEach(c -> dispatcher.dispatchCall(c));
		dispatcher.waitForAllCalls();
		Assert.assertTrue("All calls was attended",
		dispatcher.getAttendedCalls().stream().allMatch(l -> l.getState().equals(State.FINISHED)));
	}

	@Test
	public void employersPriorityTest() {
		calls.forEach(c -> dispatcher.dispatchCall(c));
		dispatcher.waitForAllCalls();

		dispatcher.getAttendedCalls().stream().forEach(l -> l.getOperador().getEmployerType().toString());

		Assert.assertTrue("All calls was attended by Employer type: Operador",
				dispatcher.getAttendedCalls().stream()
						.allMatch(l -> l.getOperador().getEmployerType().equals(EmployerType.OPERADOR)));
	}

	@Test
	public void moreCallsThanConcurrentLimit() {

		Call c5 = new Call("Call 5");
		Call c6 = new Call("Call 6");
		Call c7 = new Call("Call 7");
		Call c8 = new Call("Call 8");
		Call c9 = new Call("Call 9");
		Call c10 = new Call("Call 10");
		Call c11 = new Call("Call 11");
		Call c12 = new Call("Call 12");

		calls.add(c5);
		calls.add(c6);
		calls.add(c7);
		calls.add(c8);
		calls.add(c9);
		calls.add(c10);
		calls.add(c11);
		calls.add(c12);

		calls.forEach(c -> dispatcher.dispatchCall(c));
		dispatcher.waitForAllCalls();

		Assert.assertEquals(5, dispatcher.getAttendedCalls().stream().filter(l -> l.getHoldFlag()).count(), 0);
		Assert.assertEquals(0, dispatcher.getPendingCalls().size(), 0);
		Assert.assertTrue("All calls was attended",
				dispatcher.getAttendedCalls().stream().allMatch(l -> l.getState().equals(State.FINISHED)));
	}


}