/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	}

}
