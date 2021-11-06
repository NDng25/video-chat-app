package DTO;

import java.io.Serializable;
import java.util.List;

public class Request implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<String> users;
	public Request(List<String> users) {
		this.users = users;
	}
	public List<String> getUsers() {
		return users;
	}
}
