package org.yenbo.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

/**
 * It is a good idea to provide static factory methods on the UntypedActor which help keeping the creation 
 * of suitable Props as close to the actor definition as possible. This also allows usage of the 
 * Creator-based methods which statically verify that the used constructor actually exists instead relying 
 * only on a runtime check.
 *
 */
public class DemoActor extends UntypedActor {
	  
	  /**
	   * Create Props for an actor of this type.
	   * @param magicNumber The magic number to be passed to this actor’s constructor.
	   * @return a Props for creating this actor, which can then be further configured
	   *         (e.g. calling `.withDispatcher()` on it)
	   */
	  public static Props props(final int magicNumber) {
	    return Props.create(new Creator<DemoActor>() {
	      private static final long serialVersionUID = 1L;
	 
	      @Override
	      public DemoActor create() throws Exception {
	        return new DemoActor(magicNumber);
	      }
	    });
	  }
	  
	  final int magicNumber;
	 
	  public DemoActor(int magicNumber) {
	    this.magicNumber = magicNumber;
	  }
	  
	  @Override
	  public void onReceive(Object msg) {
	    // some behavior here
	  }
	  
	}