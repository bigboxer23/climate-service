package com.bigboxer23;

import com.bigboxer23.climate_service.sensor.BME680Controller;
import com.bigboxer23.util.http.HttpClientUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Web Service to return climate data
 */
@RestController
@EnableAutoConfiguration
public class ClimateController
{
	private static final Logger myLogger = LoggerFactory.getLogger(ClimateController.class);

	/**
	 * Location of OpenHAB
	 */
	@Value("${openHABUrl}")
	private String kOpenHABUrl;

	@Value("${sensorName}")
	private String kSensorName;

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

	@Scheduled(fixedDelay = 30000)
	private void sendClimateData()
	{
		if (kOpenHABUrl == null || kSensorName == null)
		{
			return;
		}
		myBME680Controller.getClimateData().forEach((theData, theValue) ->
		{
			HttpPost aHttpPost = new HttpPost(kOpenHABUrl + "/rest/items/" + kSensorName + capitalizeFirstLetter(theData));
			try
			{
				aHttpPost.setEntity(new ByteArrayEntity(URLDecoder.decode("" + theValue, StandardCharsets.UTF_8.displayName()).getBytes(StandardCharsets.UTF_8)));
			}
			catch (UnsupportedEncodingException theE)
			{
				myLogger.warn("OpenHABController:doAction", theE);
			}
			HttpClientUtils.execute(aHttpPost);
		});
	}

	private String capitalizeFirstLetter(String theString)
	{
		if (theString == null || theString.length() == 0)
		{
			return theString;
		}
		return theString.substring(0, 1).toUpperCase() + theString.substring(1);
	}
}
