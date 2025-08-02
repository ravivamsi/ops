package com.ops.repository;

import com.ops.entity.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
    List<HealthCheck> findByAppId(Long appId);
    
    @Query("SELECT hc FROM HealthCheck hc WHERE hc.app.group.id = :groupId")
    List<HealthCheck> findByGroupId(@Param("groupId") Long groupId);
    
    @Query("SELECT COUNT(hc) FROM HealthCheck hc WHERE hc.app.id = :appId AND hc.status = 'HEALTHY'")
    long countHealthyByAppId(@Param("appId") Long appId);
    
    @Query("SELECT COUNT(hc) FROM HealthCheck hc WHERE hc.app.id = :appId")
    long countByAppId(@Param("appId") Long appId);
} 