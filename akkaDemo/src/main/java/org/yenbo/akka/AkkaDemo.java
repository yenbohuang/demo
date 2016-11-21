package org.yenbo.akka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AkkaDemo {
	
	private static final Logger slf4jLogger = LoggerFactory.getLogger(AkkaDemo.class);
	
	private final ActorSystem system;
	private final LoggingAdapter akkaLogger;

	public AkkaDemo() {
		
		// ActorSystem is a heavy object: create only one per application
		system = ActorSystem.create("MySystem");
		
		// try SLF4J logger
		slf4jLogger.info("SLF4J logger: ActorSystem.name() = " + system.name());
		
		// try AKKA logger
		akkaLogger = Logging.getLogger(system, this);
		akkaLogger.info("AKKA logger: ActorSystem.name() = " + system.name());
		akkaLogger.debug("AKKA logger: ActorSystem.startTime() = " + system.startTime());
	}
	
	public void run() {
		
		system.actorOf(DemoActor.props(42), "demo");	
		final ActorRef myActor = system.actorOf(Props.create(MyUntypedActor.class), "myactor");
	}
	
	public static void main(String[] args) {
		new AkkaDemo().run();
	}
}
