package org.yenbo.akka;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Actors in Java are implemented by extending the UntypedActor class and implementing the onReceive method.
 * This method takes the message as a parameter.
 *
 */
public class MyUntypedActor extends UntypedActor {
	
	  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	 
	  public void onReceive(Object message) throws Exception {
	    if (message instanceof String) {
	      log.info("Received String message: {}", message);
	      getSender().tell(message, getSelf());
	    } else
	      unhandled(message);
	  }
	}