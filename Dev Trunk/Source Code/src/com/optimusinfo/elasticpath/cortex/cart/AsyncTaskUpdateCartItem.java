package com.optimusinfo.elasticpath.cortex.cart;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import android.content.Context;
import android.os.AsyncTask;

/**
 * This class executes the get complete cart request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskUpdateCartItem extends AsyncTask<Void, Void, Boolean> {

	Context mCurrent;
	String URL;
	String accessToken;
	String headerContentTypeString;
	String headerContentTypeValue;
	String headerAuthorizationTypeString;
	String headerAuthorizationTypeValue;
	String headerAccessTokenInitializer;
	String mQuantityToAdd;

	protected ListenerUpdateCartItems mListener;

	/**
	 * Initialize the Async Task
	 * 
	 * @param url
	 * @param quantity
	 * @param contentType
	 * @param token
	 */
	public AsyncTaskUpdateCartItem(Context current, String url, String token,
			String contentTypeString, String contentTypeValue,
			String authoriztionString, String accessTokenInitializer,
			ListenerUpdateCartItems listener, String quantity) {
		mCurrent = current;
		URL = url;
		accessToken = token;
		headerContentTypeString = contentTypeString;
		headerContentTypeValue = contentTypeValue;
		headerAuthorizationTypeString = authoriztionString;
		headerAccessTokenInitializer = accessTokenInitializer;
		mListener = listener;
		mQuantityToAdd = quantity;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {
			if (Utils.isNetworkAvailable(mCurrent)) {
				int responseCode = 0;
				JSONObject objInput = new JSONObject();
				objInput.put("quantity", mQuantityToAdd);

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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
