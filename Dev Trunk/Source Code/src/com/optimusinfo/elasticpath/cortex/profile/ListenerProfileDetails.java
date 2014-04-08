package com.optimusinfo.elasticpath.cortex.profile;

/**
 * This is the listener for Order Details task
 * 
 * @author Optimus
 * 
 */
public interface ListenerProfileDetails {

	void onTaskSuccessful(ProfileModel response);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
