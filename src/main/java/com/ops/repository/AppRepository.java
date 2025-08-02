package com.ops.repository;

import com.ops.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<App, Long> {
    List<App> findByGroupId(Long groupId);
    
    @Query("SELECT a FROM App a JOIN FETCH a.healthChecks WHERE a.id = :appId")
    App findByIdWithHealthChecks(@Param("appId") Long appId);
    
    @Query("SELECT a FROM App a JOIN FETCH a.operations WHERE a.id = :appId")
    App findByIdWithOperations(@Param("appId") Long appId);
} 