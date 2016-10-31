package com.ms.client;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.ms.RedisServicesGrpc;
import com.ms.StringRequest;

import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;

public class RedisServicesClient {
	private static final Logger logger = Logger.getLogger(RedisServicesClient.class.getName());

	private final ManagedChannel channel;
	private final RedisServicesGrpc.RedisServicesBlockingStub blockingStub;

	public RedisServicesClient(String host, int port) {
		channel = NettyChannelBuilder.forTarget(host + ":" + port).maxMessageSize(Integer.MAX_VALUE).usePlaintext(true).build();
		blockingStub = RedisServicesGrpc.newBlockingStub(channel);
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
	
	private String getValue(String key) {
		StringRequest request = StringRequest.newBuilder().setKey(key).build();
		StringBuilder responseBuilder = new StringBuilder();
		 blockingStub.get(request).forEachRemaining(response -> {
			 responseBuilder.append(response.getValue());
		});
		 return responseBuilder.toString();
	}

	public static void main(String[] args) throws InterruptedException {
		RedisServicesClient client = new RedisServicesClient("localhost", 50051);
		try {
			System.out.println(client.getValue("largekey"));
		} finally {
			client.shutdown();
		}
	}

}
