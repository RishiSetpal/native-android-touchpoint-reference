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

	/**
	 * This method shows the application user messages
	 * 
	 * @param current
	 * @param message
	 */
	public static void showNotificationToastFromResources(Context current,
			int resId) {
		Toast.makeText(current, current.getString(resId), Toast.LENGTH_LONG)
				.show();
	}

}
