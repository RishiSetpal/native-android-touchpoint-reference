package com.optimusinfo.elasticpath.cortex.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.DeliveriesElement;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.ShippingOptionInfo;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.ShippingOptionSelector;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class ShippingOptionsFragment extends EPFragment {

	private GridView mMethodGridView;
	private ShippingOptionsAdapter mAdapter;
	private ShippingOptionSelector mElements;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_checkout_shipping_options, container, false);

		if (viewNavigation != null) {
			mMethodGridView = (GridView) viewNavigation
					.findViewById(R.id.gvShippingOptions);
		}

		Deliveries[] mDeliveries = ((CheckoutFragment) getMainFragment()).mObjCheckOut.mDeliveries;
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
			mMethodGridView.setAdapter(mAdapter);
			setDynamicHeight();
		}

		return viewNavigation;
	}

	private void setDynamicHeight() {
		ViewGroup.LayoutParams params = mMethodGridView.getLayoutParams();
		int size = mAdapter.getCount();
		int dynamicHeight = Utils.getTotalHeightofListView(mMethodGridView);
		params.height = (dynamicHeight) + (dynamicHeight / size);

		mMethodGridView.setLayoutParams(params);
	}

	@Override
	public void detachChildFragments() {
		// No child fragments added		
	}
}
