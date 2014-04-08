package com.optimusinfo.elasticpath.cortex.profile.address;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerUpdateAdddress {

	void onTaskSuccessful(int response);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
