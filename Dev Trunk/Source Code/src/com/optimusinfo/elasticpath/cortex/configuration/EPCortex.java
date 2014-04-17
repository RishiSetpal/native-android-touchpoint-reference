/*
 * Copyright © 2014 Elastic Path Software Inc. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
