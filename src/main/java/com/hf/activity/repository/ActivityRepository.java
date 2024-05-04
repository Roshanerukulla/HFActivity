package com.hf.activity.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hf.activity.entity.Activity;



@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserIdAndDate(Long userId, String date);

    List<Activity> findByUserIdAndDateBetween(Long userId, String startDate, String endDate);

    List<Activity> findByUserId(Long userId);
    boolean existsByIdAndUserId(Long activityId, Long userId);

   
    @Query(value = "SELECT SUM(steps) FROM activity WHERE user_id = :userId", nativeQuery = true)
    Long getTotalStepsByUserId(@Param("userId") Long userId);




	Activity findFirstByUserIdOrderByTimestampDesc(Long userId);
}