package com.rener.gcm;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

/**

 */
public class WorkerRunnable implements Runnable{

	public static final String WELCOME = "Welcome to the RENeR server!";
	public static final String STATUS_LINE_200 = "HTTP/1.1 200 OK\n\n";

	protected Socket clientSocket;
	protected String clientHost;

	public WorkerRunnable(Socket clientSocket) {
		this.clientSocket = clientSocket;
		this.clientHost = clientSocket.getRemoteSocketAddress().toString();
	}

	public void run() {

		String statusLine = STATUS_LINE_200;

		try {
			InputStream input  = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			long started = System.currentTimeMillis();
			String message = WELCOME+"\nYou are connecting from "+clientHost;
			output.write((statusLine + message).getBytes());
			output.close();
			input.close();
			long time = System.currentTimeMillis() - started;
			System.out.println("Request processed: " + time);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}