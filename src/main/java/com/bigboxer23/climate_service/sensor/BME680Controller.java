package com.bigboxer23.climate_service.sensor;

import com.bigboxer23.ClimateController;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Component to interface with a BME680 sensor.  Kicks off python script to read from sensor, keeps last 30
 * sensor readings in cache and returns average of those when requested
 */
@Component
public class BME680Controller
{
	private static final Logger myLogger = LoggerFactory.getLogger(ClimateController.class);

	/**
	 * Various sensors we get readings about
	 */
	private static final List<String> kClimateItems = new ArrayList<String>()
	{{
		add("temperature");
		add("pressure");
		add("humidity");
		add("quality");
	}};

	private Cache<Long, Map<String, Float>> myClimateCache;

	public BME680Controller()
	{
		myClimateCache = CacheBuilder
				.newBuilder()
				.maximumSize(30)
				.build();
		startSensorProcess();
	}

	/**
	 * Data formatted like `19.73 C,985.42 hPa,39.04 %RH,10370 Ohm`
	 *
	 * @param theRawData data read from the sensor
	 */
	private void readSensorData(String theRawData)
	{
		if (theRawData == null || !theRawData.contains("Ohm"))
		{
			myLogger.warn("Data read from processor is bad: " + theRawData);
			return;
		}
		String[] aContent = theRawData.split(",");
		if (aContent.length != 4)
		{
			myLogger.warn("Data read from processor is bad: " + theRawData);
			return;
		}
		myLogger.debug("Sensor Data: " + theRawData);
		Map<String, Float> aData = new HashMap<>();
		for (int ai = 0; ai < kClimateItems.size(); ai++)
		{
			aData.put(kClimateItems.get(ai), Float.parseFloat(aContent[ai].split(" ")[0]));
		}
		normalizeQuality(aData);
		myClimateCache.put(System.currentTimeMillis(), aData);
	}

	private void normalizeQuality(Map<String, Float> theData)
	{
		theData.computeIfPresent("quality", (k, v) ->  v / 1000);
	}

	/**
	 * Take the average value for each climate data point, sampled over the last ~30sec
	 *
	 * @return temperature, humidity, pressure, air quality
	 */
	public Map<String, Float> getClimateData()
	{
		List<Map<String, Float>> aList = new ArrayList<>(myClimateCache.asMap().values());
		Map<String, Float> aData = new HashMap<>();
		aList.forEach(theMap -> kClimateItems.forEach(theItem ->
		{
			float aValue = aData.getOrDefault(theItem, 0f);
			aData.put(theItem, aValue + theMap.get(theItem));
		}));
		kClimateItems.forEach(theItem -> aData.put(theItem, aData.get(theItem) / aList.size()));
		return aData;
	}

	/**
	 * Start the python script to get readings from the sensor
	 */
	private void startSensorProcess()
	{
		try
		{
			new ProcessExecutor()
					.command("python", "-u", "/home/pi/com/bigboxer23/climate-service/1.0.0/read-all.py")
					.redirectOutput(new LogOutputStream()
					{
						@Override
						protected void processLine(String theLine)
						{
							readSensorData(theLine);
						}
					})
					.destroyOnExit()
					.start();
		}
		catch (IOException theE)
		{
			myLogger.warn("startSensorProcess ", theE);
		}
	}
}
