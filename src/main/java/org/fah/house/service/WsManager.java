package org.fah.house.service;

import java.util.HashMap;
import java.util.Map;

import org.fah.house.domain.WsClimate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WsManager {
		
    protected RestTemplate restTemplate = new RestTemplate();
    
    public Map<Long, WsClimate> logClimates(Map<Long, String> sensors)
    {
    	Map<Long, WsClimate> climates = new HashMap<Long, WsClimate>();
    	for (Long roomID: sensors.keySet())
    	{
    		WsClimate climate = null;
    		/*
    		try {
    			climate = restTemplate.getForObject("http://"+sensors.get(roomID)+"/ajax/dht22", WsClimate.class);
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e)
    		{
				e.printStackTrace();
    		}
    		*/
    		try
    		{
        		climate = restTemplate.getForObject("http://"+sensors.get(roomID)+"/ajax/dht22", WsClimate.class);
        		climates.put(roomID, climate);
        		//restTemplate.postForObject("http://"+HOME_WS_HOST+"/house/ws/room/" + roomID + "/climate", climate, WsClimate.class);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return climates;
    }
}
