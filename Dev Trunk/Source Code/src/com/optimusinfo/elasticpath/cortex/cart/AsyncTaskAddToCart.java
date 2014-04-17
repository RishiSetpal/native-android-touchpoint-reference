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
package com.optimusinfo.elasticpath.cortex.cart;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.os.AsyncTask;

/**
 * This class executes the add to default cart request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskAddToCart extends AsyncTask<Void, Void, Integer> {

	protected int mQuantityToAdd;
	protected String urlAddToCartForm;
	protected String contentTypeRequest;
	protected String accessToken;

	protected ListenerAddToCart mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskAddToCart(String url, int quantity, String contentType,
			String token, ListenerAddToCart listener) {
		urlAddToCartForm = url;
		mQuantityToAdd = quantity;
		contentTypeRequest = contentType;
		accessToken = token;
		mListener = listener;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int responseCode = 0;
		try {
			JSONObject objInput = new JSONObject();
			objInput.put("quantity", mQuantityToAdd);
			responseCode = Utils.postData(urlAddToCartForm, objInput,
					accessToken, Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		} catch (JsonParseException e) {
			e.printStackTrace();
			mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
		}
	}
}
