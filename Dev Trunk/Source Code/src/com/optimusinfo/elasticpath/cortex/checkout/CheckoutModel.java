package com.optimusinfo.elasticpath.cortex.checkout;

import java.io.Serializable;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.cart.AsyncTaskGetCompleteCart;
import com.optimusinfo.elasticpath.cortex.cart.ListenerGetCompleteCartItems;
import com.optimusinfo.elasticpath.cortex.common.Constants;

public class CheckoutModel implements Serializable {

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

	public class Tax implements Serializable {
		@SerializedName("cost")
		Cost[] mCosts;
	}

	public class PurchaseForm implements Serializable {
		@SerializedName("self")
		PurchaseLinks mPurchaseLinks;

		public class PurchaseLinks implements Serializable {
			@SerializedName("href")
			String mPurchaseLink;
		}
	}

	public class AddressInfo implements Serializable {
		@SerializedName("_selector")
		AddressSelector[] mSelector;
	}

	public class AddressSelector implements Serializable {
		@SerializedName("_choice")
		AddressChoice[] mChoice;
		@SerializedName("_chosen")
		AddressChoice[] mChosen;
	}

	public class AddressChoice implements Serializable {
		@SerializedName("_description")
		DescriptionElement[] mDescription;
	}

	public class DescriptionElement implements Serializable {
		@SerializedName("name")
		AddressName mAddressName;
		@SerializedName("address")
		Address mAddress;
	}

	public class Address implements Serializable {

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
		@SerializedName("family-name")
		String mFamilyName;
		@SerializedName("given-name")
		String mGivenName;
	}

	public class CartTotal implements Serializable {
		@SerializedName("_total")
		protected Total[] mCartTotal;

		@SerializedName("total-quantity")
		protected String mTotalQuantity;
	}

	public class Total implements Serializable {
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

		@SerializedName("_element")
		DeliveriesElement[] mElement;

		public class DeliveriesElement implements Serializable {
			@SerializedName("delivery-type")
			String mDeliveryType;

			@SerializedName("_destinationinfo")
			AddressInfo[] mDestinationInfo;

			@SerializedName("_shippingoptioninfo")
			ShippingAddressInfo[] mShippingAddressInfo;

		}

		public class ShippingAddressInfo implements Serializable {
			@SerializedName("_selector")
			ShippingAddressSelector[] mSelector;
		}

		public class ShippingAddressSelector implements Serializable {
			@SerializedName("_choice")
			ShippingAddressChoice[] mChoice;
		}

		public class ShippingAddressChoice implements Serializable {
			@SerializedName("_description")
			ShippingElement[] mDescriptionElement;

			public class ShippingElement implements Serializable {
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
		@SerializedName("_selector")
		PaymentSelector[] mSelector;

		public class PaymentSelector implements Serializable {
			@SerializedName("_choice")
			PaymentChoice[] mChoice;

			@SerializedName("_chosen")
			PaymentChoice[] mChosen;

			public class PaymentChoice implements Serializable {

				@SerializedName("_description")
				OptionDescription[] mDescription;

				public class OptionDescription implements Serializable {
					@SerializedName("display-value")
					public String mDisplayValue;
				}
			}
		}
	}

}
