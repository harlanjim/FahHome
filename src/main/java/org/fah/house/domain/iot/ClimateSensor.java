package org.fah.house.domain.iot;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotDeviceProperty;

public class ClimateSensor extends AWSIotDevice
{
	@AWSIotDeviceProperty
	private int temperature;
	@AWSIotDeviceProperty
	private int celTemperature;
	@AWSIotDeviceProperty
	private int humidity;
	
	public ClimateSensor(String thingName) {
		super(thingName);
	}
	
	public ClimateSensor(String thingName, int temperature, int celTemperature, int humidity)
	{
		super(thingName);
		this.temperature = temperature;
		this.celTemperature = celTemperature;
		this.humidity = humidity;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getCelTemperature() {
		return celTemperature;
	}

	public void setCelTemperature(int celTemperature) {
		this.celTemperature = celTemperature;
	}

	public int getHumidity() {
		return humidity;
	}

	public void setHumidity(int humidity) {
		this.humidity = humidity;
	}

}
