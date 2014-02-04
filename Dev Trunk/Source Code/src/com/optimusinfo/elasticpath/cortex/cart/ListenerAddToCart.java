package com.optimusinfo.elasticpath.cortex.cart;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerAddToCart {

	void onTaskSuccessful(int responseCode);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
