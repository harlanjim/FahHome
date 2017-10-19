package org.fah.house.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fah.house.domain.Climate;
import org.fah.house.domain.Room;
import org.fah.house.domain.alexa.AlexaRequest;
import org.fah.house.service.RoomServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws")
public class WebServiceController {
	
	private static String TOKEN = "8eba644e7be1cd091cb78b4407ad7277";
	
	@Autowired
	RoomServiceInterface roomServiceInterface;
	
	@RequestMapping(value = "/temperature", method = RequestMethod.POST)
	public @ResponseBody Climate createClimate(@RequestBody AlexaRequest wsRequest)
	{
		if (wsRequest == null)
		{
			System.out.println("Request is blank");
			return null;
		}
		if ("GetRoomTemperature".equals(wsRequest.getRequest().getIntent().getName()))
		{
			List<Room> rooms = roomServiceInterface.getAllRooms();
			for (Room room : rooms)
			{
				if (room.getName().toUpperCase().equals(wsRequest.getRequest().getIntent().getSlots().getRoom().getValue().toUpperCase()))
				{
					List<Climate> climates = roomServiceInterface.fetchClimateBy(room.getId());
					if (climates != null && climates.size() > 0)
					{
						return climates.get(0);
					}
				}
			}
		}
		System.out.println(wsRequest.toString());
		return null;
	}

	@RequestMapping(value = "/climate/{roomName}", method = RequestMethod.GET)
	public Climate getClimate(HttpServletRequest request, HttpServletResponse response, @PathVariable String roomName){
		String lToken = request.getHeader("token");
		if (StringUtils.isEmpty(lToken) || !TOKEN.equals(lToken))
		{
			response.setStatus(HttpStatus.FORBIDDEN.value());
			return null;
		}
		List<Room> rooms = roomServiceInterface.getAllRooms();
		for (Room room : rooms)
		{
			if (StringUtils.replace(room.getName(), " ", "").toUpperCase().equals(roomName.toUpperCase()))
			{
				return roomServiceInterface.fetchLatestClimateBy(room);
				/*
				List<Climate> climates = roomServiceInterface.fetchClimateBy(room.getId());
				if (climates != null && climates.size() > 0)
				{
					return climates.get(0);
				}*/
			}
		}
		return null;
	}
	
	
}
