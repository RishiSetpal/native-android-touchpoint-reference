package com.optimusinfo.elasticpath.cortex.purchase;

import org.apache.http.ParseException;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.Utils;

import android.os.AsyncTask;

/**
 * This class executes the add to default purcahse request
 * 
 * @author Optimus
 * 
 */
public class AsyncTaskPostPurchaseOrder extends AsyncTask<Void, Void, Boolean> {

	protected String mUrlPurchaseOrder;
	protected String contentTypeRequest;
	protected String accessToken;

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
			String token, ListenerCompletePurchaseOrder listener) {
		mUrlPurchaseOrder = url;
		contentTypeRequest = contentType;
		accessToken = token;
		mListener = listener;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		try {

			int responseCode = Utils.postData(mUrlPurchaseOrder, null,
					accessToken, Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
			if (responseCode == 201) {
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
