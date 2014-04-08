package com.optimusinfo.elasticpath.cortex.purchase;

import org.apache.http.ParseException;
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
public class AsyncTaskPostPurchaseOrder extends AsyncTask<Void, Void, Boolean> {

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
	protected Boolean doInBackground(Void... params) {
		try {
			if (Utils.isNetworkAvailable(mCurrent)) {
				String response = Utils.postData(mUrlPurchaseOrder,
						accessToken, Constants.RequestHeaders.CONTENT_TYPE,
						Constants.RequestHeaders.AUTHORIZATION_INITIALIZER);
				if (response != null) {
					if (!TextUtils.isEmpty(response)) {
						mListener.onTaskSuccessful(response);
						return true;
					} else {
						mListener
								.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
					}
				} else {
					mListener.onTaskFailed(Constants.ErrorCodes.ERROR_SERVER);
				}
			} else {
				mListener.onTaskFailed(Constants.ErrorCodes.ERROR_NETWORK);
			}
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
