package com.optimusinfo.elasticpath.cortex.checkout;

import org.apache.http.ParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.os.AsyncTask;

/**
 * This class executes the select option request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskSelectCheckOutOption extends
		AsyncTask<Void, Void, Boolean> {

	protected String urlSelectAction;
	protected String accessToken;

	protected ListenerSelectCheckoutOption mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskSelectCheckOutOption(String url, String token,
			ListenerSelectCheckoutOption listener) {
		urlSelectAction = url;
		accessToken = token;
		mListener = listener;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			int responseCode = Utils.postData(urlSelectAction, null,
					accessToken, Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
			if (responseCode == Constants.ApiResponseCode.REQUEST_SUCCESSFUL_CREATED
					|| responseCode == Constants.ApiResponseCode.REQUEST_SUCCESSFUL_UPDATED) {
				mListener.onTaskSuccessful(responseCode);
			} else {
				mListener.onTaskFailed(responseCode);
			}
			return true;
		} catch (ParseException e) {
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
