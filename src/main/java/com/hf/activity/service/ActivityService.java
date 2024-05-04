package com.hf.activity.service;

import com.hf.activity.entity.Activity;

import java.util.List;

public interface ActivityService {
    Activity addActivity(Activity activity);
    
    List<Activity> getActivitiesByDate(Long userId, String date);
    
    List<Activity> getActivitiesByDateRange(Long userId, String startDate, String endDate);
    
    int getCurrentStation(Long userId);
    
    Activity updateActivity(Long activityId, Activity activity);
    
    Long getProgress(Long userId); 
    boolean existsActivity(Long userId, Long activityId);

    void deleteActivity(Long activityId);

	Long getTotalsteps(Long userId);

}
