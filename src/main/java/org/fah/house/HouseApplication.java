package org.fah.house;

import java.util.Calendar;

import org.fah.house.repository.ClimateRepository;
import org.fah.house.service.RoomServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class HouseApplication {
	
	@Autowired
	protected ClimateRepository climateRepository;

	@Autowired
	RoomServiceInterface roomServiceInterface;

	public static void main(String[] args) {
		SpringApplication.run(HouseApplication.class, args);
	}
	
    @Scheduled(cron="0 0 0 1/1 * *")
    public void pruneClimateJob()
    {
    	System.out.println("Run Prune");
    	Calendar lCal = Calendar.getInstance();
    	lCal.add(Calendar.DAY_OF_YEAR, -14);
    	climateRepository.deleteOldClimates(lCal.getTime());
    }
    
    @Scheduled(cron="0 0/15 * 1/1 * *")
    public void refreshClimateJob()
    {
    	System.out.println("Run Refresh Climates");
    	roomServiceInterface.refreshSensors();
    }
}
