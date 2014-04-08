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

import org.json.JSONObject;

import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.content.Context;
import android.os.AsyncTask;

/**
 * This class executes the add new user account
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskAddUser extends AsyncTask<Void, Void, Integer> {

	protected String urlAddToAccount;
	protected String accessToken;
	protected Context mCurrent;
	protected JSONObject mObjInput;
	protected ListenerAddUser mListener;

	/**
	 * Initialize the Async task
	 * 
	 * @param current
	 * @param url
	 * @param token
	 * @param input
	 * @param listener
	 */
	public AsyncTaskAddUser(Context current, String url, String token,
			JSONObject input, ListenerAddUser listener) {
		mCurrent = current;
		urlAddToAccount = url;
		accessToken = token;
		mListener = listener;
		mObjInput = input;
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
	protected Integer doInBackground(Void... params) {
		int responseCode = 0;
		responseCode = Utils.postData(urlAddToAccount, mObjInput, accessToken,
				Constants.RequestHeaders.CONTENT_TYPE,
				Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
		return responseCode;
	}

	@Override
	protected void onPostExecute(Integer responseCode) {
		super.onPostExecute(responseCode);
		try {
			if (responseCode == Constants.ApiResponseCode.REQUEST_SUCCESSFUL_CREATED
					|| responseCode == Constants.ApiResponseCode.REQUEST_SUCCESSFUL_UPDATED) {
				mListener.onTaskSuccessful(responseCode);
			} else {
				mListener.onTaskFailed(responseCode);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();			
		}
	}
}
