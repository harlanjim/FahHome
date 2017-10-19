package org.fah.house.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WsClimate {
	Float temperature;
	Float humidity;
	
	public Float getTemperature() {
		return temperature;
	}
	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}
	public Float getHumidity() {
		return humidity;
	}
	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}
}
