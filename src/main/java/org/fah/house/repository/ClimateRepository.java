package org.fah.house.repository;

import java.util.Date;
import java.util.List;

import org.fah.house.domain.Climate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


public interface ClimateRepository extends CrudRepository<Climate, Long> 
{
	public List<Climate> findTop64ClimateByRoomIdOrderByDateOfReadingDesc(Long roomId);
	
	@Modifying
	@Transactional
	@Query("delete from Climate c where c.dateOfReading < ?1")
	public void deleteOldClimates(Date pCutoffDate);
}
