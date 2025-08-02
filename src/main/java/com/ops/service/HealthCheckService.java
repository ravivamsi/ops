package com.ops.service;

import com.ops.entity.HealthCheck;
import com.ops.repository.HealthCheckRepository;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthCheckService {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);
    
    @Autowired
    private HealthCheckRepository healthCheckRepository;
    
    public List<HealthCheck> getAllHealthChecks() {
        return healthCheckRepository.findAll();
    }
    
    public List<HealthCheck> getHealthChecksByAppId(Long appId) {
        return healthCheckRepository.findByAppId(appId);
    }
    
    public List<HealthCheck> getHealthChecksByGroupId(Long groupId) {
        return healthCheckRepository.findByGroupId(groupId);
    }
    
    public HealthCheck getHealthCheckById(Long id) {
        return healthCheckRepository.findById(id).orElse(null);
    }
    
    public HealthCheck saveHealthCheck(HealthCheck healthCheck) {
        return healthCheckRepository.save(healthCheck);
    }
    
    public void deleteHealthCheck(Long id) {
        healthCheckRepository.deleteById(id);
    }
    
    public void performHealthCheck(HealthCheck healthCheck) {
        try {
            logger.debug("Performing health check for: {} at URL: {}", healthCheck.getName(), healthCheck.getUrl());
            long startTime = System.currentTimeMillis();
            
            HttpResponse<JsonNode> response = Unirest.get(healthCheck.getUrl())
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .asJson();
            
            long endTime = System.currentTimeMillis();
            int responseTime = (int) (endTime - startTime);
            
            healthCheck.setResponseTime(responseTime);
            healthCheck.setLastChecked(LocalDateTime.now());
            healthCheck.setLastResponse(response.getBody().toString());
            
            // Enhanced health status validation
            boolean isHealthy = validateHealthResponse(response);
            HealthCheck.HealthStatus newStatus = isHealthy ? HealthCheck.HealthStatus.HEALTHY : HealthCheck.HealthStatus.UNHEALTHY;
            healthCheck.setStatus(newStatus);
            
            logger.info("Health check for {}: Status={}, ResponseTime={}ms, HTTPStatus={}", 
                      healthCheck.getName(), newStatus, responseTime, response.getStatus());
            
            healthCheckRepository.save(healthCheck);
            
        } catch (UnirestException e) {
            logger.error("Health check failed for {}: {}", healthCheck.getName(), e.getMessage());
            healthCheck.setStatus(HealthCheck.HealthStatus.UNHEALTHY);
            healthCheck.setLastChecked(LocalDateTime.now());
            healthCheck.setLastResponse("Error: " + e.getMessage());
            healthCheckRepository.save(healthCheck);
        }
    }
    
    private boolean validateHealthResponse(HttpResponse<JsonNode> response) {
        // Check for exactly 200 OK status
        if (response.getStatus() != 200) {
            logger.debug("Health check validation failed: HTTP status {} is not 200", response.getStatus());
            return false;
        }
        
        try {
            JsonNode body = response.getBody();
            String responseText = body.toString().toLowerCase();
            
            // Check if response contains "healthy" status value
            if (responseText.contains("\"status\"")) {
                if (responseText.contains("\"healthy\"")) {
                    return true;
                }
                // If status field exists but doesn't contain "healthy", consider unhealthy
                return false;
            }
            
            if (responseText.contains("\"health\"")) {
                if (responseText.contains("\"healthy\"")) {
                    return true;
                }
                // If health field exists but doesn't contain "healthy", consider unhealthy
                return false;
            }
            
            if (responseText.contains("\"state\"")) {
                if (responseText.contains("\"healthy\"")) {
                    return true;
                }
                // If state field exists but doesn't contain "healthy", consider unhealthy
                return false;
            }
            
            // If no status/health/state fields found, check if response contains "healthy" anywhere
            if (responseText.contains("\"healthy\"")) {
                return true;
            }
            
            // If no "healthy" indicator found, consider unhealthy
            logger.debug("Health check validation failed: No 'healthy' indicator found in response");
            return false;
            
        } catch (Exception e) {
            // If JSON parsing fails, consider it unhealthy
            logger.debug("Health check validation failed: JSON parsing error - {}", e.getMessage());
            return false;
        }
    }
    
    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    public void performScheduledHealthChecks() {
        List<HealthCheck> allHealthChecks = healthCheckRepository.findAll();
        logger.debug("Starting scheduled health checks for {} endpoints", allHealthChecks.size());
        
        for (HealthCheck healthCheck : allHealthChecks) {
            performHealthCheck(healthCheck);
        }
        
        logger.debug("Completed scheduled health checks");
    }
    
    public double getAppHealthPercentage(Long appId) {
        long totalChecks = healthCheckRepository.countByAppId(appId);
        if (totalChecks == 0) {
            return 0.0;
        }
        long healthyChecks = healthCheckRepository.countHealthyByAppId(appId);
        return (double) healthyChecks / totalChecks * 100;
    }
} 