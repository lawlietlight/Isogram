package com.efficientsciences.cowsandbulls.wordwars.socketCon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Commu extends Thread {
	   Socket socket;
	   InputStream inputStream;
	   OutputStream outputStream;
	   String words;
	   public Commu() {
		   String msg="hihhhk";
		   words=msg;
	      try {
	         socket=new Socket();
	         
	         socket.connect(new InetSocketAddress("192.168.1.8", 8080));

	         inputStream=socket.getInputStream();
	         outputStream=socket.getOutputStream();
	         this.start();
	      } catch(Exception e) {
	         System.out.println(e);
	      }
	   }

	   public void run() {
	      while(true) {
	         byte[] buffer=new byte[1024];
	         try {
				inputStream.read(buffer);
				outputStream.write(words.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         System.out.println(buffer[0]);
	      }
	   }
	   
	   public static void main(String[] args) throws Exception{
		   WebSocketClientLocal ws=new WebSocketClientLocal();
	   }
	}