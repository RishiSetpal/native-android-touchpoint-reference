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
package com.optimusinfo.elasticpath.cortex.category;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

/**
 * This is asynchronous task to get the categories
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskGetCategories extends AsyncTask<Void, Void, String> {

	Context mCurrent;
	String URL;
	String accessToken;
	Class<?> modelClass;
	ListenerGetCategories mListener;

	/**
	 * Initializes the variables for the Cortex Navigations task API
	 * 
	 * @param currentActivity
	 *            the activity making the call
	 * @param url
	 *            the base URL for the Cortex Navigation task
	 * @param zoomUrl
	 *            the zoom URL to get the list of navigations in a single HTTP
	 *            request
	 * @param route
	 *            the route URL corresponding to the Navigations API
	 * @param scope
	 *            the scope of the caller
	 * @param token
	 *            the access token to authenticate this Request
	 * @param contentTypeString
	 *            the string defining the content type text in the Request
	 *            header of this HTTP GET request
	 * @param contentTypeValue
	 *            the actual value of the content type of this request
	 * @param authoriztionString
	 *            the string defining the authorization text in the Request
	 *            header of this HTTP Get request
	 * @param accessTokenInitializer
	 *            the string to be prefixed before the access token as per the
	 *            guidelines
	 * @param listener
	 *            the listener which will receive the response of this
	 *            navigation task
	 */
	public AsyncTaskGetCategories(Context current, String url, String token,
			Class<?> model, ListenerGetCategories listener) {
		mCurrent = current;
		URL = url;
		accessToken = token;
		mListener = listener;
		modelClass = model;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (!Utils.isNetworkAvailable(mCurrent)) {
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_NETWORK);
			cancel(true);
			return;
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		String responseNavigation = null;
		try {
			// Get the categories and their corresponding URLs
			responseNavigation = Utils.getJSONFromCortexUrl(URL, accessToken,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return responseNavigation;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			if (result.length() != 0) {
				if (0 == result
						.compareTo(Integer
								.toString(Constants.ApiResponseCode.UNAUTHORIZED_ACCESS))) {
					mListener.onAuthenticationFailed();
				} else {
					mListener.onTaskSuccessful(new Gson().fromJson(result,
							modelClass));
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();			
		} catch (JsonParseException e) {
			e.printStackTrace();
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
		}
	}
}
