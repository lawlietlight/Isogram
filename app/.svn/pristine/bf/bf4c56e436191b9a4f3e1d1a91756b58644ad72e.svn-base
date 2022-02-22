/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.efficientsciences.cowsandbulls.wordwars.socketCon;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.codebutler.android_websockets.WebSocketClient;
import com.google.android.gms.internal.io;

/**
 * This is an example of a WebSocket client.
 * <p>
 * In order to run this example you need a compatible WebSocket server.
 * Therefore you can either start the WebSocket server from the examples
 * by running {@link io.netty.example.http.websocketx.server.WebSocketServer}
 * or connect to an existing WebSocket server such as
 * <a href="http://www.websocket.org/echo.html">ws://echo.websocket.org</a>.
 * <p>
 * The client will attempt to connect to the URI passed to it as the first argument.
 * You don't have to specify any arguments if you want to connect to the example WebSocket server,
 * as this is the default.
 */
public final class WebSocketClientLocal {
	final String TAG= "Websocket Android Client";
	URI uri = new URI("ws://192.168.1.8:8080/websocket");
	
	WebSocketClient cc;
    public WebSocketClientLocal() throws Exception {
    	 List<BasicNameValuePair> extraHeaders = Arrays.asList(
 			    new BasicNameValuePair("Cookie", "session=abcd")
 			);
    	/*Thread.currentThread().setContextClassLoader(new ClassLoader() {
    	    @Override
    	    public Enumeration<URL> getResources(String resName) throws IOException {
    	        Log.i("Debug", "Stack trace of who uses " +
    	                "Thread.currentThread().getContextClassLoader()." +
    	                "getResources(String resName):", new Exception());
    	        return super.getResources(resName);
    	    }
    	});
    	Thread.currentThread().setContextClassLoader(WebSocketClient.class.getClassLoader());
    	*/
    	//Class.forName("org.java_websocket.client.WebSocketClient", false, ClassLoader.getSystemClassLoader());
    	 cc = new WebSocketClient(uri, new WebSocketClient.Listener() {
			    @Override
			    public void onConnect() {
			        Log.d(TAG, "Connected!");
			    }

			    @Override
			    public void onMessage(String message) {
			        Log.d(TAG, String.format("Got string message! %s", message));
			    }

			    @Override
			    public void onMessage(byte[] data) {
			        Log.d(TAG, String.format("Got binary message! %s", data));
			    }

			    @Override
			    public void onDisconnect(int code, String reason) {
			        Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
			    }

			    @Override
			    public void onError(Exception error) {
			        Log.e(TAG, "Error!", error);
			    }

			}, extraHeaders);
		
    	

    			cc.connect();

    			
    	
         

    }

    
	public void sendMessage(String msg)
			throws Exception {
       
		// Later
		cc.send("hello!");
		cc.send(new byte[] { (byte) 0xDE, (byte)0xAD, (byte)0xBE,(byte) 0xEF });
		
		
	}
	
	public void disconnect(){
		cc.disconnect();
	}
	
    
}
