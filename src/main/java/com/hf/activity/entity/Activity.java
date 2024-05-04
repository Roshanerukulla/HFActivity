package com.hf.activity.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    
 

    private String activityType;

    private Integer steps;

    private Integer duration;

    private String date;
    
    private Long totalSteps;
    
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getTotalSteps() {
		return totalSteps;
	}

	public void setTotalSteps(Long totalSteps2) {
		this.totalSteps = totalSteps2;
	}
	public Activity() {
        // Default constructor
    }

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Activity(Long id, Long userId, String activityType, Integer steps, Integer duration, String date,
			Long totalSteps, LocalDateTime timestamp) {
		super();
		this.id = id;
		this.userId = userId;
		this.activityType = activityType;
		this.steps = steps;
		this.duration = duration;
		this.date = date;
		this.totalSteps = totalSteps;
		this.timestamp = timestamp;
	}

	

    // Getters and setters
}