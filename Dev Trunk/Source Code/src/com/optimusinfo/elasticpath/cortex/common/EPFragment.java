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
	 * Skeleton method to fire the refresh data call
	 */
	public void onRefreshData() {

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
	 * Abstract Method to remove child fragments if any
	 */
	public abstract void detachChildFragments();

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

	/**
	 * Called when back is pressed
	 */
	public void onBackPressed() {	
		
	}
}
