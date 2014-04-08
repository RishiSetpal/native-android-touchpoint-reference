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
public class AsyncTaskGetProfileDetails extends AsyncTask<Void, Void, Boolean> {

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
	public AsyncTaskGetProfileDetails(Context current, String url, String token,
			String contentTypeString, String contentTypeValue,
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
	protected Boolean doInBackground(Void... params) {
		try {
			if (Utils.isNetworkAvailable(mCurrent)) {
				String responseProfileDetails = Utils.getJSONFromCortexUrl(URL,
						accessToken, headerContentTypeValue,
						headerContentTypeString, headerAuthorizationTypeString,
						headerAccessTokenInitializer);
				if (responseProfileDetails != null
						&& responseProfileDetails.length() != 0) {
					if (0 == responseProfileDetails
							.compareTo(Integer
									.toString(Constants.ApiResponseCode.UNAUTHORIZED_ACCESS))) {
						mListener.onAuthenticationFailed();
						return null;
					} else {
						mListener.onTaskSuccessful(new Gson().fromJson(
								responseProfileDetails, ProfileModel.class));
						return true;
					}
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
