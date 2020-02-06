
package org.mwc.asset.netCore.test;

import java.io.IOException;
import java.util.Arrays;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

public class ThreadTest
{
	public static void main(final String[] args) throws IOException,
			InterruptedException
	{
		Server s = new Server();
		s.start();
		s.bind(1927);
		printThreads("server started");

		final Client c = new Client();
		c.start();
		c.connect(5000, "LOCALHOST", 1927);
		printThreads("client connected");
		final Server s1 = s;
		s.stop();
		printThreads("server stopped");

		s = new Server();
		s.start();
		s.bind(1928);
		printThreads("new server started"); // new server thread will be last on
		// the list.

		c.stop();
		printThreads("client stopped");

		c.start();
		c.connect(5000, "localhost", 1928);
		printThreads("client connected to second server");

		c.stop();
		s.stop();
		s1.stop();
		printThreads("both stopped");
	}

	private static void printThreads(final String message) throws InterruptedException
	{
		// tick:
		Thread.sleep(2000L);
		final Thread[] threads = new Thread[Thread.activeCount()];
		Thread.enumerate(threads);
		System.out.println(message + " :  " + Arrays.asList(threads));
	}
}
