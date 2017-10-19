package org.fah.house.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fah.house.domain.Climate;
import org.fah.house.domain.Room;
import org.fah.house.domain.WsClimate;
import org.fah.house.repository.ClimateRepository;
import org.fah.house.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomServiceInterfaceImpl implements RoomServiceInterface {

	
	@Autowired
	protected RoomRepository roomRepository;
	
	@Autowired
	protected ClimateRepository climateRepository;

	@Autowired
	protected WsManager wsManager;
	
	@Autowired
	protected IotManager iotManager;
	
	@Override
	public List<Room> getAllRooms() {
		return (List<Room>)roomRepository.findAll();
	}

	@Override
	public Room saveRoom(Room room) {
		return roomRepository.save(room);
	}

	@Override
	public boolean deleteRoom(Long roomId) {
		Room temp = roomRepository.findOne(roomId);
		if(temp!=null){
			 roomRepository.delete(temp);
			 return true;
		}
		return false;
	}

	@Override
	public Room findRoom(Long roomId) {
		return roomRepository.findOne(roomId);
	}

	@Override
	public Room editRoom(Room editRoom) {
		return roomRepository.save(editRoom);
	}

	@Override
	public Climate saveClimate(Climate climate) {
		return climateRepository.save(climate);
	}

	@Override
	public List<Climate> fetchClimateBy(Long roomId) {
		return climateRepository.findTop64ClimateByRoomIdOrderByDateOfReadingDesc(roomId);
	}

	@Override
	public void refreshSensors() {
		List<Room> rooms = (List<Room>) roomRepository.findAll();
		Map<Long, String> ipAddresses = new HashMap<Long, String>(); 
		for (Room room : rooms)
		{
			ipAddresses.put(room.getId(), room.getIpAddress());
		}
		Map<Long, WsClimate> climateLogs = wsManager.logClimates(ipAddresses);
		for (Long roomId: climateLogs.keySet())
		{
			Room room = roomRepository.findOne(roomId);
			Climate climate = new Climate(room, climateLogs.get(roomId));
			climateRepository.save(climate);
			/*
			try
			{
				iotManager.publishClimateSensor(room.getName(), climate);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			*/
		}
	}
	
	@Override
	public void startSensors() {
		List<Room> rooms = (List<Room>) roomRepository.findAll();
		Map<Long, String> ipAddresses = new HashMap<Long, String>(); 
		for (Room room : rooms)
		{
			ipAddresses.put(room.getId(), room.getIpAddress());
		}
		Map<Long, WsClimate> climateLogs = wsManager.logClimates(ipAddresses);
		for (Long roomId: climateLogs.keySet())
		{
			Room room = roomRepository.findOne(roomId);
			Climate climate = new Climate(room, climateLogs.get(roomId));
			//climateRepository.save(climate);
			try
			{
				iotManager.publishClimateSensor(room.getName(), climate);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public Climate fetchLatestClimateBy(Room room) {
		Map<Long, String> ipAddresses = new HashMap<Long, String>(); 
		ipAddresses.put(room.getId(), room.getIpAddress());
		Map<Long, WsClimate> climateLogs = wsManager.logClimates(ipAddresses);
		return new Climate(room, climateLogs.get(room.getId()));
	}
}
