package com.optimusinfo.elasticpath.cortex;

import com.optimusinfo.elasticpath.cortex.common.Constants;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * This Class is the main application object called when application is started
 * 
 * @author Optimus
 * 
 */
public class EPApplication extends Application {

	protected SharedPreferences mObjPreferences;

	@Override
	public void onCreate() {
		super.onCreate();

		mObjPreferences = getSharedPreferences(
				Constants.Preferences.PREFERENCES_FILE_NAME,
				Context.MODE_PRIVATE);
		mObjPreferences.edit().remove(Constants.Preferences.LIST_CART).commit();
		mObjPreferences.edit()
				.remove(Constants.Preferences.KEY_ACCESS_TOKEN_PUBLIC).commit();
		mObjPreferences.edit()
				.remove(Constants.Preferences.KEY_ACCESS_TOKEN_REGISTERED)
				.commit();
	}

}
