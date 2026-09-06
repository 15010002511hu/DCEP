package com.dcep.supergw.core.utils;

public final class Asserts {
	
	public Asserts() {
	}
	
	public static void check(boolean expression, String message) {
		if (!expression) {
			throw new IllegalStateException(message);
		}
	}

}
