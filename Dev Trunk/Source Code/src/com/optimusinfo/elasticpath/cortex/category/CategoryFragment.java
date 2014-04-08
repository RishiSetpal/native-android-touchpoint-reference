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
package com.optimusinfo.elasticpath.cortex.category;

import java.util.ArrayList;

import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.category.CategoryModel.CategoryElement;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductParentListingFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CategoryFragment extends EPFragment {

	public static String TAG_NAME = CategoryFragment.class.getSimpleName();

	private GridView mCategoriesGridView;

	private ListenerGetCategories mCortexListener;
	private OnItemClickListener mItemClickListener;

	private CategoriesAdapter mCategoriesAdapter;
	private ArrayList<CategoryElement> mListElements;

	public String mCategoryUrl;

	public CategoryFragment() {
		super();
		this.mCategoryUrl = null;
	}

	public CategoryFragment(String mCategoryUrl) {
		super();
		this.mCategoryUrl = mCategoryUrl;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the view
		View viewNavigation = inflater.inflate(R.layout.fragment_categories,
				container, false);

		if (viewNavigation != null) {
			mCategoriesGridView = (GridView) viewNavigation
					.findViewById(R.id.gvCategories);
		}

		if (mCategoriesAdapter == null) {
			// Instantiate the categories task
			getCatgeoriesFromAPI();
		} else {
			// Set the adapter
			mCategoriesGridView.setAdapter(mCategoriesAdapter);
			// Set View Properties
			setUpViewCategories();
		}
		return viewNavigation;
	}

	/**
	 * This method sets up the categories listener and executed the async
	 * asynchronous task
	 */
	protected void getCatgeoriesFromAPI() {

		mCortexListener = new ListenerGetCategories() {

			@Override
			public void onTaskSuccessful(Object dataNavigations) {
				mListElements = new ArrayList<CategoryElement>();
				if (mCategoryUrl != null) {
					mListElements.add((CategoryElement) dataNavigations);
				} else {
					for (CategoryElement element : ((CategoryModel) dataNavigations).element) {
						mListElements.add(element);
					}
				}
				// Set the adapter on main ui thread
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						if (getMainFragment() instanceof CategoryFragment) {
							mCategoriesAdapter = new CategoriesAdapter(
									getActivity(), mListElements);
							// Set the adapter
							mCategoriesGridView.setAdapter(mCategoriesAdapter);
							// Set View Properties
							setUpViewCategories();
						}
					}
				});
			}

			@Override
			public void onTaskFailed(final int errorCode) {
				// Set the adapter on main ui thread
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
				// Set the adapter on main ui thread
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// Show the authentication failed error
						NotificationUtils.showNotificationToast(
								getActivity(),
								getActivity().getString(
										R.string.msgErrorAuthentication));
					}
				});
			}
		};

		// Get config params
		EPCortex objCortexParams = getObjCortexParams();
		// Get access token
		String accessToken = getUserAuthenticationToken();

		getActivity().setProgressBarIndeterminateVisibility(true);
		if (mCategoryUrl != null) {
			// Get categories from server
			CategoryModel.getChildCategoriesFromServer(getActivity(),
					mCategoryUrl, accessToken, mCortexListener);
		} else {
			// Get categories from server
			CategoryModel.getCategoriesFromServer(getActivity(),
					objCortexParams.getEndpoint(),
					Constants.Routes.NAVIGATION_ROUTE,
					objCortexParams.getScope(),
					Constants.ZoomUrl.URL_ZOOM_NAVIGATIONS, accessToken,
					mCortexListener);
		}

	}

	/**
	 * This method sets up the categories grid view
	 */
	private void setUpViewCategories() {
		mItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> objView, View current,
					int position, long id) {
				// Get the items url
				String itemsUrl = CategoryModel.getItemsUrl(mListElements
						.get(position));

				String childUrl = CategoryModel
						.getChildCategoriesUrl(mListElements.get(position));
				String cateDesc = CategoryModel.getCategoryDesc(mListElements
						.get(position));

				if (!TextUtils.isEmpty(childUrl)) {
					CategoryFragment mObjFragment = new CategoryFragment(
							childUrl);
					addFragmentToBreadcrumb(mListElements.get(position)
							.getDisplayName(), R.id.fragment_container,
							mObjFragment);
				} else if (!TextUtils.isEmpty(itemsUrl)) {
					ProductParentListingFragment mObjFragment = new ProductParentListingFragment(
							itemsUrl, mListElements.get(position)
									.getDisplayName(), cateDesc);
					addFragmentToBreadcrumb(mListElements.get(position)
							.getDisplayName(), R.id.fragment_container,
							mObjFragment);
				}
			}
		};
		// Set the item click listeners
		mCategoriesGridView.setOnItemClickListener(mItemClickListener);
	}

	@Override
	public void onRefreshData() {
		getCatgeoriesFromAPI();
	}

	@Override
	public void detachChildFragments() {
		// No Child fragments added so empty
	}
}
