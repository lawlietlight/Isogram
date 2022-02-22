package com.efficientsciences.cowsandbulls.wordwars.scenes;


import org.andengine.entity.text.Text;

import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;

public class LoadingScene extends BaseScene {

	private Text splash;
	


	public void createMenuLoading() {
		
		this.getBackground().setColor(0.996f, 0.823f, 0.231f);
		splash = resourcesManager.splash_region;
		splash.setColor(.01f,.01f,.01f);
		        
		splash.setScaleX(1.5f);
		splash.setPosition(centerX+20,centerY);
		attachChild(splash);
	}

	@Override
	public void createScene() {
		
		createMenuLoading(); 
		
		//createMenuSceneExample(); temp

	}
	
	
	@Override
	public void onBackKeyPressed() {
		System.exit(0);

	}

	@Override
	public SceneType getSceneType() {

	    return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene()
	{
	    splash.detachSelf();
	    splash.dispose();
	    this.detachSelf();
	    this.dispose();
	}
	


}
