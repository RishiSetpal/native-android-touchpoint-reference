package com.optimusinfo.elasticpath.cortex.configuration;

import com.google.gson.annotations.SerializedName;

/**
 * This class serves as the model class for Cortex API Related configuration
 * parameters.
 * 
 * @author Optimus
 * 
 */
public class EPCortex {
	@SerializedName("path")
	private String mPath;

	@SerializedName("scope")
	private String mScope;

	@SerializedName("endpoint")
	private String mEndpoint;

	@SerializedName("role")
	private String mRole;

	public String getEndpoint() {
		return mEndpoint;
	}

	public void setEndpoint(String mEndpoint) {
		this.mEndpoint = mEndpoint;
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String mPath) {
		this.mPath = mPath;
	}

	public String getScope() {
		return mScope;
	}

	public void setScope(String mScope) {
		this.mScope = mScope;
	}

	public String getRole() {
		return mRole;
	}

	public void setRole(String mRole) {
		this.mRole = mRole;
	}
}
