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
package com.optimusinfo.elasticpath.cortex.profile.address;

import org.json.JSONObject;

import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This class executes the update address request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskUpdateAddress extends AsyncTask<Void, Void, Boolean> {

	Context mCurrent;
	String URL;
	String accessToken;
	String headerContentTypeString;
	String headerContentTypeValue;
	String headerAuthorizationTypeString;
	String headerAuthorizationTypeValue;
	String headerAccessTokenInitializer;
	JSONObject objInput;

	protected ListenerUpdateAdddress mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskUpdateAddress(Context current, String url, String token,
			String contentTypeString, String contentTypeValue,
			String authoriztionString, String accessTokenInitializer,
			ListenerUpdateAdddress listener, JSONObject input) {
		mCurrent = current;
		URL = url;
		accessToken = token;
		headerContentTypeString = contentTypeString;
		headerContentTypeValue = contentTypeValue;
		headerAuthorizationTypeString = authoriztionString;
		headerAccessTokenInitializer = accessTokenInitializer;
		mListener = listener;
		objInput = input;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			if (Utils.isNetworkAvailable(mCurrent)) {
				int responseCode = 0;
				responseCode = Utils.putRequest(URL, objInput, accessToken,
						headerContentTypeValue, headerContentTypeString,
						headerAuthorizationTypeString,
						headerAccessTokenInitializer);
				if (responseCode != 0) {
					mListener.onTaskSuccessful(responseCode);
					return true;
				}
			} else {
				mListener.onTaskFailed(Constants.ErrorCodes.ERROR_NETWORK);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result != null && !result) {
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
		}
	}
}
