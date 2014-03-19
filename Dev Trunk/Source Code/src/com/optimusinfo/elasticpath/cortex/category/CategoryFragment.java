package com.optimusinfo.elasticpath.cortex.category;

import java.util.ArrayList;

import com.optimusinfo.elasticpath.cortex.EPHomeActivity;
import com.optimusinfo.elasticpath.cortexAPI.R;
import com.optimusinfo.elasticpath.cortex.category.Category.CategoryElement;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.EPFragment;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductLisitngActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CategoryFragment extends EPFragment {
	private GridView mCategoriesGridView;

	private ListenerGetCategories mCortexListener;
	private OnItemClickListener mItemClickListener;

	private CategoriesAdapter mCategoriesAdapter;
	private ArrayList<CategoryElement> mListElements;

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
				for (CategoryElement element : ((Category) dataNavigations).element) {
					mListElements.add(element);
				}
				// Set the adapter on main ui thread
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						mCategoriesAdapter = new CategoriesAdapter(
								getActivity(), mListElements);
						// Set the adapter
						mCategoriesGridView.setAdapter(mCategoriesAdapter);
						// Set View Properties
						setUpViewCategories();
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
		EPCortex objCortexParams = ((EPHomeActivity) getActivity())
				.getObjCortexParams();
		// Get access token
		String accessToken = ((EPHomeActivity) getActivity())
				.getUserAuthenticationToken();

		getActivity().setProgressBarIndeterminateVisibility(true);
		// Get categories from server
		Category.getCategoriesFromServer(getActivity(),
				objCortexParams.getEndpoint(),
				Constants.Routes.NAVIGATION_ROUTE, objCortexParams.getScope(),
				Constants.ZoomUrl.URL_ZOOM_NAVIGATIONS, accessToken,
				mCortexListener);

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
				String itemsUrl = Category.getItemsUrl(mListElements
						.get(position));
				if (!TextUtils.isEmpty(itemsUrl)) {
					Intent intent = new Intent(getActivity(),
							ProductLisitngActivity.class);
					intent.putExtra(Constants.PageUrl.INTENT_BASE_URL, itemsUrl);
					intent.putExtra(Constants.Content.INTENT_CAT_HEADER,
							mListElements.get(position).getDisplayName());
					intent.putExtra(Constants.Content.INTENT_CAT_DESC,
							mListElements.get(position).getDetails()[0]
									.getItemDesc());
					startActivity(intent);
				} else {
					NotificationUtils.showNotificationToast(getActivity(),
							"Item Url- " + itemsUrl);
				}
			}
		};
		// Set the item click listeners
		mCategoriesGridView.setOnItemClickListener(mItemClickListener);
	}

}
