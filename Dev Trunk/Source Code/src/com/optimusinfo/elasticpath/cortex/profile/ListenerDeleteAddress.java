package com.optimusinfo.elasticpath.cortex.profile;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerDeleteAddress {

	void onTaskSuccessful(int response);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
