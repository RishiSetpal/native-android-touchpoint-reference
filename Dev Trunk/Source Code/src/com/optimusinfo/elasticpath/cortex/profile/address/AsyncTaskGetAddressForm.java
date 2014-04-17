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
import com.optimusinfo.elasticpath.cortex.profile.address.AddressModel.CreateAddressFormModel;

import android.content.Context;
import android.os.AsyncTask;

/**
 * This class executes the get cart form request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskGetAddressForm extends AsyncTask<Void, Void, Boolean> {

	protected Context mCurrent;
	protected String mURL;
	protected String mAccessToken;
	protected String mHeaderContentTypeString;
	protected String mHeaderContentTypeValue;
	protected String mHeaderAuthorizationTypeString;
	protected String mHeaderAccessTokenInitializer;

	protected ListenerGetAddressForm mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskGetAddressForm(Context current, String url, String token,
			String contentTypeString, String contentTypeValue,
			String authoriztionString, String accessTokenInitializer,
			ListenerGetAddressForm listener) {
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
	protected Boolean doInBackground(Void... params) {
		try {
			if (Utils.isNetworkAvailable(mCurrent)) {
				String responseAddToCartForm = Utils.getJSONFromCortexUrl(mURL,
						mAccessToken, mHeaderContentTypeValue,
						mHeaderContentTypeString,
						mHeaderAuthorizationTypeString,
						mHeaderAccessTokenInitializer);
				if (responseAddToCartForm != null
						&& responseAddToCartForm.length() != 0) {
					if (0 == responseAddToCartForm
							.compareTo(Integer
									.toString(Constants.ApiResponseCode.UNAUTHORIZED_ACCESS))) {
						mListener.onAuthenticationFailed();
						return true;
					} else {
						mListener.onTaskSuccessful(new Gson().fromJson(
								responseAddToCartForm, CreateAddressFormModel.class));
						return true;
					}
				}
			} else {
				mListener.onTaskFailed(Constants.ErrorCodes.ERROR_NETWORK);
				return true;
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
