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
package com.optimusinfo.elasticpath.cortex.common;

import com.optimusinfo.elasticpath.cortex.configuration.EPCortex;
import com.optimusinfo.elasticpath.cortexAPI.R;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;

/**
 * This Fragment manages a single background task and retains itself across
 * configuration changes.
 */
public abstract class EPFragment extends Fragment {

	/**
	 * Hold a reference to the parent Activity so we can report the task's
	 * current progress and results. The Android framework will pass us a
	 * reference to the newly created Activity after each configuration change.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/**
	 * This method will only be called once when the retained Fragment is first
	 * created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}

	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * This method returns the authentication token
	 * 
	 * @return
	 */
	public String getUserAuthenticationToken() {
		return ((EPFragmentActivity) getActivity())
				.getUserAuthenticationToken();
	}

	/**
	 * This method returns the current user name
	 * 
	 * @return
	 */
	public String getUserName() {
		return ((EPFragmentActivity) getActivity()).getUserName();
	}

	/**
	 * This method tells whether user is logged in or not
	 * 
	 * @return
	 */
	public boolean isUserLoggedIn() {
		return ((EPFragmentActivity) getActivity()).isUserLoggedIn();
	}

	public EPCortex getObjCortexParams() {
		return ((EPFragmentActivity) getActivity()).getObjCortexParams();
	}

	/**
	 * This method adds a child breadcrumb
	 * 
	 * @param title
	 * @param fargmentContainerId
	 * @param objFragment
	 */
	public void addChildFragment(String title, int fargmentContainerId,
			EPFragment objFragment) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(fargmentContainerId, objFragment, title);
		ft.commit();
	}

	/*
	 * This method removes a child fragment
	 * 
	 * @param title
	 * 
	 * @param fargmentContainerId
	 * 
	 * @param objFragment
	 */
	public void removeChildFragment(int fargmentContainerId) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment frgment = getFragmentManager().findFragmentById(
				fargmentContainerId);
		if (frgment != null) {
			ft.remove(frgment);
			ft.commit();
		}
	}

	/**
	 * This method adds a prent breadcrumb
	 * 
	 * @param title
	 * @param isNew
	 * @param fargmentContainerId
	 * @param objFragment
	 */
	public void addFragmentToBreadcrumb(String title, int fargmentContainerId,
			EPFragment objFragment) {
		((EPFragmentActivity) getActivity()).addFragmentToBreadCrumb(title,
				fargmentContainerId, objFragment);
	}

	public EPFragment getMainFragment() {
		EPFragment mCurrentFrgament = (EPFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_container);
		return mCurrentFrgament;
	}

	public int getIntegerResource(int resId) {
		return getResources().getInteger(resId);
	}

	/**
	 * Called when back is pressed
	 */
	public abstract void onBackPressed();

	/**
	 * Skeleton method to fire the refresh data call
	 */
	public abstract void onRefreshData();

	/**
	 * Abstract Method to remove child fragments if any
	 */
	public abstract void detachChildFragments();

	/**
	 * Abstract Method to perform action when authentication is sucessful
	 */
	public abstract void onAuthenticationSucessful();

}
