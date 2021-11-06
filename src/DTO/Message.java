package DTO;

import java.io.Serializable;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String receiver;
	private String message;
	public Message(String name, String receiver, String message) {
		this.receiver = receiver;
		this.name = name;
		this.message = message;
	}
	
	public String getName() {
		return name;
	}
	public String getMessage() {
		return message;
	}

	public String getReceiver() {
		return receiver;
	}
}
