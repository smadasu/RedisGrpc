/**
 * 
 */
package com.ms.server;

import java.io.IOException;
import java.util.logging.Logger;

import com.ms.service.RedisServicesImpl;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * @author sreenath
 *
 */
public class RedisServer {
	  private static final Logger logger = Logger.getLogger(RedisServer.class.getName());

	  /* The port on which the server should run */
	  private int port = 50051;
	  private Server server;

	  private void start() throws IOException {
	    server = ServerBuilder.forPort(port)
	        .addService(new RedisServicesImpl())
	        .build()
	        .start();
	    logger.info("Server started, listening on " + port);
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	      @Override
	      public void run() {
	        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
	        System.err.println("*** shutting down gRPC server since JVM is shutting down");
	        RedisServer.this.stop();
	        System.err.println("*** server shut down");
	      }
	    });
	  }

	  private void stop() {
	    if (server != null) {
	      server.shutdown();
	    }
	  }

	  /**
	   * Await termination on the main thread since the grpc library uses daemon threads.
	   */
	  private void blockUntilShutdown() throws InterruptedException {
	    if (server != null) {
	      server.awaitTermination();
	    }
	  }


	  
	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		final RedisServer server = new RedisServer();
		server.start();
		server.blockUntilShutdown();
	}

}
