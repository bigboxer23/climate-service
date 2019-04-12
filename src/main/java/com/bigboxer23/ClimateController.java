package com.bigboxer23;

import com.bigboxer23.climate_service.sensor.BME680Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Web Service to return climate data
 */
@RestController
@EnableAutoConfiguration
public class ClimateController
{
	private static final Logger myLogger = LoggerFactory.getLogger(ClimateController.class);

	private BME680Controller myBME680Controller;

	@Autowired
	public void setBME680Controller(BME680Controller theBME680Controller)
	{
		myBME680Controller = theBME680Controller;
	}

	/**
	 * Get JSON climate data
	 *
	 * @return
	 */
	@GetMapping(path = "/climate", produces = "application/json;charset=UTF-8")
	public Map<String, Float> getClimateData()
	{
		myLogger.debug("Climate data requested");
		return myBME680Controller.getClimateData();
	}
}
