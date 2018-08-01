package com.test.exception;

public class RedisLockTimeOutException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RedisLockTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}

	public RedisLockTimeOutException(String message) {
		super(message);
	}
}