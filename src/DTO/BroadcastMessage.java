package DTO;

import java.io.Serializable;

public class BroadcastMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	
	public BroadcastMessage(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}