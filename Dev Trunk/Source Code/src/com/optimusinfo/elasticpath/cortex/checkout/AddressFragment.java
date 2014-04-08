package com.optimusinfo.elasticpath.cortex.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressInfo;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressSelector;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries;
import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.Deliveries.DeliveriesElement;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class AddressFragment extends EPFragment {

	private GridView mAddressGridView;
	private AddressAdapter mAdapter;
	private AddressSelector mBillingElements, mShippingElements;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_checkout_address, container, false);

		if (viewNavigation != null) {
			mAddressGridView = (GridView) viewNavigation
					.findViewById(R.id.gvAddress);
		}

		AddressInfo[] mBillingAddressInfo = ((CheckoutFragment) getMainFragment()).mObjCheckOut.mBillingAddressInfo;

		if (mBillingAddressInfo != null
				&& mBillingAddressInfo[0].mSelector != null) {
			mBillingElements = mBillingAddressInfo[0].mSelector[0];
		}

		Deliveries[] mDeliveries = ((CheckoutFragment) getMainFragment()).mObjCheckOut.mDeliveries;

		if (mDeliveries != null) {
			DeliveriesElement[] mElement = mDeliveries[0].mElement;
			if (mElement != null) {
				AddressInfo[] mDestinationInfo = mElement[0].mDestinationInfo;
				if (mDestinationInfo != null) {
					mShippingElements = mDestinationInfo[0].mSelector[0];
				}
			}
		}

		if (mBillingElements != null) {
			mAdapter = new AddressAdapter(this, mBillingElements,
					mShippingElements);
		}
		if (mAdapter != null) {
			mAddressGridView.setAdapter(mAdapter);
			setDynamicHeight();
		}

		return viewNavigation;
	}

	private void setDynamicHeight() {
		ViewGroup.LayoutParams params = mAddressGridView.getLayoutParams();
		int size = mAdapter.getCount();
		int dynamicHeight = Utils.getTotalHeightofListView(mAddressGridView);
		if (size % 2 == 0) {
			params.height = (dynamicHeight / 2) + 50;
		} else {
			params.height = (dynamicHeight / 2) + (dynamicHeight / size);
		}

		mAddressGridView.setLayoutParams(params);
		mAddressGridView.requestLayout();
	}

	@Override
	public void detachChildFragments() {
		// No Child fragments added
	}
}
