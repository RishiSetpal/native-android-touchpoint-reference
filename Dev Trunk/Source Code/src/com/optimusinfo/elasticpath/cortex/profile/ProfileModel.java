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
package com.optimusinfo.elasticpath.cortex.profile;

import java.io.Serializable;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

public class ProfileModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("family-name")
	public String mFamilyName;

	@SerializedName("given-name")
	public String mGivenName;

	@SerializedName("_addresses")
	public AddressInfo[] mAddresses;

	@SerializedName("_paymentmethods")
	public PaymentInfo[] mPaymentMethods;

	@SerializedName("_purchases")
	public PurchaseInfo[] mPurchases;

	public class PurchaseInfo implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("_element")
		protected PurchaseElement[] mElements;
	}

	public class PurchaseElement implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("purchase-number")
		public String mOrderNumber;

		@SerializedName("status")
		public String mStatus;

		@SerializedName("purchase-date")
		public PurchaseDate mPurchaseDate;

		@SerializedName("monetary-total")
		public PurchaseTotal[] mPurchaseTotals;

		@SerializedName("self")
		public PurchaseOrderLink mLink;

	}

	public class PurchaseOrderLink implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("href")
		protected String mHREF;

	}

	public class PurchaseDate implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("display-value")
		public String mDisplayValue;
	}

	public class PurchaseTotal implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("display")
		public String mDisplayValue;
	}

	public class PaymentInfo implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("_element")
		public PaymentElement[] mElement;
	}

	public class PaymentElement implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("display-value")
		public String mDisplayValue;
	}

	public class AddressInfo implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("_element")
		public AddressElement[] mElements;

	}

	public class AddressElement implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("name")
		public AddressName mAddressName;

		@SerializedName("address")
		public AddressDesc mAddressDesc;

		@SerializedName("self")
		public AddressLink mSelfLinks;

	}

	public class AddressLink implements Serializable {
		private static final long serialVersionUID = 1L;
		@SerializedName("href")
		public String mHREF;
	}

	public class AddressName implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("family-name")
		public String mFamilyName;

		@SerializedName("given-name")
		public String mGivenName;
	}

	public class AddressDesc implements Serializable {
		private static final long serialVersionUID = 1L;

		@SerializedName("country-name")
		public String mCountryName;

		@SerializedName("locality")
		public String mLocality;

		@SerializedName("postal-code")
		public String mPostalCode;

		@SerializedName("region")
		public String mRegion;

		@SerializedName("street-address")
		public String mStreetAddress;
		
		@SerializedName("extended-address")
		public String mExtendedAddress;
	}

	/**
	 * This method calls asynchronous task to get order details
	 * 
	 * @param current
	 * @param url
	 * @param token
	 * @param listener
	 */
	public static void getProfileDetails(Context current, String url,
			String token, ListenerProfileDetails listener) {
		try {
			AsyncTaskGetProfileDetails taskProfileDetails = new AsyncTaskGetProfileDetails(
					current, url, token,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener);
			taskProfileDetails.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method returns the billing address
	 * 
	 * @param address
	 * @return
	 */
	public static String getAddressLine(AddressElement address) {
		String addressLine = "";
		if (address.mAddressName.mGivenName != null) {
			addressLine = address.mAddressName.mGivenName.concat(" ");
		}
		if (address.mAddressName.mFamilyName != null) {
			addressLine = addressLine.concat(address.mAddressName.mFamilyName)
					.concat("\n");
		}

		if (address.mAddressDesc.mStreetAddress != null) {
			addressLine = addressLine.concat(
					address.mAddressDesc.mStreetAddress).concat("\n");
		}
		if (address.mAddressDesc.mLocality != null) {
			addressLine = addressLine.concat(address.mAddressDesc.mLocality)
					.concat(", ");
		}
		if (address.mAddressDesc.mRegion != null) {
			addressLine = addressLine.concat(address.mAddressDesc.mRegion)
					.concat(", ");
		}
		if (address.mAddressDesc.mCountryName != null) {
			addressLine = addressLine.concat(address.mAddressDesc.mCountryName)
					.concat(" ");
		}
		if (address.mAddressDesc.mPostalCode != null) {
			addressLine = addressLine.concat(address.mAddressDesc.mPostalCode);
		}
		return addressLine;
	}

	/**
	 * This method deletes an address
	 * 
	 * @param current
	 * @param url
	 * @param token
	 * @param listener
	 */
	public static void deleteAddress(Context current, String url, String token,
			ListenerDeleteAddress listener) {
		try {
			AsyncTaskDeleteAddress taskDelete = new AsyncTaskDeleteAddress(
					current, url, token,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener);
			taskDelete.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

}
