package com.optimusinfo.elasticpath.cortex.configuration;

import java.io.IOException;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.optimusinfo.elasticpath.cortex.common.Utils;

/**
 * This is the Configuration Model class.
 * 
 * @author Optimus
 * 
 */
public class EPConfiguration {

	@SerializedName("cortexApi")
	private EPCortex mCortexParams;

	@SerializedName("logging")
	private EPLogging mLoggingParams;

	public EPCortex getCortexParams() {
		return this.mCortexParams;
	}

	public void setCortexParams(EPCortex mCortexParams) {
		this.mCortexParams = mCortexParams;
	}

	public EPLogging getLoggingParams() {
		return this.mLoggingParams;
	}

	public void setLoggingParams(EPLogging mLoggingParams) {
		this.mLoggingParams = mLoggingParams;
	}

	/**
	 * This function returns the configuration parameter object
	 * 
	 * @param configFileName
	 * @return
	 */
	public static EPCortex getConfigurationParameters(Context current,
			String configFilename) {
		String configData;
		try {
			configData = Utils.getStringFromInputStream(current.getAssets()
					.open(configFilename));
			EPConfiguration objConfiguration = new Gson().fromJson(configData,
					EPConfiguration.class);
			return objConfiguration.getCortexParams();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
