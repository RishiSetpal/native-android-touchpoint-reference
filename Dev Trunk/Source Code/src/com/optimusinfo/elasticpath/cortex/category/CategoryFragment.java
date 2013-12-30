package com.optimusinfo.elasticpath.cortex.category;

import java.util.ArrayList;

import com.optimusinfo.elasticpath.cortex.EPHomeActivity;
import com.optimusinfo.elasticpath.cortex.R;
import com.optimusinfo.elasticpath.cortex.category.Category.CategoryElement;
import com.optimusinfo.elasticpath.cortex.category.Category.CategoryLink;
import com.optimusinfo.elasticpath.cortex.common.Constants;
import com.optimusinfo.elasticpath.cortex.common.NotificationUtils;
import com.optimusinfo.elasticpath.cortex.configuration.EPConfiguration;
import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortex.product.listing.ProductLisitngActivity;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CategoryFragment extends Fragment {

	// Variables
	private SharedPreferences objPreferences;
	private ListenerGetCategories mListener;

	private GridView mCategoriesGridView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// get the shared preferences
		objPreferences = ((EPHomeActivity) getActivity()).getObjPreferences();

		// Inflate the view
		View viewNavigation = inflater.inflate(R.layout.fragment_categories,
				container, false);
		if (viewNavigation != null) {
			mCategoriesGridView = (GridView) viewNavigation
					.findViewById(R.id.gvCategories);
			// Instantiate the categories task
			getCatgeoriesFromAPI();
		}
		return viewNavigation;
	}

	/**
	 * This method sets up the categories listener and executed the async
	 * asynchronous task
	 */
	protected void getCatgeoriesFromAPI() {
		mListener = new ListenerGetCategories() {

			@Override
			public void onTaskSuccessful(Object dataNavigations) {
				final ArrayList<CategoryElement> mListElements = new ArrayList<CategoryElement>();
				for (CategoryElement element : ((Category) dataNavigations).element) {
					mListElements.add(element);
				}
				// Set the adapter on main ui thread
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						getActivity().setProgressBarIndeterminateVisibility(
								false);
						// Set the adapter
						mCategoriesGridView.setAdapter(new CategoriesAdapter(
								getActivity(), mListElements));
						// Set the item click listeners
						mCategoriesGridView
								.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> objView,
											View current, int position, long id) {
										// Get the items url
										String itemsUrl = "";
										
										CategoryElement currElement = mListElements
												.get(position);
										
										if (currElement.getLinks() != null) {
											
											for (CategoryLink link : currElement
													.getLinks()) {
												
												if (link.getRelation() != null
														&& link.getRelation()
																.length() != 0) {
													
													if (0 == link.getRelation()
															.compareTo("items")) {
														
														itemsUrl = link
																.getItemsUrl();
													}
												}
											}
										}
										Intent intent = new Intent(
												getActivity(),
												ProductLisitngActivity.class);
										intent.putExtra(
												Constants.PageUrl.INTENT_BASE_URL,
												itemsUrl);										
										startActivity(intent);
									}
								});
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
		EPCortex objCortexParams = EPConfiguration.getConfigurationParameters(
				getActivity(), Constants.Config.FILE_NAME_CONFIG);

		// Get access token
		String accessToken = objPreferences.getString(
				Constants.Preferences.KEY_ACCESS_TOKEN, "");
		getActivity().setProgressBarIndeterminateVisibility(true);

		// Get categories from server
		Category.getCategoriesFromServer(getActivity(),
				objCortexParams.getEndpoint(),
				Constants.Routes.NAVIGATION_ROUTE, objCortexParams.getScope(),
				Constants.ZoomUrl.URL_ZOOM_NAVIGATIONS, accessToken, mListener);

	}
}
