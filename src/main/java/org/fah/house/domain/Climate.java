package org.fah.house.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Climate {

	public final static DateFormat DATEFORMAT_MMDDYYYYHHMM = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	
	@Id
	@GeneratedValue
	Long id;
	
	@NotNull
	@Column
	Float temperature;

	@NotNull
	@Column
	Float humidity;

	@NotNull
	@Column
	Date dateOfReading;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="room_id")
	private Room room;
	

	public Climate() {
		super();
	}
	
	public Climate(Room room, WsClimate wsClimate)
	{
		super();
		this.room = room;
		this.dateOfReading = Calendar.getInstance().getTime();
		this.temperature = wsClimate.getTemperature();
		this.humidity = wsClimate.getHumidity();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Date getDateOfReading() {
		return dateOfReading;
	}

	public void setDateOfReading(Date dateOfReading) {
		this.dateOfReading = dateOfReading;
	}
	
	public String getFormattedDateOfReading()
	{
		return DATEFORMAT_MMDDYYYYHHMM.format(this.dateOfReading);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
}
