package com.optimusinfo.elasticpath.cortex.common;

import com.optimusinfo.elasticpath.cortexAPI.R;

import android.content.Context;
import android.widget.Toast;

public class NotificationUtils {

	/**
	 * This method shows the application default toast messages when any error
	 * occurs in asynchronous task
	 * 
	 * @param current
	 *            - current context
	 * @param erroCode
	 *            - error code
	 */
	public static void showErrorToast(Context current, int erroCode) {
		switch (erroCode) {
		case Constants.ErrorCodes.ERROR_NETWORK:
			Toast.makeText(current,
					current.getString(R.string.msgErrorNetworkUnavailable),
					Toast.LENGTH_LONG).show();
			break;
		case Constants.ErrorCodes.ERROR_SERVER:
			Toast.makeText(current, current.getString(R.string.msgErrorServer),
					Toast.LENGTH_LONG).show();
			break;
		default:
			Toast.makeText(
					current,
					erroCode + " - "
							+ current.getString(R.string.msgErrorServer),
					Toast.LENGTH_LONG).show();
			break;
		}
	}

	/**
	 * This method shows the application user messages
	 * 
	 * @param current
	 * @param message
	 */
	public static void showNotificationToast(Context current, String message) {
		Toast.makeText(current, message, Toast.LENGTH_LONG).show();
	}

}
