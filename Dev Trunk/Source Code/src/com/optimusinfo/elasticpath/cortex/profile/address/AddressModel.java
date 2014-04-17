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
package com.optimusinfo.elasticpath.cortex.profile.address;

import org.json.JSONObject;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

public class AddressModel {

	
	public static void getAddressForm(Context current, String addtoCartUrl,
			String accessToken, ListenerGetAddressForm mListner) {
		try {
			new AsyncTaskGetAddressForm(current, addtoCartUrl, accessToken,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					mListner).execute();
		} catch (NullPointerException e) {
			e.printStackTrace();

		}
	}
	
	
	/**
	 * This method adds a Address
	 * 
	 * @param urlAddToCartForm
	 * @param contentType
	 * @param accessToken
	 * @param listener
	 * @param input
	 */
	public static void addAddress(String urlAddToCartForm, String contentType,
			String accessToken, ListenerAddAddress listener, JSONObject input) {
		try {
			AsyncTaskAddAddress taskAddAddress = new AsyncTaskAddAddress(
					urlAddToCartForm, urlAddToCartForm, accessToken, input,
					listener);
			taskAddAddress.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static void updateAddress(Context current, String url, String token,
			ListenerUpdateAdddress listener, JSONObject input) {
		try {
			AsyncTaskUpdateAddress taskUpdate = new AsyncTaskUpdateAddress(
					current, url, token,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener, input);
			taskUpdate.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public class CreateAddressFormModel {
		@SerializedName("_addresses")
		public AddressInfo[] mAddresses;
	}

	public class AddressInfo {
		@SerializedName("_addressform")
		public AddressFormElement[] mElements;
	}

	public class AddressFormElement {
		@SerializedName("links")
		public AddressLink[] mLinks;
	}

	public class AddressLink {
		@SerializedName("rel")
		public String mRelation;

		@SerializedName("href")
		public String mHREF;
	}

	public static String getCreateAddressLink(AddressLink[] mLinks) {
		for (AddressLink curr : mLinks) {
			if (curr.mRelation.equalsIgnoreCase("createaddressaction")) {
				return curr.mHREF;
			}
		}
		return "";
	}

}
