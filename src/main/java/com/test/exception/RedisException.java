package com.test.exception;

public class RedisException  extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedisException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisException(String message) {
		super(message);
	}
}