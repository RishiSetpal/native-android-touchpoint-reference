package com.optimusinfo.elasticpath.cortex.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.PaymentMethodInfo;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.PaymentMethodInfo.PaymentSelector;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class PaymentMethodFragment extends EPFragment {

	private GridView mMethodGridView;
	private PaymentMethodsAdapter mAdapter;
	private PaymentSelector mElements;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_checkout_payment_methods, container, false);

		if (viewNavigation != null) {
			mMethodGridView = (GridView) viewNavigation
					.findViewById(R.id.gvPaymentMethods);
		}

		PaymentMethodInfo[] mPaymentInfo = ((CheckoutFragment) getMainFragment()).mObjCheckOut.mPaymentInfo;
		if (mPaymentInfo != null && mPaymentInfo[0].mSelector != null) {
			mElements = mPaymentInfo[0].mSelector[0];
		}

		if (mElements != null) {
			mAdapter = new PaymentMethodsAdapter(this, mElements);
		}

		if (mAdapter != null) {
			mMethodGridView.setAdapter(mAdapter);
			setDynamicHeight();
		}

		return viewNavigation;
	}

	private void setDynamicHeight() {
		ViewGroup.LayoutParams params = mMethodGridView.getLayoutParams();
		int size = mAdapter.getCount();
		int dynamicHeight = Utils.getTotalHeightofListView(mMethodGridView);
		if (size % 2 == 0) {
			params.height = dynamicHeight / 2;
		} else {
			params.height = (dynamicHeight / 2) + (dynamicHeight / size);
		}
	}

	@Override
	public void detachChildFragments() {
		// No child fragments added
	}
}
