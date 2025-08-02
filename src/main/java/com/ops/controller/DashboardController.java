package com.ops.controller;

import com.ops.entity.App;
import com.ops.entity.Group;
import com.ops.entity.HealthCheck;
import com.ops.service.AppService;
import com.ops.service.GroupService;
import com.ops.service.HealthCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private HealthCheckService healthCheckService;
    
    @GetMapping
    public String dashboard(Model model) {
        List<Group> groups = groupService.getAllGroups();
        
        // Calculate health status for each group
        for (Group group : groups) {
            boolean groupHealthy = true;
            boolean hasHealthChecks = false;
            
            for (App app : group.getApps()) {
                List<HealthCheck> appHealthChecks = healthCheckService.getHealthChecksByAppId(app.getId());
                if (!appHealthChecks.isEmpty()) {
                    hasHealthChecks = true;
                    boolean appHealthy = true;
                    
                    for (HealthCheck healthCheck : appHealthChecks) {
                        if (healthCheck.getStatus() != HealthCheck.HealthStatus.HEALTHY) {
                            appHealthy = false;
                            groupHealthy = false;
                            break;
                        }
                    }
                    
                    // Set app health status
                    if (appHealthy) {
                        app.setHealthStatus("healthy");
                    } else {
                        app.setHealthStatus("unhealthy");
                    }
                } else {
                    app.setHealthStatus("unknown");
                }
            }
            
            // Set group health status
            if (!hasHealthChecks) {
                group.setHealthStatus("unknown");
            } else if (groupHealthy) {
                group.setHealthStatus("healthy");
            } else {
                group.setHealthStatus("unhealthy");
            }
        }
        
        model.addAttribute("groups", groups);
        return "dashboard";
    }
    
    @GetMapping("/group/{groupId}")
    public String groupDetails(@PathVariable Long groupId, Model model) {
        Group group = groupService.getGroupById(groupId);
        if (group == null) {
            return "redirect:/dashboard";
        }
        
        List<App> apps = appService.getAppsByGroupId(groupId);
        
        // Calculate health status for each app
        for (App app : apps) {
            List<HealthCheck> appHealthChecks = healthCheckService.getHealthChecksByAppId(app.getId());
            if (!appHealthChecks.isEmpty()) {
                boolean appHealthy = true;
                
                for (HealthCheck healthCheck : appHealthChecks) {
                    if (healthCheck.getStatus() != HealthCheck.HealthStatus.HEALTHY) {
                        appHealthy = false;
                        break;
                    }
                }
                
                // Set app health status
                if (appHealthy) {
                    app.setHealthStatus("healthy");
                } else {
                    app.setHealthStatus("unhealthy");
                }
            } else {
                app.setHealthStatus("unknown");
            }
        }
        
        model.addAttribute("group", group);
        model.addAttribute("apps", apps);
        return "group-details";
    }
    
    @GetMapping("/app/{appId}")
    public String appDetails(@PathVariable Long appId, Model model) {
        App app = appService.getAppWithHealthChecks(appId);
        if (app == null) {
            return "redirect:/dashboard";
        }
        
        List<HealthCheck> healthChecks = healthCheckService.getHealthChecksByAppId(appId);
        double healthPercentage = healthCheckService.getAppHealthPercentage(appId);
        
        model.addAttribute("app", app);
        model.addAttribute("healthChecks", healthChecks);
        model.addAttribute("healthPercentage", healthPercentage);
        return "app-details";
    }
    
    @GetMapping("/health-check/{healthCheckId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHealthCheckDetails(@PathVariable Long healthCheckId) {
        Map<String, Object> response = new HashMap<>();
        
        HealthCheck healthCheck = healthCheckService.getHealthCheckById(healthCheckId);
        if (healthCheck != null) {
            response.put("id", healthCheck.getId());
            response.put("name", healthCheck.getName());
            response.put("url", healthCheck.getUrl());
            response.put("status", healthCheck.getStatus().name());
            response.put("responseTime", healthCheck.getResponseTime());
            response.put("lastChecked", healthCheck.getLastChecked());
            response.put("lastResponse", healthCheck.getLastResponse());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Health check not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/health-check/{healthCheckId}/perform")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> performHealthCheck(@PathVariable Long healthCheckId) {
        Map<String, Object> response = new HashMap<>();
        
        HealthCheck healthCheck = healthCheckService.getHealthCheckById(healthCheckId);
        if (healthCheck != null) {
            // Store the original status to check if it changed
            HealthCheck.HealthStatus originalStatus = healthCheck.getStatus();
            
            // Perform the health check
            healthCheckService.performHealthCheck(healthCheck);
            
            // Get the updated health check from database
            HealthCheck updatedHealthCheck = healthCheckService.getHealthCheckById(healthCheckId);
            
            response.put("success", true);
            response.put("message", "Health check performed successfully");
            response.put("status", updatedHealthCheck.getStatus().name());
            response.put("responseTime", updatedHealthCheck.getResponseTime());
            response.put("lastChecked", updatedHealthCheck.getLastChecked());
            response.put("lastResponse", updatedHealthCheck.getLastResponse());
            response.put("statusChanged", originalStatus != updatedHealthCheck.getStatus());
            
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Health check not found");
            return ResponseEntity.notFound().build();
        }
    }
} 