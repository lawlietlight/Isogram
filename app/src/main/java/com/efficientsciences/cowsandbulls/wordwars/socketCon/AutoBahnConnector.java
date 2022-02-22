package com.efficientsciences.cowsandbulls.wordwars.socketCon;

import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;

public class AutoBahnConnector {
	public final WebSocketConnection  mConnection = new WebSocketConnection();
	final String wsuri = "ws://picshare.sytes.net/websocket";
       
       // final String wsuri = "ws://192.168.0.137:8888/";
        String TAG = "ArticlesListActivity"; 

       public AutoBahnConnector(final String msg){
    	   
    		 
    	   try {
    	      mConnection.connect(wsuri, new WebSocketConnectionHandler() {
    	 
    	         @Override
    	         public void onOpen(){
    	            Log.d(TAG, "Status: Connected to " + wsuri);
    	            mConnection.sendTextMessage(msg);

    	         }
    	 
    	         @Override
    	         public void onTextMessage(String payload) {
						Toast.makeText(ResourcesManager.getInstance().activity, " I'm Server Echo "+payload, Toast.LENGTH_LONG).show();
						if(SceneManager.getInstance().getCurrentSceneType().equals(SceneType.SCENE_HOST)){
							ResourcesManager.getInstance().payloadFromServer = payload;
						Toast.makeText(ResourcesManager.getInstance().activity,"Welcome Added", Toast.LENGTH_LONG).show();
						}
						else{
							Toast.makeText(ResourcesManager.getInstance().activity,"Not Host Scene", Toast.LENGTH_LONG).show();
						}
    	         }
    	 
    	         @Override
    	         public void onClose(int code, String reason) {
    	            Log.d(TAG, "Connection lost.");
    	         }
    	      });
    	   } catch (WebSocketException e) {
    	 
    	      Log.d(TAG, e.toString());
    	   }
       }
	
	 
    public void start(String msg) {
	 
	  mConnection.sendTextMessage(msg);
	}
    
    
}
