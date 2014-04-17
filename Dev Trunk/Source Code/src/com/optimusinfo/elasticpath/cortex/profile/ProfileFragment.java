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
package com.optimusinfo.elasticpath.cortex.profile;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.common.Utils;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.AddressElement;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.AddressInfo;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PaymentElement;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PaymentInfo;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PurchaseElement;
import com.optimusinfo.elasticpath.cortex.profile.ProfileModel.PurchaseInfo;
import com.optimusinfo.elasticpath.cortex.purchase.PurchaseFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProfileFragment extends EPFragment {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments representing each object in a collection. We use a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter} derivative,
	 * which will destroy and re-create fragments as needed, saving and
	 * restoring their state in the process. This is important to conserve
	 * memory and is a best practice when allowing navigation between objects in
	 * a potentially large collection.
	 */
	protected String mProfileUrl;

	protected ListenerProfileDetails mProfileDetailsListener;
	protected ListenerDeleteAddress mDeleteAddressListner;

	protected ProfileModel mObjProfile;
	protected boolean mIsOrderConfirmed = false;

	protected RelativeLayout mLayout;
	protected View mViewParent;
	private GridView mAddressGridView, mPaymentsGridView;
	private ListView mPurchaseListView;

	private PurchaseElement[] mPurchaseElements;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewNavigation = inflater.inflate(R.layout.activity_profile,
				container, false);
		mViewParent = viewNavigation;
		// Get the profile url
		mProfileUrl = getProfileUrl();
		// Initialize views
		initializeViews();
		if (mObjProfile == null) {
			getProfileDetails(mProfileUrl);
		} else {
			setUpViews();
		}

		return viewNavigation;
	}

	/**
	 * This method initializes the page view elements
	 */
	private void initializeViews() {
		mLayout = (RelativeLayout) mViewParent.findViewById(R.id.rLayout);
		mAddressGridView = (GridView) mViewParent.findViewById(R.id.gvAddress);
		mPaymentsGridView = (GridView) mViewParent
				.findViewById(R.id.gvPaymentMethods);
		mPurchaseListView = (ListView) mViewParent
				.findViewById(R.id.lvPurchaseHistory);
	}

	/**
	 * This method gets the profile details
	 * 
	 * @param urlProfile
	 */
	public void getProfileDetails(String urlProfile) {

		mProfileDetailsListener = new ListenerProfileDetails() {
			@Override
			public void onTaskSuccessful(ProfileModel response) {
				mObjProfile = response;
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
						NotificationUtils.showErrorToast(getActivity(),
								Constants.ErrorCodes.ERROR_SERVER);
					}
				});
			}
		};

		getActivity().setProgressBarIndeterminateVisibility(true);
		mLayout.setVisibility(View.INVISIBLE);
		if (!TextUtils.isEmpty(urlProfile)) {
			ProfileModel.getProfileDetails(getActivity(), urlProfile,
					getUserAuthenticationToken(), mProfileDetailsListener);
		}
	}

	/**
	 * This method delets the address
	 * 
	 * @param deleteAddressLink
	 */
	public void deleteAddress(String deleteAddressLink) {
		mDeleteAddressListner = new ListenerDeleteAddress() {
			@Override
			public void onTaskSuccessful(final int response) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						if (response == Constants.ApiResponseCode.REQUEST_SUCCESSFUL_DELETED) {
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
		ProfileModel.deleteAddress(getActivity(), deleteAddressLink,
				getUserAuthenticationToken(), mDeleteAddressListner);

	}

	public void setUpViews() {
		mLayout.setVisibility(View.VISIBLE);

		TextView tvUserName = (TextView) mViewParent
				.findViewById(R.id.tvUserName);
		tvUserName.setText(mObjProfile.mGivenName.concat(" ").concat(
				mObjProfile.mFamilyName));

		TextView tvUserFullName = (TextView) mViewParent
				.findViewById(R.id.tvUserFullName);
		tvUserFullName.setText(mObjProfile.mFamilyName.concat(", ").concat(
				mObjProfile.mGivenName));

		TextView tvEmail = (TextView) mViewParent.findViewById(R.id.tvEmail);
		tvEmail.setText(getUserName());

		TextView tvContinueShopping = (TextView) mViewParent
				.findViewById(R.id.tvContinueShopping);
		tvContinueShopping.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
			}
		});
		showAddressGridView();
		showPaymentMethodsGridView();
		showPurchaseHistory();
	}

	/**
	 * This method shows the address grid view
	 */
	public void showAddressGridView() {
		AddressInfo[] mAddressInfo = mObjProfile.mAddresses;
		AddressElement[] mAddressElements = null;
		AddressAdapter mAddressAdapter;

		if (mAddressInfo != null) {
			mAddressElements = mAddressInfo[0].mElements;
		}

		if (mAddressElements != null) {
			mAddressAdapter = new AddressAdapter(this, mAddressElements);
		} else {
			mAddressAdapter = new AddressAdapter(this, null);
		}
		if (mAddressAdapter != null) {
			mAddressGridView.setAdapter(mAddressAdapter);
			Utils.setGridViewHeightBasedOnChildren(mAddressGridView,
					getIntegerResource(R.integer.numColumns));
		}
	}

	/**
	 * This method shows the payment methods grid view
	 */
	public void showPaymentMethodsGridView() {
		PaymentInfo[] mPaymentInfo = mObjProfile.mPaymentMethods;
		PaymentElement[] mPaymentElements = null;
		PaymentMethodAdapter mAdapter = null;
		if (mPaymentInfo != null) {
			mPaymentElements = mPaymentInfo[0].mElement;
		}
		if (mPaymentElements != null) {
			mAdapter = new PaymentMethodAdapter(getActivity(), mPaymentElements);
		}
		View noMethodView = getActivity().getLayoutInflater().inflate(
				R.layout.view_no_payment_method, null);
		
		if (mAdapter != null) {
			noMethodView.setVisibility(View.GONE);
			mPaymentsGridView.setAdapter(mAdapter);
			Utils.setGridViewHeightBasedOnChildren(mPaymentsGridView,
					getIntegerResource(R.integer.numColumns));
		} else {
			noMethodView.setVisibility(View.VISIBLE);
			((ViewGroup)mPaymentsGridView.getParent()).addView(noMethodView);
		}
	}

	public void showPurchaseHistory() {
		PurchaseInfo[] mPurchaseInfo = mObjProfile.mPurchases;
		PurchaseAdapter mAdapter = null;

		if (mPurchaseInfo != null) {
			mPurchaseElements = mPurchaseInfo[0].mElements;
		}

		if (mPurchaseElements != null) {
			mAdapter = new PurchaseAdapter(getActivity(), mPurchaseElements);
		}

		if (mAdapter != null) {
			mPurchaseListView.setAdapter(mAdapter);
			Utils.setListViewHeightBasedOnChildren(mPurchaseListView);
			mPurchaseListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long id) {
					String orderUrl = mPurchaseElements[pos].mLink.mHREF;
					String header = "Order "
							.concat(mPurchaseElements[pos].mOrderNumber);
					getMainFragment().detachChildFragments();
					PurchaseFragment mObjFragment = new PurchaseFragment(null,
							orderUrl, getActivity().getString(
									R.string.labelTextViewReturnProfile));
					addFragmentToBreadcrumb(header, R.id.fragment_container,
							mObjFragment);
				}
			});
		}

	}

	@Override
	public void onBackPressed() {
	}

	@Override
	public void onRefreshData() {
		getProfileDetails(mProfileUrl);
	}

	public String getProfileUrl() {
		String scope = getObjCortexParams().getScope();
		String url = getObjCortexParams().getEndpoint().concat("profiles/")
				.concat(scope).concat(Constants.ZoomUrl.URL_ZOOM_PROFILE);
		return url;
	}

	@Override
	public void detachChildFragments() {

	}

	@Override
	public void onAuthenticationSucessful() {
		// TODO
	}

}
