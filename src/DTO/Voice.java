package DTO;

import javax.sound.sampled.AudioFormat;
public class Voice {

	public static AudioFormat getAudioFormat()
	{
		float sapleRate=16000.0F;
		int sampleSizeInbits=16;
		int chanel=1;
		boolean signed=true;
		boolean bigEndian=false;
		return new AudioFormat(sapleRate, sampleSizeInbits, chanel, signed, bigEndian);
	}

}
