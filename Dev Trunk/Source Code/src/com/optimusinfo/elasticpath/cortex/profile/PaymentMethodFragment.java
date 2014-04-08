package com.optimusinfo.elasticpath.cortex.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PaymentElement;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PaymentInfo;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class PaymentMethodFragment extends EPFragment {

	private GridView mPaymentsGridView;
	private PaymentMethodAdapter mAdapter;
	private PaymentElement[] mElements;
	private EPFragment mParentFragment;

	// Constructor using fields
	public PaymentMethodFragment(EPFragment mParentFragment) {
		super();
		this.mParentFragment = mParentFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_profile_payment_methods, container, false);

		if (viewNavigation != null) {
			mPaymentsGridView = (GridView) viewNavigation
					.findViewById(R.id.gvPaymentMethods);
		}

		PaymentInfo[] mPaymentInfo = ((ProfileFragment) mParentFragment).mObjProfile.mPaymentMethods;

		if (mPaymentInfo != null) {
			mElements = mPaymentInfo[0].mElement;
		}
		if (mElements != null) {
			mAdapter = new PaymentMethodAdapter(getActivity(), mElements);
		}

		if (mAdapter != null) {
			mPaymentsGridView.setAdapter(mAdapter);
			setDynamicHeight();
		}

		return viewNavigation;
	}

	private void setDynamicHeight() {
		ViewGroup.LayoutParams params = mPaymentsGridView.getLayoutParams();
		int size = mAdapter.getCount();
		int dynamicHeight = Utils.getTotalHeightofListView(mPaymentsGridView);
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
