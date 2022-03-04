/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import DTO.NameServer;

public class Home extends javax.swing.JFrame {
	 Socket soc;
	 Boolean status; 
	 String peer;
    /**
     * Creates new form Home
     * @throws IOException 
     * @throws UnknownHostException 
     */
    public Home(Boolean status, String peer) throws UnknownHostException, IOException {
    	this.status = status;
    	this.peer = peer;
    	
        initComponents();
        setLocationRelativeTo(this);
        receiveS();
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();


        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 806, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void receiveS() {//GEN-FIRST:event_jMenuItem1ActionPerformed

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                    	int count = 0;
                    	
                        try {
							
							soc = new Socket(NameServer.getServerAddress(),7749);
					    	DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
					    	
					    	dout.writeBoolean(false);
					    	dout.writeUTF(peer);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                            while (count<1600) {
                         	
                            	try {
                            		
	                                System.out.println("Started1!!!");
	                                BufferedImage img = ImageIO.read(soc.getInputStream());
	                                System.out.println("Started!!!");
	                                if(img!=null) {
	                                	count=0;
		                                float size = (float)img.getWidth()/img.getHeight();
		                                float size2 = (float)jPanel2.getWidth()/jPanel2.getHeight();   
		                                		                                
		                                float jpW = jPanel2.getHeight()*size;
		                                float jpH = jPanel2.getWidth()/size;
		                                
		                                if(size>size2) {
		                                	
		                                	jPanel2.getGraphics().drawImage(img, 0, (int)((jPanel2.getHeight()-jpH)/2), jPanel2.getWidth(), (int)jpH, null);
		                                }else {
		                                	jPanel2.getGraphics().drawImage(img, (int)((jPanel2.getWidth()-jpW)/2), 0, (int)jpW, jPanel2.getHeight(), null);
		                                }
	                                }else {
	                                	count++;
	                                }


                                } catch (Exception e) {
                                	e.printStackTrace();
                                }
                            	
                            }
                        try {
							soc.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        // close windows
                    }
                }).start();
    }//GEN-LAST:event_jMenuItem1ActionPerformed
    

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
