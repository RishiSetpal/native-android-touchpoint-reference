package com.optimusinfo.elasticpath.cortex.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.purchase.PurchaseFragment;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class CheckoutFragment extends EPFragment {

	protected String mCheckOutLink;
	protected CheckoutModel mObjCheckOut;
	protected RelativeLayout mLayout;
	protected String isOptionsSet;

	protected ListenerCheckOutOrder mListener;
	protected ListenerSelectCheckoutOption mOptionListener;
	protected View mViewParent;

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
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						setUpViews();
					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
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
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// TODO For Future Req
					}
				});
			}
		};

		getActivity().setProgressBarIndeterminateVisibility(true);
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
						getActivity().setProgressBarIndeterminateVisibility(
								false);
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
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// TODO For Future Req
					}
				});

			}

			@Override
			public void onAuthenticationFailed() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// TODO For Future Req
					}
				});
			}
		};

		getActivity().setProgressBarIndeterminateVisibility(true);
		mLayout.setVisibility(View.INVISIBLE);
		CheckoutModel.selectOption(url, getUserAuthenticationToken(),
				mOptionListener);
	}

	private void setUpViews() {
		mLayout.setVisibility(View.VISIBLE);
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

		addChildFragment("Address", R.id.frame_address,
				(EPFragment) new AddressFragment());
		addChildFragment("Payment_Method", R.id.frame_payment_methods,
				(EPFragment) new PaymentMethodFragment());
		addChildFragment("Shipping Option", R.id.frame_shipping_options,
				(EPFragment) new ShippingOptionsFragment());

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
						purchaseUrl, null);
				addFragmentToBreadcrumb("Order", R.id.fragment_container,
						mObjFragment);
			}
		});
	}

	public void setShippingAmount(String amount) {
		TextView tvShippingTotal = (TextView) mViewParent
				.findViewById(R.id.tvShippingTotal);
		tvShippingTotal.setText(amount);
	}

	@Override
	public void onRefreshData() {
		// TODO Auto-generated method stub
		super.onRefreshData();
		getCheckoutSummary();
	}

	@Override
	public void detachChildFragments() {
		removeChildFragment(R.id.frame_address);
		removeChildFragment(R.id.frame_payment_methods);
		removeChildFragment(R.id.frame_shipping_options);
	}

}
