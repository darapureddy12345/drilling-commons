package com.bh.drillingcommons.enumerators;

public class Service {
	private Service() {
	}

	public enum GE {
		
		ENGAGE_DRILLING  ("host.services.engagedrilling"),
        SERVICE_NOW_BASE_URL  ("host.services.servicenow");

		private String appKey;

		GE(String appKey) {
			this.appKey = appKey;
		}

		/**
		 * @return the Application Property Key
		 */
		public String getAppKey() {
			return this.appKey;
		}
	}
}
