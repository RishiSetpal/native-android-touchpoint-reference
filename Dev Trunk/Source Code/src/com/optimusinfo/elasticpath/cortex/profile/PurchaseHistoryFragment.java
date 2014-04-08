package com.optimusinfo.elasticpath.cortex.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PurchaseElement;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PurchaseInfo;
import com.optimusinfo.elasticpath.cortex.purchase.PurchaseFragment;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class PurchaseHistoryFragment extends EPFragment {

	private ListView mPurchaseListView;
	private PurchaseAdapter mAdapter;
	private PurchaseElement[] mElements;
	private EPFragment mParentFragment;

	// Constructor using fields
	public PurchaseHistoryFragment(EPFragment mParentFragment) {
		super();
		this.mParentFragment = mParentFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_profile_purchase_history, container, false);

		if (viewNavigation != null) {
			mPurchaseListView = (ListView) viewNavigation
					.findViewById(R.id.lvPurchaseHistory);
		}

		PurchaseInfo[] mPurchaseInfo = ((ProfileFragment) mParentFragment).mObjProfile.mPurchases;

		if (mPurchaseInfo != null) {
			mElements = mPurchaseInfo[0].mElements;
		}
		if (mElements != null) {
			mAdapter = new PurchaseAdapter(getActivity(), mElements);
		}

		if (mAdapter != null) {
			mPurchaseListView.setAdapter(mAdapter);
			mPurchaseListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {
					String orderUrl = mElements[pos].mLink.mHREF;
					getMainFragment().detachChildFragments();
					PurchaseFragment mObjFragment = new PurchaseFragment(null,
							orderUrl);
					addFragmentToBreadcrumb("Order", R.id.fragment_container,
							mObjFragment);
				}
			});
		}
		Utils.setListViewHeightBasedOnChildren(mPurchaseListView);
		return viewNavigation;
	}

	@Override
	public void detachChildFragments() {
		// No child fragments added		
	}
}
