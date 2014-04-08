package com.optimusinfo.elasticpath.cortex.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.AddressElement;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.AddressInfo;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class AddressFragment extends EPFragment {

	private GridView mAddressGridView;
	private AddressAdapter mAdapter;
	private AddressElement[] mElements;
	private EPFragment mParentFragment;

	// Constructor using fields
	public AddressFragment(EPFragment mParentFragment) {
		super();
		this.mParentFragment = mParentFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_profile_address, container, false);

		if (viewNavigation != null) {
			mAddressGridView = (GridView) viewNavigation
					.findViewById(R.id.gvAddress);
		}

		AddressInfo[] mAddressInfo = ((ProfileFragment) mParentFragment).mObjProfile.mAddresses;

		if (mAddressInfo != null) {
			mElements = mAddressInfo[0].mElements;
		}
		if (mElements != null) {
			mAdapter = new AddressAdapter(this, mElements);
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
			params.height = (dynamicHeight / 2) + 20;
		} else {
			params.height = (dynamicHeight / 2) + (dynamicHeight / size);
		}

		mAddressGridView.setLayoutParams(params);
		mAddressGridView.requestLayout();
	}

	@Override
	public void detachChildFragments() {
		// No child fragments added
	}
}
