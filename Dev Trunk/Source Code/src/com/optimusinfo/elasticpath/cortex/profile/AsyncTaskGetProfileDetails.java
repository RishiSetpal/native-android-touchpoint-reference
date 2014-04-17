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
package com.optimusinfo.elasticpath.cortex.profile;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This class executes the get order details request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskGetProfileDetails extends AsyncTask<Void, Void, String> {

	Context mCurrent;
	String URL;
	String accessToken;
	String headerContentTypeString;
	String headerContentTypeValue;
	String headerAuthorizationTypeString;
	String headerAuthorizationTypeValue;
	String headerAccessTokenInitializer;

	protected ListenerProfileDetails mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskGetProfileDetails(Context current, String url,
			String token, String contentTypeString, String contentTypeValue,
			String authoriztionString, String accessTokenInitializer,
			ListenerProfileDetails listener) {
		mCurrent = current;
		URL = url;
		accessToken = token;
		headerContentTypeString = contentTypeString;
		headerContentTypeValue = contentTypeValue;
		headerAuthorizationTypeString = authoriztionString;
		headerAccessTokenInitializer = accessTokenInitializer;
		mListener = listener;
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
		String responseProfileDetails = null;
		try {

			responseProfileDetails = Utils.getJSONFromCortexUrl(URL,
					accessToken, headerContentTypeValue,
					headerContentTypeString, headerAuthorizationTypeString,
					headerAccessTokenInitializer);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		}
		return responseProfileDetails;
	}

	@Override
	protected void onPostExecute(String responseProfileDetails) {
		super.onPostExecute(responseProfileDetails);
		try {
			if (responseProfileDetails != null
					&& responseProfileDetails.length() != 0) {
				if (0 == responseProfileDetails
						.compareTo(Integer
								.toString(Constants.ApiResponseCode.UNAUTHORIZED_ACCESS))) {
					mListener.onAuthenticationFailed();
				} else {
					mListener.onTaskSuccessful(new Gson().fromJson(
							responseProfileDetails, ProfileModel.class));
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
