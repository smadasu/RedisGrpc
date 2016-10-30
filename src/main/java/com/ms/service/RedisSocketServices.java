package com.ms.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RedisSocketServices {
	
	private final InetSocketAddress redisAddress;
	
	public RedisSocketServices(String redisHost, int redisPort) {
		this.redisAddress = new InetSocketAddress(redisHost, redisPort);
	}
	
	public String get(String key) throws IOException {
		SocketChannel socketChannel = SocketChannel.open(redisAddress);
		String redisCommand = "get " + key + "\r\n";
		ByteBuffer keyByteBuffer = ByteBuffer.wrap(redisCommand.getBytes());
		int numberOfBytesWritten = socketChannel.write(keyByteBuffer);
		if (numberOfBytesWritten > 0) {
			ByteBuffer bytesRead = ByteBuffer.allocate(32);
			int numberOfBytesRead = socketChannel.read(bytesRead);
			if (numberOfBytesRead > 0) {
				bytesRead.flip();
				if (bytesRead.get() == '$') {
					byte nextByte;
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					while ((nextByte = bytesRead.get()) != '\r') {
						baos.write(nextByte);
					}
					String valueOf = new String(baos.toByteArray());
					System.out.println(valueOf);
				}
			}
		}
		return "";
	}

}
