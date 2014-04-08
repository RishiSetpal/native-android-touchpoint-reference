package com.optimusinfo.elasticpath.cortex.profile;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	protected RelativeLayout mLayout;
	protected boolean mIsOrderConfirmed = false;
	protected View mViewParent;

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

		addChildFragment("Address", R.id.frame_address,
				(EPFragment) new AddressFragment(this));
		addChildFragment("Address", R.id.frame_payment_methods,
				new PaymentMethodFragment(this));
		addChildFragment("Address", R.id.frame_purchases,
				new PurchaseHistoryFragment(this));

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onRefreshData() {
		super.onRefreshData();
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
		removeChildFragment(R.id.frame_address);
		removeChildFragment(R.id.frame_payment_methods);
		removeChildFragment(R.id.frame_purchases);
	}
}
