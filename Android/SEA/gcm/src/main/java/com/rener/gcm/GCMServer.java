package com.rener.gcm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GCMServer implements Runnable {

	static final String GOOGLE_API_KEY = "AIzaSyDAUpf_GpRVw4DQBnz7pr_aGzDEFoWJ3_8";
	static final int LISTEN_PORT = 3002;

	public static void main(String[] args) {

		GCMServer server = new GCMServer(LISTEN_PORT);
		new Thread(server).start();

		try {
			Thread.sleep(20 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	protected int serverPort;
	protected ServerSocket serverSocket;
	protected boolean isStopped = false;
	protected Thread runningThread;

	public GCMServer(int port) {
		this.serverPort = port;
	}

	@Override
	public void run(){
		synchronized(this){
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while(! isStopped()){
			Socket clientSocket;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if(isStopped()) {
					System.out.println("Server Stopped.") ;
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			new Thread(new WorkerRunnable(clientSocket)).start();
		}
		System.out.println("Server Stopped.") ;
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop(){
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port 8080", e);
		}
	}
}
