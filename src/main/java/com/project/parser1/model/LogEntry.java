package com.project.parser1.model;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity (name = "LOG_ENTRY")
public class LogEntry implements Serializable{
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_CMD = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
	
	interface IpAndCount {
		String getIp();
		String getIpCount();
	}
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
        
    /*
     * log file data
     */
    private Timestamp date;
    private String ip;
    private String request;
    private String status;
    private String userAgent;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}



	


}