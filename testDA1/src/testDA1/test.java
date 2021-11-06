package testDA1;


import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.github.sarxos.webcam.Webcam;

public class test 
{

		public static void main (String[] args)
		{
			
			Webcam webcam = Webcam.getDefault ();
	
			webcam.open();
		}
}
