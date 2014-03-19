package com.optimusinfo.elasticpath.cortex.product.listing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortexAPI.R;

public class ProductListFragment extends EPFragment {

	private GridView mProductListGridView;
	private ListenerGetProductListings mListenerGetProductListings;

	private ProductListingAdapter mProductsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(
				R.layout.fragment_product_listing, container, false);
		if (viewNavigation != null) {
			mProductListGridView = (GridView) viewNavigation
					.findViewById(R.id.gvProducts);
		}

		if (mProductsAdapter == null) {
			// Instantiate the categories task
			getProductListing();
		} else {
			// Set the adapter
			mProductListGridView.setAdapter(mProductsAdapter);
			// Set View Properties

		}

		return viewNavigation;
	}

	public void getProductListing() {
		mListenerGetProductListings = new ListenerGetProductListings() {

			@Override
			public void onTaskSuccessful(final Object dataNavigations) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// Create an adapter that when requested, will return a
						// fragment
						// representing an object in
						// the collection.
						if (((ProductListing) dataNavigations).getElements() != null) {
							mProductsAdapter = new ProductListingAdapter(
									getActivity(),
									(ProductListing) dataNavigations);
							mProductListGridView.setAdapter(mProductsAdapter);
						} else {
							NotificationUtils.showNotificationToast(
									getActivity(),
									getActivity().getString(
											R.string.msgNoProductsPresent));
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
						// TODO - For Future Req
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
		ProductListing.getProuctListingFromServer(getActivity(),
				((ProductLisitngActivity) getActivity()).mProductBaseUrl,
				Constants.PageUrl.pageUrl, "1",
				Constants.ZoomUrl.URL_ZOOM_PRODUCT_LISTING,
				((ProductLisitngActivity) getActivity())
						.getUserAuthenticationToken(),
				mListenerGetProductListings);

	}
}
