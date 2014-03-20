package com.optimusinfo.elasticpath.cortex.checkout;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.optimusinfo.elasticpath.cortex.cart.ListenerGetCompleteCartItems;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragmentActivity;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class CheckoutActivity extends EPFragmentActivity {

	protected String mCheckOutLink;
	protected CheckoutModel mObjCheckOut;

	protected ListenerCheckOutOrder mListener;
	protected ListenerGetCompleteCartItems mCartItemsListener;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set the content view
		setContentView(R.layout.activity_checkout);
		// Initialize the params objects
		super.initializeParams();
		// Disable the title
		mObjActionBar.setDisplayShowTitleEnabled(false);
		// Get the order details
		mCheckOutLink = getIntent().getStringExtra(
				Constants.PageUrl.INTENT_CHECKOUT);

		getCheckoutSummary();

	}

	public void getCheckoutSummary() {
		mListener = new ListenerCheckOutOrder() {

			@Override
			public void onTaskSuccessful(CheckoutModel response) {
				mObjCheckOut = response;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						setUpViews();
					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO - For Future Req
						NotificationUtils.showErrorToast(
								getApplicationContext(), errorCode);
					}
				});
			}

			@Override
			public void onAuthenticationFailed() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						setProgressBarIndeterminateVisibility(false);
						// TODO For Future Req
					}
				});
			}
		};

		setProgressBarIndeterminateVisibility(true);
		CheckoutModel.getCheckOutSummary(getApplicationContext(),
				mCheckOutLink.concat(Constants.ZoomUrl.URL_ZOOM_CHECKOUT),
				getUserAuthenticationToken(), mListener);

	}

	private void setUpViews() {

		RelativeLayout mLayout = (RelativeLayout) findViewById(R.id.rlCart);
		mLayout.setVisibility(View.VISIBLE);

		TextView tvQuant = (TextView) findViewById(R.id.tvQuantity);
		tvQuant.setText(mObjCheckOut.mCartTotal[0].mTotalQuantity.concat(" ")
				.concat(this.getString(R.string.prefixItems)));

		TextView tvOrderSubtotal = (TextView) findViewById(R.id.tvOrderSubtotal);
		tvOrderSubtotal
				.setText(mObjCheckOut.mCartTotal[0].mCartTotal[0].mCosts[0].mTotalCost);

		TextView tvShippingTotal = (TextView) findViewById(R.id.tvShippingTotal);
		tvShippingTotal.setText("N/A");

		TextView tvOrderTotal = (TextView) findViewById(R.id.tvOrderTotal);
		tvOrderTotal.setText(mObjCheckOut.mTotal[0].mCosts[0].mTotalCost);

		addFragment(R.id.frame_address, new AddressFragment());

	}

}
