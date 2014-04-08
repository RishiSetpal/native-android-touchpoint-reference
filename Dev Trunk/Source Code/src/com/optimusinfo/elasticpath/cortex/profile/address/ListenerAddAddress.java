package com.optimusinfo.elasticpath.cortex.profile.address;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerAddAddress {

	void onTaskSuccessful(int responseCode);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
