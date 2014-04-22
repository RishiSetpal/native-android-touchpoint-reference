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
package com.optimusinfo.elasticpath.cortex.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressInfo;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressSelector;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.PaymentMethodInfo;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.DeliveriesElement;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.ShippingOptionInfo;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.ShippingOptionSelector;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.PaymentMethodInfo.PaymentSelector;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortex.purchase.PurchaseFragment;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class CheckoutFragment extends EPFragment {

	protected String mCheckOutLink;
	protected String isOptionsSet;

	protected CheckoutModel mObjCheckOut;

	protected ListenerCheckOutOrder mListener;
	protected ListenerSelectCheckoutOption mOptionListener;

	protected View mViewParent, mNoMethodView;

	protected RelativeLayout mLayout;
	private GridView mAddressGridView, mMethodGridView,
			mShippingOptionGridView;

	/**
	 * Check out constructor
	 * 
	 * @param mCheckOutLink
	 */
	public CheckoutFragment(String mCheckOutLink) {
		super();
		this.mCheckOutLink = mCheckOutLink;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewNavigation = inflater.inflate(R.layout.activity_checkout,
				container, false);
		mViewParent = viewNavigation;
		mLayout = (RelativeLayout) mViewParent.findViewById(R.id.rlCart);
		if (mObjCheckOut == null) {
			getCheckoutSummary();
		} else {
			setUpViews();
		}

		return viewNavigation;
	}

	public void getCheckoutSummary() {
		mListener = new ListenerCheckOutOrder() {
			@Override
			public void onTaskSuccessful(CheckoutModel response) {
				mObjCheckOut = response;
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						setUpViews();
					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO - For Future Req
						NotificationUtils.showErrorToast(getActivity(),
								errorCode);
					}
				});
			}

			@Override
			public void onAuthenticationFailed() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
					}
				});
			}
		};

		showProgressDialog(true);
		mLayout.setVisibility(View.INVISIBLE);
		CheckoutModel.getCheckOutSummary(getActivity(),
				mCheckOutLink.concat(Constants.ZoomUrl.URL_ZOOM_CHECKOUT),
				getUserAuthenticationToken(), mListener);

	}

	public void updateSelectOption(String url) {
		mOptionListener = new ListenerSelectCheckoutOption() {

			@Override
			public void onTaskSuccessful(final int responseCode) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
						if (responseCode != 404) {
							onRefreshData();
						}
					}
				});

			}

			@Override
			public void onTaskFailed(int errorCode) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						// TODO For Future Req
					}
				});
			}
		};

		showProgressDialog(true);
		mLayout.setVisibility(View.INVISIBLE);
		CheckoutModel.selectOption(url, getUserAuthenticationToken(),
				mOptionListener);
	}

	private void setUpViews() {
		mLayout.setVisibility(View.VISIBLE);

		mAddressGridView = (GridView) mViewParent.findViewById(R.id.gvAddress);
		mMethodGridView = (GridView) mViewParent
				.findViewById(R.id.gvPaymentMethods);

		mShippingOptionGridView = (GridView) mViewParent
				.findViewById(R.id.gvShippingOptions);

		mNoMethodView = getActivity().getLayoutInflater().inflate(
				R.layout.view_no_payment_method, null);
		if (((ViewGroup) mMethodGridView.getParent()).getChildCount() < 3) {
			((ViewGroup) mMethodGridView.getParent()).addView(mNoMethodView);
		}

		TextView tvQuant = (TextView) mViewParent.findViewById(R.id.tvQuantity);
		tvQuant.setText(mObjCheckOut.mCartTotal[0].mTotalQuantity.concat(" ")
				.concat(this.getString(R.string.prefixItems)));

		TextView tvOrderSubtotal = (TextView) mViewParent
				.findViewById(R.id.tvOrderSubtotal);
		tvOrderSubtotal
				.setText(mObjCheckOut.mCartTotal[0].mCartTotal[0].mCosts[0].mTotalCost);

		TextView tvOrderTotal = (TextView) mViewParent
				.findViewById(R.id.tvOrderTotal);
		tvOrderTotal.setText(mObjCheckOut.mTotal[0].mCosts[0].mTotalCost);

		Button mReturnCart = (Button) mViewParent
				.findViewById(R.id.btReturnCart);
		mReturnCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

		Button mCompleteOrder = (Button) mViewParent
				.findViewById(R.id.btCompletePurchase);

		if (CheckoutModel
				.isInfoRequired(mObjCheckOut.mPurchaseForm[0].mPurchaseLinks)) {
			mCompleteOrder.setEnabled(false);
		} else {
			mCompleteOrder.setEnabled(true);
		}

		mCompleteOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String purchaseUrl = CheckoutModel
						.getOrderActionLink(mObjCheckOut.mPurchaseForm[0].mPurchaseLinks);
				detachChildFragments();
				PurchaseFragment mObjFragment = new PurchaseFragment(
						purchaseUrl, null, getActivity().getString(
								R.string.labelTextViewContinue));
				addFragmentToBreadcrumb("Order", R.id.fragment_container,
						mObjFragment);
			}
		});

		TextView tvContinue = (TextView) mViewParent
				.findViewById(R.id.tvContinueShopping);
		tvContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});

		showAddressGridView();
		showPaymentMethods();
		showShippingOptions();
	}

	/**
	 * This method shows the address gridview
	 */
	public void showAddressGridView() {

		AddressSelector mBillingElements = null;
		AddressSelector mShippingElements = null;
		AddressInfo[] mBillingAddressInfo = mObjCheckOut.mBillingAddressInfo;

		if (mBillingAddressInfo != null
				&& mBillingAddressInfo[0].mSelector != null) {
			mBillingElements = mBillingAddressInfo[0].mSelector[0];
		}

		Deliveries[] mDeliveries = mObjCheckOut.mDeliveries;
		if (mDeliveries != null) {
			DeliveriesElement[] mElement = mDeliveries[0].mElement;
			if (mElement != null) {
				AddressInfo[] mDestinationInfo = mElement[0].mDestinationInfo;
				if (mDestinationInfo != null) {
					mShippingElements = mDestinationInfo[0].mSelector[0];
				}
			}
		}
		AddressAdapter mAdapter = new AddressAdapter(this, mBillingElements,
				mShippingElements);
		if (mAdapter != null) {
			mAddressGridView.setAdapter(mAdapter);
			Utils.setGridViewHeightBasedOnChildren(mAddressGridView,
					getIntegerResource(R.integer.numColumns));
		}

	}

	/**
	 * 
	 */
	public void showPaymentMethods() {
		PaymentMethodInfo[] mPaymentInfo = mObjCheckOut.mPaymentInfo;
		PaymentSelector mElements = null;
		PaymentMethodsAdapter mAdapter = null;

		if (mPaymentInfo != null && mPaymentInfo[0].mSelector != null) {
			mElements = mPaymentInfo[0].mSelector[0];
		}

		if (mElements != null) {
			mAdapter = new PaymentMethodsAdapter(this, mElements);
		}

		if (mAdapter != null) {
			mNoMethodView.setVisibility(View.GONE);
			mMethodGridView.setAdapter(mAdapter);
			Utils.setGridViewHeightBasedOnChildren(mMethodGridView,
					getIntegerResource(R.integer.numColumns));
		} else {
			mNoMethodView.setVisibility(View.VISIBLE);
		}

	}

	public void showShippingOptions() {
		Deliveries[] mDeliveries = mObjCheckOut.mDeliveries;
		ShippingOptionSelector mElements = null;
		ShippingOptionsAdapter mAdapter = null;

		if (mDeliveries != null) {
			DeliveriesElement[] mElement = mDeliveries[0].mElement;
			if (mElement != null) {
				ShippingOptionInfo[] mShippingOptionInfo = mElement[0].mShippingOptionInfo;
				if (mShippingOptionInfo != null) {
					mElements = mShippingOptionInfo[0].mSelector[0];
				}
			}
		}

		if (mElements != null) {
			mAdapter = new ShippingOptionsAdapter(this, mElements);
		}

		if (mAdapter != null) {
			((ViewGroup) mShippingOptionGridView.getParent())
					.setVisibility(View.VISIBLE);
			mShippingOptionGridView.setAdapter(mAdapter);
			Utils.setSingleCoulmnGridViewHeightBasedOnChildren(mShippingOptionGridView);
		} else {
			((ViewGroup) mShippingOptionGridView.getParent())
					.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * This method sets the shipping amount
	 * 
	 * @param amount
	 */
	public void setShippingAmount(String amount) {
		TextView tvShippingTotal = (TextView) mViewParent
				.findViewById(R.id.tvShippingTotal);
		tvShippingTotal.setText(amount);
	}

	@Override
	public void onRefreshData() {
		// TODO Auto-generated method stub
		getCheckoutSummary();
	}

	@Override
	public void detachChildFragments() {

	}

	@Override
	public void onBackPressed() {
		getActivity().onBackPressed();
	}

	@Override
	public void onAuthenticationSucessful() {
		onRefreshData();
	}
}
