package com.optimusinfo.elasticpath.cortex.checkout;

import java.io.Serializable;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Constants;

public class CheckoutModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("_billingaddressinfo")
	AddressInfo[] mBillingAddressInfo;

	@SerializedName("_cart")
	CartTotal[] mCartTotal;

	@SerializedName("_total")
	Total[] mTotal;

	@SerializedName("_deliveries")
	Deliveries[] mDeliveries;

	@SerializedName("_paymentmethodinfo")
	PaymentMethodInfo[] mPaymentInfo;

	@SerializedName("_purchaseform")
	PurchaseForm[] mPurchaseForm;

	@SerializedName("_tax")
	Tax[] mTax;

	public static void getCheckOutSummary(Context current, String url,
			String token, ListenerCheckOutOrder listener) {
		try {
			AsyncTaskGetCheckout taskCheckOut = new AsyncTaskGetCheckout(
					current, url, token,
					Constants.RequestHeaders.CONTENT_TYPE_STRING,
					Constants.RequestHeaders.CONTENT_TYPE,
					Constants.RequestHeaders.AUTHORIZATION_STRING,
					Constants.RequestHeaders.AUTHORIZATION_INITIALIZER,
					listener);
			taskCheckOut.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method selects the Check out option
	 * 
	 * @param url
	 * @param accessToken
	 * @param listener
	 */
	public static void selectOption(String url, String accessToken,
			ListenerSelectCheckoutOption listener) {
		try {
			AsyncTaskSelectCheckOutOption taskSelectionOption = new AsyncTaskSelectCheckOutOption(
					url, accessToken, listener);
			taskSelectionOption.execute();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static String getAddressLine(DescriptionElement address) {
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

	public class Tax implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("cost")
		Cost[] mCosts;
	}

	public class PurchaseForm implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("links")
		LinkElement[] mPurchaseLinks;

	}

	public class AddressInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_selector")
		AddressSelector[] mSelector;
	}

	public class AddressSelector implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_choice")
		AddressChoice[] mChoice;
		@SerializedName("_chosen")
		AddressChoice[] mChosen;
	}

	public class AddressChoice implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SerializedName("_description")
		DescriptionElement[] mDescription;

		@SerializedName("links")
		LinkElement[] mLinks;

	}

	public class DescriptionElement implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("name")
		AddressName mAddressName;
		@SerializedName("address")
		Address mAddress;
	}

	public class LinkElement implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("rel")
		String mRelation;
		@SerializedName("href")
		String mHREF;
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

	public class CartTotal implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@SerializedName("_total")
		protected Total[] mCartTotal;

		@SerializedName("total-quantity")
		protected String mTotalQuantity;
	}

	public class Total implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("cost")
		public Cost[] mCosts;

		public Cost[] getCosts() {
			return mCosts;
		}

		public void setCosts(Cost[] mCosts) {
			this.mCosts = mCosts;
		}
	}

	public class Cost implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("display")
		protected String mTotalCost;

		public String getTotalCost() {
			return mTotalCost;
		}

		public void setTotalCost(String mTotalCost) {
			this.mTotalCost = mTotalCost;
		}
	}

	public class Deliveries implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_element")
		DeliveriesElement[] mElement;

		public class DeliveriesElement implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SerializedName("delivery-type")
			String mDeliveryType;

			@SerializedName("_destinationinfo")
			AddressInfo[] mDestinationInfo;

			@SerializedName("_shippingoptioninfo")
			ShippingOptionInfo[] mShippingOptionInfo;

		}

		public class ShippingOptionInfo implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SerializedName("_selector")
			ShippingOptionSelector[] mSelector;
		}

		public class ShippingOptionSelector implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SerializedName("_choice")
			ShippingOptionChoice[] mChoice;

			@SerializedName("_chosen")
			ShippingOptionChoice[] mChosen;
		}

		public class ShippingOptionChoice implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			@SerializedName("_description")
			ShippingElement[] mDescriptionElement;

			@SerializedName("links")
			LinkElement[] mLinks;

			public class ShippingElement implements Serializable {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@SerializedName("carrier")
				String mCarrier;
				@SerializedName("display-name")
				String mDisplayName;
				@SerializedName("cost")
				Cost[] mCosts;
			}
		}
	}

	public class PaymentMethodInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@SerializedName("_selector")
		PaymentSelector[] mSelector;

		public class PaymentSelector implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@SerializedName("_choice")
			PaymentChoice[] mChoice;

			@SerializedName("_chosen")
			PaymentChoice[] mChosen;

			public class PaymentChoice implements Serializable {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				@SerializedName("_description")
				OptionDescription[] mDescription;

				@SerializedName("links")
				LinkElement[] mLinks;

				public class OptionDescription implements Serializable {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					@SerializedName("display-value")
					public String mDisplayValue;
				}
			}
		}
	}

	public static String getSelectActionLink(LinkElement[] mElements) {
		for (LinkElement curr : mElements) {
			if (curr.mRelation.equalsIgnoreCase("selectaction")) {
				return curr.mHREF;
			}
		}
		return "";
	}

	public static String getOrderActionLink(LinkElement[] mElements) {
		for (LinkElement curr : mElements) {
			if (curr.mRelation.equalsIgnoreCase("submitorderaction")) {
				return curr.mHREF;
			}
		}
		return "";
	}

	public static boolean isInfoRequired(LinkElement[] mElements) {
		for (LinkElement curr : mElements) {
			if (curr.mRelation.equalsIgnoreCase("needinfo")) {
				return true;
			}
		}
		return false;
	}

}
