package com.optimusinfo.elasticpath.cortex.product.listing;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerGetProductListings {
	void onTaskSuccessful(Object dataNavigations);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
