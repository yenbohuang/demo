package org.yenbo.akka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AkkaDemo {
	
	private static final Logger logger = LoggerFactory.getLogger(AkkaDemo.class);
	
	public static void main(String[] args) {
		
		// ActorSystem is a heavy object: create only one per application
		final ActorSystem system = ActorSystem.create("MySystem");
		logger.info("ActorSystem.name() = " + system.name());
		
		system.actorOf(DemoActor.props(42), "demo");
		
		final ActorRef myActor = system.actorOf(Props.create(MyUntypedActor.class), "myactor");
	}

}
