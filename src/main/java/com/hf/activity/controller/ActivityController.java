package com.hf.activity.controller;

import com.hf.activity.entity.Activity;
import com.hf.activity.feign.UserFeign;

import com.hf.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserFeign userFeign;

    @PostMapping("/{userId}")
    public ResponseEntity<?> addActivity(@PathVariable Long userId, @RequestBody Activity activity) {
        Long user = userFeign.getUserId(userId);

        // Check if user exists
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

  
        activity.setUserId(userId);
        Activity newActivity = activityService.addActivity(activity);
        return new ResponseEntity<>(newActivity, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}/{date}")
    public ResponseEntity<List<Activity>> getActivitiesByDate(@PathVariable Long userId, @PathVariable String date) {
        List<Activity> activities = activityService.getActivitiesByDate(userId, date);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @GetMapping("/{userId}/{startDate}/{endDate}")
    public ResponseEntity<List<Activity>> getActivitiesByDateRange(@PathVariable Long userId,
                                                                   @PathVariable String startDate,
                                                                   @PathVariable String endDate) {
        List<Activity> activities = activityService.getActivitiesByDateRange(userId, startDate, endDate);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @GetMapping("/station/{userId}")
    public ResponseEntity<Integer> getCurrentStation(@PathVariable Long userId) {
        int currentStation = activityService.getCurrentStation(userId);
        return new ResponseEntity<>(currentStation, HttpStatus.OK);
    }

    @PutMapping("/{userId}/{activityId}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long userId, @PathVariable Long activityId, @RequestBody Activity activity) {
        Activity updatedActivity = activityService.updateActivity(activityId, activity);
        return new ResponseEntity<>(updatedActivity, HttpStatus.OK);
    }

    @GetMapping("/{userId}/progress")
    public ResponseEntity<Long> getProgress(@PathVariable Long userId) {
        Long progress = activityService.getProgress(userId);
        return new ResponseEntity<>(progress, HttpStatus.OK);
    }
    
    @DeleteMapping("/{userId}/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long userId, @PathVariable Long activityId) {
        // Check if the activity exists for the given user
        if (activityService.existsActivity(userId, activityId)) {
            // Delete the activity
            activityService.deleteActivity(activityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            // Activity not found for the given user
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    } 
    @GetMapping("/{userId}/total-steps")
    public ResponseEntity<Long> getTotalSteps(@PathVariable Long userId) {
        Long TotalSteps= activityService.getTotalsteps(userId);
        return new ResponseEntity<>(TotalSteps, HttpStatus.OK);
    }
    

}
