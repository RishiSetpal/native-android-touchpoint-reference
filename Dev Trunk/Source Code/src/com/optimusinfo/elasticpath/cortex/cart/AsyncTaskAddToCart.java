package com.optimusinfo.elasticpath.cortex.cart;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.os.AsyncTask;

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
			String entityString = Utils.postData(accessToken, urlAddToCartForm,
					objInput, contentTypeRequest);
			if (entityString != null && entityString.length() != 0) {
				mListener.onTaskSuccessful(entityString);
				return true;
			} else {
				mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
			}
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
