package DTO;

import java.io.Serializable;

public class Disconnect implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String peer;
	
	public Disconnect(String name, String peer) {
		this.name = name;
		this.peer = peer;
	}

	public String getName() {
		return name;
	}

	public String getPeer() {
		return peer;
	}

}
