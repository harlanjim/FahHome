package org.fah.house.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fah.house.domain.Climate;
import org.fah.house.domain.Room;
import org.fah.house.service.RoomServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RoomController {
	
	@Autowired
	RoomServiceInterface roomServiceInterface;

	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String index(Model model) {
		Map<String, Climate> lClimates = new HashMap<String, Climate>();
		List<Room> lRooms = roomServiceInterface.getAllRooms();
		for (Room lRoom: lRooms)
		{
			List<Climate> lList = roomServiceInterface.fetchClimateBy(lRoom.getId());
			if (lList.size() > 0)
			{
				lClimates.put(lRoom.getName(), lList.get(0));
			}
		}
		model.addAttribute("climates", lClimates);
		model.addAttribute("rooms", lRooms);
		return "index";
	}
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String login(Model model) {
		return "login";
	}

	@RequestMapping(value = {"/room/","/room/savepage"}, method = RequestMethod.GET)
	public String savePage(Model model) {
		model.addAttribute("room", new Room());
		model.addAttribute("allRooms", (ArrayList<Room>)roomServiceInterface.getAllRooms());
		return "roomindex";
	}
	
	@RequestMapping(value = {"/room/save"}, method = RequestMethod.POST)
	public String saveEmployee(@ModelAttribute("room") Room room,
			final RedirectAttributes redirectAttributes) {
		
		if(roomServiceInterface.saveRoom(room)!=null) {
			redirectAttributes.addFlashAttribute("saveRoom", "success");
		} else {
			redirectAttributes.addFlashAttribute("saveRoom", "unsuccess");
		}
		return "redirect:/room/savepage";
	}
	
	@RequestMapping(value = "/room/{operation}/{roomId}", method = RequestMethod.GET)
	public String editRemoveEmployee(@PathVariable("operation") String operation,
			@PathVariable("roomId") Long roomId, final RedirectAttributes redirectAttributes,
			Model model) {
		if(operation.equals("delete")) {
			if(roomServiceInterface.deleteRoom(roomId)) {
				redirectAttributes.addFlashAttribute("deletion", "success");
			} else {
				redirectAttributes.addFlashAttribute("deletion", "unsuccess");
			}
		} else if(operation.equals("edit")){
		  Room editRoom = roomServiceInterface.findRoom(roomId);
		  if(editRoom!=null) {
		       model.addAttribute("editRoom", editRoom);
		       return "editPage";
		  } else {
			  redirectAttributes.addFlashAttribute("status","notfound");
		  }
		}
		
		return "redirect:/room/savepage";
	}
	
	@RequestMapping(value = "/room/update", method = RequestMethod.POST)
	public String updateEmployee(@ModelAttribute("editRoom") Room editRoom,
			final RedirectAttributes redirectAttributes) {
		if(roomServiceInterface.editRoom(editRoom)!=null) {
			redirectAttributes.addFlashAttribute("edit", "success");
		} else {
			redirectAttributes.addFlashAttribute("edit", "unsuccess");
		}
		return "redirect:/room/savepage";
	}
	
	
	@RequestMapping(value = "/room/{roomId}/climates", method = RequestMethod.GET)
	public String viewClimates(@PathVariable("roomId") Long roomId, final RedirectAttributes redirectAttributes,
			Model model) {

		List<Climate> lClimates = new ArrayList<Climate>();
		int lCount = 0;
		for (Climate lClimate :	roomServiceInterface.fetchClimateBy(roomId))
		{
			if ((lCount % 4) == 0)
			{
				lClimates.add(lClimate);
			}
			lCount++;
		}
		List<Room> lRooms = roomServiceInterface.getAllRooms();
		Room lRoom = roomServiceInterface.findRoom(roomId);
		model.addAttribute("climates", lClimates);
		model.addAttribute("rooms", lRooms);
		model.addAttribute("room", lRoom);
		return "viewClimates";
	}
}
