package com.rener;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SimpleGcmServer {

	private static final String PROJECT_API_KEY = "AIzaSyDAUpf_GpRVw4DQBnz7pr_aGzDEFoWJ3_8";
	public static final int LISTEN_PORT = 3002;
	public static final String LOOPBACK_ADDRESS = "http://127.0.0.1";
	public static final String TEST_REGID =
			"APA91bFZ_8vMJg2t6IaGAPj1z7tWPq6dpLnLsqRLrrQQlizE6OHOGC8p2t-zgkpz1C0EcFLDisMUA8yGny2ZTXZ7FthoNlKzj5ldz5ip_9zZuGQfxSNDIeMG31S96YsjX4vYjy9LR3ZQKSL033X9G9j89hCnYNAjdA";

	public static void main(String[] args) throws IOException {

		SimpleGcmServer server = new SimpleGcmServer();

		Scanner scanner = new Scanner(System.in);
		while(scanner.hasNextLine()) {
			String input = scanner.nextLine().trim();
			String[] pair = input.split("\\s", 2);
			String command = pair[0];
			String options = pair[1];
			switch(command) {
				case "stop":
					server.stop();
					break;
				case "start":
					server.start();
					break;
				case "gcm":
					gcmSend(options);
					break;
				default:
					System.out.println(command + ": Command not found");
			}
		}

	}

	private HttpServer server;

	private void start() throws IOException {

		System.out.println("Starting HTTP server...");

		server = HttpServer.create(new InetSocketAddress(LISTEN_PORT), 0);
		server.createContext("/", new SimpleHttpHandler());
		server.setExecutor(null);
		server.start();
		System.out.println("Listening on port " + LISTEN_PORT);
	}

	private void stop() {
		server.stop(0);
		System.out.println("Server stopped.");
	}

	private static void gcmSend(String args) {

		List<String> regIds = new ArrayList<>();
		regIds.add(TEST_REGID);

		Message.Builder builder = new Message.Builder();

		String[] pair = args.split("=", 2);
		builder.addData(pair[0], pair[1]);

		Message message = builder.build();
		System.out.println(message.toString());

		Sender sender = new Sender(PROJECT_API_KEY);
		try {
			MulticastResult result = sender.sendNoRetry(message, regIds);
			System.out.println(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class SimpleHttpHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange request) throws IOException {
			System.out.println("Request received, handling...");
			String protocol = request.getProtocol();
			String host = request.getRemoteAddress().toString();
			String method = request.getRequestMethod();
			String uri = request.getRequestURI().toString();
			StringBuilder headerBuilder = new StringBuilder();
			Map<String,List<String>> map = request.getRequestHeaders();
			for(Map.Entry<String, List<String>> e : map.entrySet()) {
				headerBuilder.append("\n").append(e.getKey()).append("=").append(e.getValue());
			}
			String headers = headerBuilder.toString();

			InputStream in = request.getRequestBody();
			StringBuilder bodyBuilder = new StringBuilder();
			int data = in.read();
			while(data != -1) {
				bodyBuilder.append(data);
				data = in.read();
			}
			in.close();
			String body = bodyBuilder.toString();

			String response = protocol+"\t"
					+host+"\t"
					+method+"\t"
					+uri+"\t"
					+headers+"\t"
					+body;

			System.out.println(response);

			request.sendResponseHeaders(200, response.length());

			OutputStream out = request.getResponseBody();
			out.write(response.getBytes());
			request.close();
		}
	}

	private static String toGcmJSON(List<String> regIds, Map<String,String> data) {

		//Build the registration id array string
		StringBuilder regBuilder = new StringBuilder();
		regBuilder.append("  \"registration_ids\" : [");
		for(int i=0; i<regIds.size(); i++) {
			String regId = regIds.get(i);
			regBuilder.append(regId);
			if(i != regIds.size()-1) regBuilder.append(",");
		}
		regBuilder.append("],\n");
		String regIdsJson = regBuilder.toString();

		//Build the data string
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append("  \"data\" : {\n");
		for(Map.Entry<String,String> e : data.entrySet()) {
			String entry = String.format("    \"%s\" : \"%s\",\n", e.getKey(), e.getValue());
			dataBuilder.append(entry);
		}
		dataBuilder.append("  },\n");
		String dataJson = dataBuilder.toString();

		return "{\n" + regIdsJson + dataJson + "}";

	}
}
