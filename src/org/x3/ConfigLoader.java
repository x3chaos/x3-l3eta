package org.x3;

import java.io.File;
import java.util.HashMap;

public class ConfigLoader {
	//TODO
	private File config;
	private HashMap<String, Object> defaults;
	private HashMap<String, Object> configData;

	public ConfigLoader(File config) {
		this.setDefaults();
		configData = new HashMap<String, Object>();
		this.config = config;
		load();
	}

	private void setDefaults() {
		this.defaults = new HashMap<String, Object>();
		this.defaults.put("server.name",
				"default server name [Please edit config to change]");
	}

	public void load() {

	}

	public String getString(String string) {
		if (configData.containsKey(string)) {
			return configData.get(string).toString();
		}
		return defaults.get(string).toString();
	}

}
