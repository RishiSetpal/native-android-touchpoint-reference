package com.optimusinfo.elasticpath.cortex.product.details;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerGetProductDetails {
	void onTaskSuccessful(Object dataNavigations);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
