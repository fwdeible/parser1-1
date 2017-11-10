package com.project.parser1.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LogEntryRepository extends CrudRepository<LogEntry, Long> {


	Iterable<LogEntry> findAllByIp(String ip);
	
	
	
	/**
	 * SQL example:
	select ip, count(*) c from log_entry e
	where e.date  >= '2017-01-01.13:00:00'
    and e.date <= '2017-01-01.14:00:00'
    group by ip
    having c > 100;
	 */
	
	public static final String FIND_IP_BY_THRESHOLD_QUERY = "select ip as ip from LOG_ENTRY e\n" + 
			"	where e.date  >= :startDate \n " +
			"    and e.date <= :endDate \n " + 
			"    group by e.ip\n" + 
			"    having count(e.ip) > :threshold";
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param threshold
	 * @return list of IP's exceeding threshold requests from startDate to endDate
	 */
	@Query(FIND_IP_BY_THRESHOLD_QUERY)
	public List<String> findIpByThreshold(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate,
			@Param("threshold") long threshold);
}
