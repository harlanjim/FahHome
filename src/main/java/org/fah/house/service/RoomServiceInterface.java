package org.fah.house.service;

import java.util.List;

import org.fah.house.domain.Climate;
import org.fah.house.domain.Room;

public interface RoomServiceInterface {

	public List<Room> getAllRooms();

	public Room saveRoom(Room room);

	public boolean deleteRoom(Long roomId);

	public Room findRoom(Long roomId);

	public Room editRoom(Room editRoom);

	public Climate saveClimate(Climate climate);
	
	public List<Climate> fetchClimateBy(Long roomId); 
	
	public Climate fetchLatestClimateBy(Room room); 
	
	public void refreshSensors();
	
	public void startSensors();
}
