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

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PurchaseFragment extends EPFragment {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	public String mPurchaseUrl;

	protected String mOrderDetailsUrl, mBackTitle;

	protected ListenerCompletePurchaseOrder mPostPurchaseListner;
	protected ListenerOrderDetails mOrderDetailsListener;
	protected OrderModel mObjOrder;

	protected LinearLayout mLayout;
	public boolean mIsOrderConfirmed = false;
	protected View mViewParent;

	/**
	 * Constructor
	 * 
	 * @param mPurchaseUrl
	 * @param mOrderDetailsUrl
	 */
	public PurchaseFragment(String mPurchaseUrl, String mOrderDetailsUrl,
			String backTitle) {
		super();
		this.mPurchaseUrl = mPurchaseUrl;
		this.mOrderDetailsUrl = mOrderDetailsUrl;
		this.mBackTitle = backTitle;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewNavigation = inflater.inflate(R.layout.activity_purchase,
				container, false);
		mViewParent = viewNavigation;
		// Initialize views
		initializeViews();
		if (mObjOrder == null) {
			if (mOrderDetailsUrl != null) {
				getOrderDetails(mOrderDetailsUrl);
			} else if (mPurchaseUrl != null) {
				postPurchaseOrder(mPurchaseUrl);
			}
		} else {
			setUpViews();
		}

		return viewNavigation;
	}

	/**
	 * This method initializes the page view elements
	 */
	private void initializeViews() {
		mLayout = (LinearLayout) mViewParent.findViewById(R.id.lLayout);
	}

	/**
	 * This function posts the add to cart object
	 * 
	 * @param mPostUrl
	 */
	public void postPurchaseOrder(String mPostUrl) {
		mPostPurchaseListner = new ListenerCompletePurchaseOrder() {
			@Override
			public void onTaskSuccessful(final String response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showProgressDialog(false);
						mIsOrderConfirmed = true;
						mOrderDetailsUrl = response;
						getOrderDetails(mOrderDetailsUrl);
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
						NotificationUtils.showErrorToast(getActivity(),
								Constants.ErrorCodes.ERROR_SERVER);
					}
				});
			}
		};
		showProgressDialog(true);
		mLayout.setVisibility(View.INVISIBLE);
		if (!TextUtils.isEmpty(mPostUrl)) {
			OrderModel.postPurchaseOrder(getActivity(), mPostUrl,
					Constants.Config.CONTENT_TYPE_PURCHASE_ORDER,
					getUserAuthenticationToken(), mPostPurchaseListner);
		}

	}

	/**
	 * This method gets the order details
	 * 
	 * @param urlOrder
	 */
	public void getOrderDetails(String urlOrder) {

		mOrderDetailsListener = new ListenerOrderDetails() {
			@Override
			public void onTaskSuccessful(OrderModel response) {
				mObjOrder = response;
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
						NotificationUtils.showErrorToast(getActivity(),
								Constants.ErrorCodes.ERROR_SERVER);
					}
				});
			}
		};

		showProgressDialog(true);
		mLayout.setVisibility(View.INVISIBLE);
		if (!TextUtils.isEmpty(urlOrder)) {
			OrderModel.getOrderDetails(getActivity(), urlOrder,
					getUserAuthenticationToken(), mOrderDetailsListener);
		}
	}

	public void setUpViews() {
		mLayout.setVisibility(View.VISIBLE);

		TextView tvOrderNumber = (TextView) mViewParent
				.findViewById(R.id.tvOrderNumber);
		tvOrderNumber.setText(mObjOrder.mOrderNumber);

		TextView tvBillingAddress = (TextView) mViewParent
				.findViewById(R.id.tvBillingAddress);
		tvBillingAddress.setText(OrderModel
				.getAddressLine(mObjOrder.mBillingInfo[0]));

		TextView tvPaymentCard = (TextView) mViewParent
				.findViewById(R.id.tvPaymentCard);
		tvPaymentCard
				.setText(mObjOrder.mPaymentMeans[0].mElements[0].mDisplayValue);

		TextView tvOrderStatus = (TextView) mViewParent
				.findViewById(R.id.tvOrderStatus);
		tvOrderStatus.setText(mObjOrder.mStatus);

		TextView tvOrderContent = (TextView) mViewParent
				.findViewById(R.id.tvOrderContent);
		tvOrderContent.setText(OrderModel
				.getOrderContents(mObjOrder.mLineItems[0].mItems));

		TextView tvOrderSubtotal = (TextView) mViewParent
				.findViewById(R.id.tvOrderSubtotal);
		tvOrderSubtotal.setText(mObjOrder.mTotal[0].mDisplayValue);

		TextView tvOrderTax = (TextView) mViewParent
				.findViewById(R.id.tvOrderTax);
		tvOrderTax.setText(mObjOrder.mTax.mDisplayValue);

		/*
		 * TextView tvOrderShipping = (TextView)
		 * mViewParent.findViewById(R.id.tvOrderShipping);
		 * tvOrderShipping.setText(mObjOrder.mTax.mDisplayValue);
		 */
		TextView tvOrderTotal = (TextView) mViewParent
				.findViewById(R.id.tvOrderTotal);
		tvOrderTotal.setText(mObjOrder.mTotal[0].mDisplayValue);

		TextView tvContinue = (TextView) mViewParent
				.findViewById(R.id.tvContinueShopping);
		tvContinue.setText(mBackTitle);
		tvContinue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// Incase user presses back button from top navigation
		if (mIsOrderConfirmed && mPurchaseUrl != null) {
			((EPFragmentActivity) getActivity()).clearTop();
		} else {
			getFragmentManager().popBackStack();
		}
	}

	@Override
	public void onRefreshData() {
		// TODO Auto-generated method stub
		if (mOrderDetailsUrl != null) {
			getOrderDetails(mOrderDetailsUrl);
		} else if (mPurchaseUrl != null) {
			postPurchaseOrder(mPurchaseUrl);
		}
	}

	@Override
	public void detachChildFragments() {
		// No child fragments added
	}

	@Override
	public void onAuthenticationSucessful() {
		// TODO Auto-generated method stub

	}

}
