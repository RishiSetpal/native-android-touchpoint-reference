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
package com.optimusinfo.elasticpath.cortex.authentication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

/**
 * This is an Asynchronous Task used for authentication
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskAuthentication extends AsyncTask<Void, Void, String> {

	protected String mCortexUrl;
	protected Context mCurrent;
	protected Gson mObjGson;
	protected ListenerAsyncTaskAuthentication mListener;
	protected String mUsername;
	protected String mPassword;
	protected String mScope;
	protected String mRole;

	/**
	 * Initializes the variables
	 * 
	 * @param currentActivity
	 *            the activity calling the async task
	 * @param url
	 *            the base url for this async task
	 * @param listener
	 *            the listener receiving the responses
	 * @param userNameLoginForm
	 *            the user name for which the auth token has to be generated
	 * @param passwordLoginForm
	 *            the password using which the auth token has to be generated
	 * @param scope
	 *            the scope of the user making the call
	 * @param role
	 *            the role of the user requesting the call
	 */
	public AsyncTaskAuthentication(Context current, String url,
			ListenerAsyncTaskAuthentication listener, String userNameLoginForm,
			String passwordLoginForm, String scope, String role) {
		mCurrent = current;
		mObjGson = new Gson();
		mListener = listener;
		mUsername = userNameLoginForm;
		mPassword = passwordLoginForm;
		mCortexUrl = url;
		mScope = scope;
		mRole = role;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!Utils.isNetworkAvailable(mCurrent)) {
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_NETWORK, "");
			cancel(true);
			return;
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		String responseJson = null;
		try {
			// Create the HTTP post Request
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Add grant type
			parameters.add(new BasicNameValuePair(
					Constants.Authentication.HEADER_GRANT_TYPE, "password"));
			// Add user name only if provided
			if (mUsername != null && mUsername.length() != 0) {
				parameters.add(new BasicNameValuePair(
						Constants.Authentication.HEADER_USERNAME, mUsername));
			}
			// Add pass word only if provided
			if (mPassword != null && mPassword.length() != 0) {
				parameters.add(new BasicNameValuePair(
						Constants.Authentication.HEADER_PASSWORD, mPassword));
			}
			// Add scope header
			parameters.add(new BasicNameValuePair(
					Constants.Authentication.HEADER_SCOPE, mScope));
			// Add role header
			parameters.add(new BasicNameValuePair(
					Constants.Authentication.HEADER_ROLE, mRole));
			// Post the Request
			UrlEncodedFormEntity objEntity = new UrlEncodedFormEntity(
					parameters, HTTP.UTF_8);

			responseJson = Utils.postData(
					mCortexUrl.concat(Constants.Routes.AUTH_ROUTE), objEntity,
					Constants.Config.CONTENT_TYPE);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return responseJson;
	}

	@Override
	protected void onPostExecute(String entityResponse) {
		super.onPostExecute(entityResponse);
		try {
			mListener.onTaskComplete(mObjGson.fromJson(entityResponse,
					Authentication.class));
		} catch (NullPointerException e) {
			e.printStackTrace();			
		} catch (JsonParseException e) {
			e.printStackTrace();
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER,
					entityResponse);
		}
	}
}
