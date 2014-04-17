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

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * This class executes the get geographies request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskGetGeographies extends AsyncTask<Void, Void, String> {

	protected Context mCurrent;
	protected String mURL;
	protected String mAccessToken;
	protected Class<?> modelClass;
	protected ListenerGetGeographies mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskGetGeographies(Context current, String url, String token,
			Class<?> model, ListenerGetGeographies listener) {
		mCurrent = current;
		mURL = url;
		mAccessToken = token;
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
		String responseGeographies = null;
		try {
			responseGeographies = Utils.getJSONFromCortexUrl(mURL,
					mAccessToken, Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return responseGeographies;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			if (result.length() != 0) {

				mListener.onTaskSuccessful(new Gson().fromJson(result,
						modelClass));

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
		}
	}
}
