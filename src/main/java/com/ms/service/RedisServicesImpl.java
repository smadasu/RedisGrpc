/**
 * 
 */
package com.ms.service;

import com.ms.RedisServicesGrpc.RedisServicesImplBase;
import com.ms.StringRequest;
import com.ms.StringResponse;

import io.grpc.stub.StreamObserver;

/**
 * @author sreenath
 *
 */
public class RedisServicesImpl extends RedisServicesImplBase {
	
	private final RedisSocketServices redisSocketServices;

	public RedisServicesImpl() {
		this.redisSocketServices = new RedisSocketServices("localhost", 6379);
	}

	@Override
	public void get(StringRequest request, StreamObserver<StringResponse> responseObserver) {
		String value = redisSocketServices.get(request.getKey());
		StringResponse reply = StringResponse.newBuilder().setValue(value).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}

}
