package com.optimusinfo.elasticpath.cortex.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.checkout.CheckoutModel.AddressSelector;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class AddressFragment extends EPFragment {

	private GridView mAddressGridView;
	private AddressAdapter mAdapter;
	private AddressSelector mElements;

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

		if (mElements == null) {
			mElements = ((CheckoutActivity) getActivity()).mObjCheckOut.mBillingAddressInfo[0].mSelector[0];
		}

		if (mAdapter == null) {
			mAdapter = new AddressAdapter(getActivity(), mElements);
		}

		mAddressGridView.setAdapter(mAdapter);
		setDynamicHeight();
		return viewNavigation;
	}

	private void setDynamicHeight() {
		ViewGroup.LayoutParams params = mAddressGridView.getLayoutParams();
		int size = mAdapter.getCount();
		int dynamicHeight = Utils.getTotalHeightofListView(mAddressGridView);
		if (size % 2 == 0) {
			params.height = dynamicHeight / 2;
		} else {
			params.height = (dynamicHeight / 2) + (dynamicHeight / size);
		}
	}
}
