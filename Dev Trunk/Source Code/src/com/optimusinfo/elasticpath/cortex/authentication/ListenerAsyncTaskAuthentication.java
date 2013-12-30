package com.optimusinfo.elasticpath.cortex.authentication;

import com.optimusinfo.elasticpath.cortex.authentication.Authentication;

/**
 * This listener executes when authentication is in progress
 * 
 * @author Optimus
 * 
 */
public interface ListenerAsyncTaskAuthentication {
	void onTaskComplete(Authentication authenticationResponse);

	void onTaskFailed(int errorCode);
}
