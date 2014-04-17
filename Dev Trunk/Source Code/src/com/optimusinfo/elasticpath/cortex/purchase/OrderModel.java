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
package com.optimusinfo.elasticpath.cortex.purchase;

import java.io.Serializable;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

public class OrderModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("status")
	protected String mStatus;

	@SerializedName("purchase-number")
	protected String mOrderNumber;

	@SerializedName("tax-total")
	protected OrderTax mTax;

	@SerializedName("monetary-total")
	protected OrderTotal[] mTotal;

	@SerializedName("_paymentmeans")
	protected PaymentMeans[] mPaymentMeans;

	@SerializedName("_billingaddress")
	protected BillingInfo[] mBillingInfo;

	@SerializedName("_lineitems")
	protected LineItems[] mLineItems;

	/**
	 * 
	 * @param url
	 * @param contentType
	 * @param token
	 * @param listener
	 */
	public static void postPurchaseOrder(Context current, String url,
			String contentType, String token,
			ListenerCompletePurchaseOrder listener) {
		try {
			AsyncTaskPostPurchaseOrder taskPurchaseOrder = new AsyncTaskPostPurchaseOrder(
					url, contentType, token, listener, current);
			taskPurchaseOrder.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method calls asynchronous task to get order details
	 * 
	 * @param current
	 * @param url
	 * @param token
	 * @param listener
	 */
	public static void getOrderDetails(Context current, String url,
			String token, ListenerOrderDetails listener) {
		try {
			AsyncTaskGetOrderDetails taskOrderDetails = new AsyncTaskGetOrderDetails(
					current, url.concat(Constants.ZoomUrl.URL_ZOOM_ORDER),
					token, Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener);
			taskOrderDetails.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public class LineItems implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_element")
		protected ItemElement[] mItems;
	}

	public class ItemElement implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("name")
		protected String mItemName;
		@SerializedName("quantity")
		protected String mQuantity;
	}

	public class OrderTax implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("display")
		protected String mDisplayValue;

	}

	public class OrderTotal implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("display")
		protected String mDisplayValue;

	}

	public class PaymentMeans implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_element")
		protected PaymentElement[] mElements;
	}

	public class PaymentElement implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("display-value")
		protected String mDisplayValue;
	}

	public class BillingInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("name")
		AddressName mAddressName;
		@SerializedName("address")
		Address mAddress;
	}

	public class AddressName implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("family-name")
		String mFamilyName;
		@SerializedName("given-name")
		String mGivenName;
	}

	public class Address implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SerializedName("family-name")
		String mCountryName;

		@SerializedName("locality")
		String mLocality;

		@SerializedName("postal-code")
		String mPostalCode;

		@SerializedName("region")
		String mRegion;

		@SerializedName("street-address")
		String mStreetAddress;
	}

	/**
	 * This method returns the billing address
	 * 
	 * @param address
	 * @return
	 */
	public static String getAddressLine(BillingInfo address) {
		String addressLine = "";
		if (address.mAddressName.mGivenName != null) {
			addressLine = address.mAddressName.mGivenName.concat(" ");
		}
		if (address.mAddressName.mFamilyName != null) {
			addressLine = addressLine.concat(address.mAddressName.mFamilyName)
					.concat("\n");
		}

		if (address.mAddress.mStreetAddress != null) {
			addressLine = addressLine.concat(address.mAddress.mStreetAddress)
					.concat("\n");
		}
		if (address.mAddress.mLocality != null) {
			addressLine = addressLine.concat(address.mAddress.mLocality)
					.concat(", ");
		}
		if (address.mAddress.mRegion != null) {
			addressLine = addressLine.concat(address.mAddress.mRegion).concat(
					", ");
		}
		if (address.mAddress.mCountryName != null) {
			addressLine = addressLine.concat(address.mAddress.mCountryName)
					.concat(" ");
		}
		if (address.mAddress.mPostalCode != null) {
			addressLine = addressLine.concat(address.mAddress.mPostalCode);
		}
		return addressLine;
	}
	
	public static String getOrderContents(ItemElement[] mElements) {
		String response = "";
		for (ItemElement currElement : mElements) {
			response = response.concat(currElement.mItemName).concat(" (x")
					.concat(currElement.mQuantity).concat(")\n");
		}
		return response;
	}
	
	

}
