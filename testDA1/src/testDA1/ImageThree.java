package testDA1;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;

import com.github.sarxos.webcam.Webcam;

public class ImageThree extends Thread
{
	public void run() {

		Socket s;
		try {
			s = new Socket("localhost",7788);
			ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());
			Webcam webcam=Webcam.getDefault();
		    webcam.open();

			while (true) {

				if (!webcam.isOpen()) {
					break;
				}

				BufferedImage image = webcam.getImage();
				ImageIcon icon=new ImageIcon(image);
				out.writeObject(icon);
				out.flush();    
				Client.img_client.setIcon(icon);
				if (image == null) {
					break;
				}
	           
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
}
