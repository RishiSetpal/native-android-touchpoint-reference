/*
 * Copyright � 2014 Elastic Path Software Inc. All rights reserved.
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
package com.optimusinfo.elasticpath.cortex.cart;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This class executes the get cart form request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskGetCartForm extends AsyncTask<Void, Void, String> {

	protected Context mCurrent;
	protected String mURL;
	protected String mAccessToken;
	protected String mHeaderContentTypeString;
	protected String mHeaderContentTypeValue;
	protected String mHeaderAuthorizationTypeString;
	protected String mHeaderAccessTokenInitializer;

	protected ListenerGetCartForm mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskGetCartForm(Context current, String url, String token,
			String contentTypeString, String contentTypeValue,
			String authoriztionString, String accessTokenInitializer,
			ListenerGetCartForm listener) {
		mCurrent = current;
		mURL = url;
		mAccessToken = token;
		mHeaderContentTypeString = contentTypeString;
		mHeaderContentTypeValue = contentTypeValue;
		mHeaderAuthorizationTypeString = authoriztionString;
		mHeaderAccessTokenInitializer = accessTokenInitializer;
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
		String responseAddToCartForm = null;
		try {
			responseAddToCartForm = Utils.getJSONFromCortexUrl(mURL,
					mAccessToken, mHeaderContentTypeValue,
					mHeaderContentTypeString, mHeaderAuthorizationTypeString,
					mHeaderAccessTokenInitializer);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		}
		return responseAddToCartForm;
	}

	@Override
	protected void onPostExecute(String responseAddToCartForm) {
		super.onPostExecute(responseAddToCartForm);
		try {
			if (responseAddToCartForm != null
					&& responseAddToCartForm.length() != 0) {
				if (0 == responseAddToCartForm
						.compareTo(Integer
								.toString(Constants.ApiResponseCode.UNAUTHORIZED_ACCESS))) {
					mListener.onAuthenticationFailed();
				} else {
					mListener.onTaskSuccessful(new Gson().fromJson(
							responseAddToCartForm, AddToCartModel.class));
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
