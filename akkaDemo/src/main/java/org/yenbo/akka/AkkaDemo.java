package org.yenbo.akka;

import akka.actor.ActorSystem;

public class AkkaDemo {

	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create();
		System.out.println(system.settings());
	}

}
