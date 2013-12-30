package com.optimusinfo.elasticpath.cortex.category;

/**
 * This is the listener for Categories asynchronous task
 * 
 * @author Optimus
 * 
 */
public interface ListenerGetCategories {
	void onTaskSuccessful(Object dataNavigations);

	void onTaskFailed(int errorCode);

	void onAuthenticationFailed();
}
