package com.optimusinfo.elasticpath.cortex.cart;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.os.AsyncTask;

/**
 * This class executes the add to default cart request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskAddToCart extends AsyncTask<Void, Void, Boolean> {

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
	protected Boolean doInBackground(Void... params) {
		try {
			JSONObject objInput = new JSONObject();
			objInput.put("quantity", mQuantityToAdd);
			int responseCode = Utils.postData(urlAddToCartForm, objInput,
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
		} catch (JSONException e) {
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
