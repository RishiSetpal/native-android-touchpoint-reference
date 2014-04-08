package com.optimusinfo.elasticpath.cortex.product.listing;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductParentListingFragment extends EPFragment {

	public static String TAG_NAME = ProductParentListingFragment.class
			.getSimpleName();
	protected String mProductBaseUrl, mCatHeader, mCatDesc;

	private ListenerGetProductListings mListenerGetProductListings;
	private ProductListing mObjProducts;

	/**
	 * Constructor to initialize the product listing fragment
	 * 
	 * @param mProductBaseUrl
	 * @param mCatHeader
	 * @param mCatDesc
	 */
	public ProductParentListingFragment(String mProductBaseUrl,
			String mCatHeader, String mCatDesc) {
		super();
		this.mProductBaseUrl = mProductBaseUrl;
		this.mCatHeader = mCatHeader;
		this.mCatDesc = mCatDesc;
	}

	protected ProductListFragment mObjFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewNavigation = inflater.inflate(
				R.layout.activity_product_listing, container, false);
		if (viewNavigation != null) {
			setUpViews(viewNavigation);
		}

		mObjFragment = (ProductListFragment) getFragmentManager()
				.findFragmentByTag(mProductBaseUrl);

		// Check if model objects is null
		if (mObjProducts == null || mObjProducts.getElements() == null) {
			getProductListing();
		} else {
			mObjFragment = new ProductListFragment(mProductBaseUrl,
					mObjProducts);
			addChildFragment(mProductBaseUrl, R.id.fragment_child_container,
					(EPFragment) mObjFragment);
		}
		return viewNavigation;
	}

	/**
	 * This method gets the product listing.
	 */
	public void getProductListing() {
		mListenerGetProductListings = new ListenerGetProductListings() {

			@Override
			public void onTaskSuccessful(final Object dataNavigations) {
				mObjProducts = (ProductListing) dataNavigations;
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						if (getMainFragment() instanceof ProductParentListingFragment) {
							if (mObjProducts.getElements() != null) {
								// TODO
								mObjFragment = new ProductListFragment(
										mProductBaseUrl, mObjProducts);
								addChildFragment(mProductBaseUrl,
										R.id.fragment_child_container,
										(EPFragment) mObjFragment);
							} else {
								NotificationUtils.showNotificationToast(
										getActivity(),
										getActivity().getString(
												R.string.msgNoProductsPresent));
							}
						}
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
					}
				});
			}
		};
		getActivity().setProgressBarIndeterminateVisibility(true);
		ProductListing.getProuctListingFromServer(getActivity(),
				mProductBaseUrl, Constants.PageUrl.pageUrl, "1",
				Constants.ZoomUrl.URL_ZOOM_PRODUCT_LISTING,
				getUserAuthenticationToken(), mListenerGetProductListings);

	}

	/**
	 * This method initializes the views
	 * 
	 * @param container
	 */
	private void setUpViews(View container) {
		((TextView) container.findViewById(R.id.tvProductListingCategoryTitle))
				.setText(mCatHeader);
		((TextView) container.findViewById(R.id.tvProductListingCategoryDesc))
				.setText(mCatDesc);
	}

	@Override
	public void onRefreshData() {
		// TODO Auto-generated method stub
		super.onRefreshData();
		getProductListing();
	}

	@Override
	public void detachChildFragments() {		
		removeChildFragment(R.id.fragment_child_container);
	}

}
