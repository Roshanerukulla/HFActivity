package com.hf.activity.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hf.activity.entity.Activity;
import com.hf.activity.repository.ActivityRepository;

import jakarta.transaction.Transactional;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Activity addActivity(Activity activity) {
        Long userId = activity.getUserId();
        // Fetch the user's existing total steps
        Long existingTotalSteps = activityRepository.getTotalStepsByUserId(userId);

        int steps = 0;
        if (activity.getSteps() == null) {
            steps = convertDurationToSteps(activity.getActivityType(), activity.getDuration());
        } else {
            steps = activity.getSteps();
        }

        // Ensure existingTotalSteps is not null before performing arithmetic operations
        if (existingTotalSteps == null) {
            existingTotalSteps = 0L; // Set it to 0 if it's null
        }

        // Add the steps from the new activity to the existing total steps
        Long totalSteps = existingTotalSteps + steps;

        activity.setSteps(steps);
        activity.setTotalSteps(totalSteps);
        activity.setTimestamp(LocalDateTime.now()); // Set current timestamp
        return activityRepository.save(activity);
    }




    @Override
    public int getCurrentStation(Long userId) {
        Activity latestActivity = activityRepository.findFirstByUserIdOrderByTimestampDesc(userId);
        if (latestActivity != null) {
            Long totalSteps = latestActivity.getTotalSteps();
            return calculateStation(totalSteps);
        } else {
            return 0; // or any default value if no activities are found
        }
    }


    @Override
    public Activity updateActivity(Long activityId, Activity activity) {
        // Retrieve the existing activity
        Activity existingActivity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with id: " + activityId));

        // Get the previous steps recorded for this activity
        Integer previousSteps = existingActivity.getSteps(); // Use Integer instead of int to handle null

        // Calculate the difference between previous steps and updated steps
        int stepsDifference;
        if (activity.getSteps() != null && previousSteps != null) {
            stepsDifference = activity.getSteps() - previousSteps;
        } else {
            // Handle the case where either steps or previousSteps is null
            int newSteps = convertDurationToSteps(activity.getActivityType(), activity.getDuration());
            stepsDifference = newSteps - (previousSteps != null ? previousSteps : 0);
        }

        // Update the activity with new data
        existingActivity.setActivityType(activity.getActivityType());
        existingActivity.setSteps(activity.getSteps());
        existingActivity.setDuration(activity.getDuration());
        existingActivity.setDate(activity.getDate());

        // Update the total steps for the user
        Long userId = existingActivity.getUserId();
        Long existingTotalSteps = activityRepository.getTotalStepsByUserId(userId);
        if (existingTotalSteps == null) {
            existingTotalSteps = 0L;
        }
        Long newTotalSteps = existingTotalSteps + stepsDifference;
        existingActivity.setTotalSteps(newTotalSteps);

        // Save the updated activity
        return activityRepository.save(existingActivity);
    }



    @Override
    public Long getProgress(Long userId) {
        Activity latestActivity = activityRepository.findFirstByUserIdOrderByTimestampDesc(userId);
        if (latestActivity != null) {
            Long totalSteps = latestActivity.getTotalSteps();
            return calculateProgress(totalSteps);
        } else {
            return 0L; // or any default value if no activities are found
        }
    }




    @Override
    public List<Activity> getActivitiesByDate(Long userId, String date) {
        return activityRepository.findByUserIdAndDate(userId, date);
    }

    @Override
    public List<Activity> getActivitiesByDateRange(Long userId, String startDate, String endDate) {
        return activityRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
    }

    
    public int convertDurationToSteps(String activityType, int duration) {
        int steps = 0;

        switch (activityType.toUpperCase()) {
            case "AEROBICS CLASS":
                steps = duration * 145;
                break;
            case "BASKETBALL, GAME":
                steps = duration * 178;
                break;
            case "BICYCLING":
                steps = duration * 133;
                break;
            case "CALISTHENICS":
                steps = duration * 106;
                break;
            case "CIRCUIT TRAINING, GENERAL":
                steps = duration * 178;
                break;
            case "CLIMBING, ROCK OR MOUNTAIN":
                steps = duration * 273;
                break;
            case "DANCING":
                steps = duration * 133;
                break;
            case "ELLIPTICAL MACHINE":
                steps = duration * 249;
                break;
            case "FOOTBALL":
                steps = duration * 189;
                break;
            case "HIKING":
                steps = duration * 172;
                break;
            case "HORSEBACK RIDING, GENERAL":
                steps = duration * 102;
                break;
            case "ICE SKATING":
                steps = duration * 203;
                break;
            case "JOGGING":
                steps = duration * 156;
                break;
            case "JUMPING ROPE, MODERATE":
                steps = duration * 244;
                break;
            case "MARTIAL ARTS":
                steps = duration * 222;
                break;
            case "ROWING MACHINE, MODERATE":
                steps = duration * 111;
                break;
            case "RUNNING, 8 MINUTE MILE":
                steps = duration * 278;
                break;
            case "SKIING, CROSS-COUNTRY":
                steps = duration * 200;
                break;
            case "SOCCER, RECREATIONAL":
                steps = duration * 156;
                break;
            case "SWIMMING LAPS, MODERATE":
                steps = duration * 212;
                break;
            case "TENNIS, SINGLES":
                steps = duration * 178;
                break;
            case "VOLLEYBALL":
                steps = duration * 89;
                break;
            case "WALKING":
                steps = duration * 100;
                break;
            case "WEIGHT LIFTING":
                steps = duration * 133;
                break;
            case "YARD WORK":
                steps = duration * 111;
                break;
            default:
                // Handle unknown activity types
                break;
        }

        return steps;
    }


    private int calculateStation(Long totalSteps) {
        if (totalSteps < 10000) {
            return 1;
        } else if (totalSteps < 20000) {
            return 2;
        } else if (totalSteps < 30000) {
            return 3;
        } else if (totalSteps < 40000) {
            return 4;
        } else if (totalSteps < 50000) {
            return 5;
        } else {
            return 6;
        }
    }

    private Long calculateProgress(Long totalSteps) {
        int currentStation = calculateStation(totalSteps);
        long nextStationSteps = (currentStation * 10000L); // Assuming each station is at 50,000 steps
        long stepsToNextStation = nextStationSteps - totalSteps;

        // Calculate the progress percentage
        Long progress = (((nextStationSteps - stepsToNextStation) * 100) / nextStationSteps);
        return progress;
    }


    private int recalculateTotalStepsForDate(Long userId, String date) {
        List<Activity> activitiesForDate = activityRepository.findByUserIdAndDate(userId, date);
        int totalStepsForDay = activitiesForDate.stream().mapToInt(Activity::getSteps).sum();
        return totalStepsForDay;
    }


    @Override
    public boolean existsActivity(Long userId, Long activityId) {
        return activityRepository.existsByIdAndUserId(activityId, userId);
    }

    @Override
    @Transactional
    public void deleteActivity(Long activityId) {
        activityRepository.deleteById(activityId);
    }

	@Override
	public Long getTotalsteps(Long userId) {
		// TODO Auto-generated method stub
		return activityRepository.getTotalStepsByUserId(userId);
	}
		
	}

