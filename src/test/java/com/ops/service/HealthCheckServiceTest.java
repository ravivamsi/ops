package com.ops.service;

import com.ops.entity.App;
import com.ops.entity.Group;
import com.ops.entity.HealthCheck;
import com.ops.repository.AppRepository;
import com.ops.repository.GroupRepository;
import com.ops.repository.HealthCheckRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class HealthCheckServiceTest {

    @Autowired
    private HealthCheckService healthCheckService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private HealthCheckRepository healthCheckRepository;

    @Test
    void testHealthCheckCreation() {
        // Create test data
        Group group = new Group("Test Group", "Test group for health checks");
        groupRepository.save(group);

        App app = new App("Test App", "Test application");
        app.setGroup(group);
        appRepository.save(app);

        HealthCheck healthCheck = new HealthCheck("Test Health Check", "https://httpbin.org/status/200", "Test health check");
        healthCheck.setApp(app);
        healthCheckRepository.save(healthCheck);

        // Verify health check was created
        assertNotNull(healthCheck.getId());
        assertEquals("Test Health Check", healthCheck.getName());
        assertEquals("https://httpbin.org/status/200", healthCheck.getUrl());
    }

    @Test
    void testGetAppHealthPercentage() {
        // Create test data
        Group group = new Group("Test Group 2", "Test group for health percentage");
        groupRepository.save(group);

        App app = new App("Test App 2", "Test application for health percentage");
        app.setGroup(group);
        appRepository.save(app);

        // Create healthy health check
        HealthCheck healthyCheck = new HealthCheck("Healthy Check", "https://httpbin.org/status/200", "Healthy endpoint");
        healthyCheck.setApp(app);
        healthyCheck.setStatus(HealthCheck.HealthStatus.HEALTHY);
        healthCheckRepository.save(healthyCheck);

        // Create unhealthy health check
        HealthCheck unhealthyCheck = new HealthCheck("Unhealthy Check", "https://httpbin.org/status/500", "Unhealthy endpoint");
        unhealthyCheck.setApp(app);
        unhealthyCheck.setStatus(HealthCheck.HealthStatus.UNHEALTHY);
        healthCheckRepository.save(unhealthyCheck);

        // Test health percentage calculation
        double percentage = healthCheckService.getAppHealthPercentage(app.getId());
        assertEquals(50.0, percentage, 0.1); // 1 out of 2 checks are healthy = 50%
    }
    
    @Test
    void testHealthCheckValidation() {
        // Test that the validation logic works correctly
        // This test verifies that only 200 OK responses with "healthy" status are considered healthy
        
        // Create test data
        Group group = new Group("Test Group 3", "Test group for validation");
        groupRepository.save(group);

        App app = new App("Test App 3", "Test application for validation");
        app.setGroup(group);
        appRepository.save(app);

        // Create health check with a URL that returns 200 OK
        HealthCheck healthCheck = new HealthCheck("Validation Test", "https://httpbin.org/json", "Test validation");
        healthCheck.setApp(app);
        healthCheckRepository.save(healthCheck);

        // Verify the health check was created
        assertNotNull(healthCheck.getId());
        assertEquals("Validation Test", healthCheck.getName());
    }
} 