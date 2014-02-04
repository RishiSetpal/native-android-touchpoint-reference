package com.optimusinfo.elasticpath.cortex.purchase;

/**
 * This is the listener for Product Listing asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerGetPurchaseForm {

	void onTaskSuccessful(PurchaseFormModel response);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
