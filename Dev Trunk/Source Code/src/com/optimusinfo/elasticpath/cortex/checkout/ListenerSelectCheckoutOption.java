package com.optimusinfo.elasticpath.cortex.checkout;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerSelectCheckoutOption {

	void onTaskSuccessful(int responseCode);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
