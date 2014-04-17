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
package com.optimusinfo.elasticpath.cortex.purchase;

import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

/**
 * This class executes the add to default purcahse request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskPostPurchaseOrder extends AsyncTask<Void, Void, String> {

	protected String mUrlPurchaseOrder;
	protected String contentTypeRequest;
	protected String accessToken;
	protected Context mCurrent;

	protected ListenerCompletePurchaseOrder mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskPostPurchaseOrder(String url, String contentType,
			String token, ListenerCompletePurchaseOrder listener,
			Context current) {
		mUrlPurchaseOrder = url;
		contentTypeRequest = contentType;
		accessToken = token;
		mListener = listener;
		mCurrent = current;
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
		String response = null;
		try {
			response = Utils.postData(mUrlPurchaseOrder, accessToken,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	protected void onPostExecute(String response) {
		super.onPostExecute(response);
		try {
			if (response != null) {
				if (!TextUtils.isEmpty(response)) {
					mListener.onTaskSuccessful(response);
				} else {
					mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
				}
			} else {
				mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
