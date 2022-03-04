package DTO;

import java.io.Serializable;

public class MessageUDP implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String receiver;
	private byte[] video ;
	private byte[] voice ;
	public MessageUDP(String name, String receiver, byte[] video, byte[] voice) {
		this.receiver = receiver;
		this.name = name;
		this.video = video;
		this.voice = voice;
		
	}
	
	public String getName() {
		return name;
	}
	public byte[] getVoice() {
		return voice;
	}
	public byte[] getVideo() {
		return video;
	}
	public String getReceiver() {
		return receiver;
	}
}
