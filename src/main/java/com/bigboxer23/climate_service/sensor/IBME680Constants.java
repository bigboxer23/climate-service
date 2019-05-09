package com.bigboxer23.climate_service.sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface IBME680Constants
{
	/**
	 * Various sensors we get readings about
	 */
	List<String> kClimateItems = new ArrayList<String>()
	{{
		add("temperature");
		add("pressure");
		add("humidity");
		add("quality");
	}};

	Map<String, Float> kStepSensitivity = new HashMap<String, Float>()
	{{
		put("humidity", .5f);
		put("quality", 5f);
	}};
}
