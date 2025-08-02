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

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HealthCheckService {
    
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
            healthCheck.setStatus(isHealthy ? HealthCheck.HealthStatus.HEALTHY : HealthCheck.HealthStatus.UNHEALTHY);
            
            healthCheckRepository.save(healthCheck);
            
        } catch (UnirestException e) {
            healthCheck.setStatus(HealthCheck.HealthStatus.UNHEALTHY);
            healthCheck.setLastChecked(LocalDateTime.now());
            healthCheck.setLastResponse("Error: " + e.getMessage());
            healthCheckRepository.save(healthCheck);
        }
    }
    
    private boolean validateHealthResponse(HttpResponse<JsonNode> response) {
        // Check HTTP status code first
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
            return false;
        }
        
        try {
            JsonNode body = response.getBody();
            String responseText = body.toString().toLowerCase();
            
            // Check if response contains health status indicators
            if (responseText.contains("\"status\"")) {
                if (responseText.contains("\"healthy\"") || responseText.contains("\"up\"") || responseText.contains("\"ok\"")) {
                    return true;
                }
                if (responseText.contains("\"unhealthy\"") || responseText.contains("\"down\"") || responseText.contains("\"error\"")) {
                    return false;
                }
            }
            
            if (responseText.contains("\"health\"")) {
                if (responseText.contains("\"healthy\"") || responseText.contains("\"up\"") || responseText.contains("\"ok\"")) {
                    return true;
                }
                if (responseText.contains("\"unhealthy\"") || responseText.contains("\"down\"") || responseText.contains("\"error\"")) {
                    return false;
                }
            }
            
            if (responseText.contains("\"state\"")) {
                if (responseText.contains("\"healthy\"") || responseText.contains("\"up\"") || responseText.contains("\"ok\"")) {
                    return true;
                }
                if (responseText.contains("\"unhealthy\"") || responseText.contains("\"down\"") || responseText.contains("\"error\"")) {
                    return false;
                }
            }
            
            // If no specific health indicators found, consider 2xx responses as healthy
            return true;
            
        } catch (Exception e) {
            // If JSON parsing fails, consider it unhealthy
            return false;
        }
    }
    
    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void performScheduledHealthChecks() {
        List<HealthCheck> allHealthChecks = healthCheckRepository.findAll();
        for (HealthCheck healthCheck : allHealthChecks) {
            performHealthCheck(healthCheck);
        }
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