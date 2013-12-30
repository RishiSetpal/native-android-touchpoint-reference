package com.optimusinfo.elasticpath.cortex.product.listing;

import android.content.Context;
import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

/**
 * This is asynchronous task to get the product listings
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskGetProductListing extends AsyncTask<Void, Void, Boolean> {

	Context mCurrent;
	String URL;
	String accessToken;
	String headerContentTypeString;
	String headerContentTypeValue;
	String headerAuthorizationTypeString;
	String headerAuthorizationTypeValue;
	String headerAccessTokenInitializer;
	ListenerGetProductListings mProdTaskListener;

	/**
	 * Initializes the variables for the Cortex Navigations task API
	 * 
	 * @param currentActivity
	 *            the activity making the call
	 * @param url
	 *            the base URL for the Cortex Navigation task
	 * @param zoomUrl
	 *            the zoom URL to get the list of navigations in a single HTTP
	 *            request
	 * @param route
	 *            the route URL corresponding to the Navigations API
	 * @param scope
	 *            the scope of the caller
	 * @param token
	 *            the access token to authenticate this Request
	 * @param contentTypeString
	 *            the string defining the content type text in the Request
	 *            header of this HTTP GET request
	 * @param contentTypeValue
	 *            the actual value of the content type of this request
	 * @param authoriztionString
	 *            the string defining the authorization text in the Request
	 *            header of this HTTP Get request
	 * @param accessTokenInitializer
	 *            the string to be prefixed before the access token as per the
	 *            guidelines
	 * @param listener
	 *            the listener which will receive the response of this
	 *            navigation task
	 */
	public AsyncTaskGetProductListing(Context current, String url,
			String token, String contentTypeString, String contentTypeValue,
			String authoriztionString, String accessTokenInitializer,
			ListenerGetProductListings listener) {
		mCurrent = current;
		URL = url;
		accessToken = token;
		headerContentTypeString = contentTypeString;
		headerContentTypeValue = contentTypeValue;
		headerAuthorizationTypeString = authoriztionString;
		headerAccessTokenInitializer = accessTokenInitializer;
		mProdTaskListener = listener;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {
			if (Utils.isNetworkAvailable(mCurrent)) {
				// Get the categories and their corresponding URLs
				String responseNavigation = Utils.getJSONFromCortexUrl(URL,
						accessToken, headerContentTypeValue,
						headerContentTypeString, headerAuthorizationTypeString,
						headerAccessTokenInitializer);
				Log.i("STRING RESPONSE", responseNavigation);
				if (responseNavigation != null
						&& responseNavigation.length() != 0) {
					if (0 == responseNavigation
							.compareTo(Integer
									.toString(Constants.ApiResponseCode.UNAUTHORIZED_ACCESS))) {
						mProdTaskListener.onAuthenticationFailed();
						return null;
					} else {
						mProdTaskListener.onTaskSuccessful(new Gson().fromJson(
								responseNavigation, ProductListing.class));
						return true;
					}
				}
			} else {
				mProdTaskListener
						.onTaskFailed(Constants.ErrorCodes.ERROR_NETWORK);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result != null && !result) {
			mProdTaskListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
		}

	}

}
