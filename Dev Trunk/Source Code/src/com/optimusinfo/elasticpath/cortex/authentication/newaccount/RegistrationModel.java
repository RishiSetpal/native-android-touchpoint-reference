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
package com.optimusinfo.elasticpath.cortex.authentication.newaccount;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class RegistrationModel {

	/**
	 * This method returns the JSON for firing the new new user form request
	 * 
	 * @param familyName
	 * @param givenName
	 * @param password
	 * @param username
	 * @return
	 */
	public static JSONObject getUserRegistrationJson(String familyName,
			String givenName, String password, String username) {
		JSONObject regObject = new JSONObject();
		try {
			regObject.put("family-name", familyName);
			regObject.put("given-name", givenName);
			regObject.put("password", password);
			regObject.put("username", username);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return regObject;
	}

	/**
	 * This method adds a new user
	 * 
	 * @param urlAddAccountForm
	 * @param contentType
	 * @param accessToken
	 * @param listener
	 * @param input
	 */
	public static void addNewAccount(Context current, String urlAddAccountForm,
			String accessToken, ListenerAddUser listener, JSONObject input) {
		try {
			AsyncTaskAddUser taskAddAccount = new AsyncTaskAddUser(current,
					urlAddAccountForm, accessToken, input, listener);
			taskAddAccount.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

}
