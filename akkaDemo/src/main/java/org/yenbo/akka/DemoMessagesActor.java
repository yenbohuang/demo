package org.yenbo.akka;

import akka.actor.UntypedActor;

/**
 * Another good practice is to declare what messages an Actor can receive as close to the actor definition 
 * as possible (e.g. as static classes inside the Actor or using other suitable class), which makes it 
 * easier to know what it can receive.
 *
 */
public class DemoMessagesActor extends UntypedActor {

	static public class Greeting {
		
		private final String from;

		public Greeting(String from) {
			this.from = from;
		}

		public String getGreeter() {
			return from;
		}
	}

	public void onReceive(Object message) throws Exception {
		
		if (message instanceof Greeting) {
			getSender().tell("Hello " + ((Greeting) message).getGreeter(), getSelf());
		} else
			unhandled(message);
	}
}