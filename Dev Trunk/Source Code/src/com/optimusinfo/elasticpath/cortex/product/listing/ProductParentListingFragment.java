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
package com.optimusinfo.elasticpath.cortex.product.listing;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class ProductParentListingFragment extends EPFragment {

	public static String TAG_NAME = ProductParentListingFragment.class
			.getSimpleName();

	protected String mProductBaseUrl, mCatHeader, mCatDesc;

	protected int mCurrentPageNumber;

	private ListenerGetProductListings mListenerGetProductListings;
	private ProductListing mObjProducts;

	private PullToRefreshGridView mPullRefreshGridView;
	private GridView mProductListGridView;
	private ProductListingAdapter mProductsAdapter;

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
		mCurrentPageNumber = 1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewNavigation = inflater.inflate(
				R.layout.activity_product_listing, container, false);
		if (viewNavigation != null) {
			setUpViews(viewNavigation);
		}

		// Check if model objects is null
		if (mObjProducts == null || mObjProducts.getElements() == null) {
			getProductListing();
		} else {
			showProductList();
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
						mPullRefreshGridView.onRefreshComplete();
						if (getMainFragment() instanceof ProductParentListingFragment) {
							if (mObjProducts.getElements() != null) {
								showProductList();
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
				mProductBaseUrl, Constants.PageUrl.pageUrl,
				String.valueOf(mCurrentPageNumber),
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
		mPullRefreshGridView = (PullToRefreshGridView) container
				.findViewById(R.id.gvProducts);

		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						onPullDown();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						onPullUp();
					}

				});
		mProductListGridView = mPullRefreshGridView.getRefreshableView();
	}

	public void showProductList() {
		if (mObjProducts != null) {
			int currPage = Integer
					.parseInt(mObjProducts.Pagination.CurrentPage);
			int totalPages = Integer
					.parseInt(mObjProducts.Pagination.TotalPages);
			if (currPage >= totalPages && currPage == 1) {
				mPullRefreshGridView.setMode(Mode.DISABLED);
			} else if (currPage < totalPages && currPage > 1) {
				mPullRefreshGridView.setMode(Mode.BOTH);
			} else if (currPage < totalPages && currPage == 1) {
				mPullRefreshGridView.setMode(Mode.PULL_FROM_END);
			} else {
				mPullRefreshGridView.setMode(Mode.PULL_FROM_START);
			}

			mProductsAdapter = new ProductListingAdapter(this, mObjProducts);
			// Set the adapter
			mProductListGridView.setAdapter(mProductsAdapter);
		}
	}

	/**
	 * This method is exceuted when pull down is called
	 */
	public void onPullDown() {
		mCurrentPageNumber--;
		onRefreshData();
	}

	/**
	 * This method is exceuted when pull up is called
	 */
	public void onPullUp() {
		mCurrentPageNumber++;
		onRefreshData();
	}

	@Override
	public void onRefreshData() {
		getProductListing();
	}

	@Override
	public void detachChildFragments() {

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAuthenticationSucessful() {
		getProductListing();
	}

}
