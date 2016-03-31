package com.olanh.pam_dataaccess.util;


public class DAOResponseUtil<T extends Object, E extends Object> {
	
	private T ResponseStatus;
	private E Response;
	
	public DAOResponseUtil(){
		
	}

	public T getStatus() {
		return ResponseStatus;
	}

	public DAOResponseUtil<T,E> setStatus(T responseStatus) {
		ResponseStatus = responseStatus;
		return this;
	}

	public E getResponse() {
		return Response;
	}

	public DAOResponseUtil<T,E> setResponse(E response) {
		Response = response;
		return this;
	}	
}
