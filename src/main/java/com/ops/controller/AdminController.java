package com.ops.controller;

import com.ops.entity.*;
import com.ops.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private GroupService groupService;
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private HealthCheckService healthCheckService;
    
    @Autowired
    private OperationService operationService;
    
    // Group Management
    @GetMapping("/groups")
    public String listGroups(Model model) {
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("groups", groups);
        model.addAttribute("newGroup", new Group());
        return "admin/groups";
    }
    
    @PostMapping("/groups")
    public String createGroup(@Valid @ModelAttribute("newGroup") Group group, 
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Group> groups = groupService.getAllGroups();
            model.addAttribute("groups", groups);
            return "admin/groups";
        }
        
        groupService.saveGroup(group);
        return "redirect:/admin/groups";
    }
    
    @GetMapping("/groups/{id}/edit")
    public String editGroup(@PathVariable Long id, Model model) {
        Group group = groupService.getGroupById(id);
        if (group == null) {
            return "redirect:/admin/groups";
        }
        model.addAttribute("group", group);
        model.addAttribute("groups", groupService.getAllGroups());
        return "admin/edit-group";
    }
    
    @PostMapping("/groups/{id}/edit")
    public String updateGroup(@PathVariable Long id, @Valid @ModelAttribute("group") Group group, 
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.getAllGroups());
            return "admin/edit-group";
        }
        
        Group existingGroup = groupService.getGroupById(id);
        if (existingGroup != null) {
            existingGroup.setName(group.getName());
            existingGroup.setDescription(group.getDescription());
            groupService.saveGroup(existingGroup);
        }
        
        return "redirect:/admin/groups";
    }
    
    @PostMapping("/groups/{id}/delete")
    public String deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return "redirect:/admin/groups";
    }
    
    // App Management
    @GetMapping("/apps")
    public String listApps(Model model) {
        List<App> apps = appService.getAllApps();
        List<Group> groups = groupService.getAllGroups();
        model.addAttribute("apps", apps);
        model.addAttribute("groups", groups);
        model.addAttribute("newApp", new App());
        return "admin/apps";
    }
    
    @PostMapping("/apps")
    public String createApp(@Valid @ModelAttribute("newApp") App app, 
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<App> apps = appService.getAllApps();
            List<Group> groups = groupService.getAllGroups();
            model.addAttribute("apps", apps);
            model.addAttribute("groups", groups);
            return "admin/apps";
        }
        
        appService.saveApp(app);
        return "redirect:/admin/apps";
    }
    
    @GetMapping("/apps/{id}/edit")
    public String editApp(@PathVariable Long id, Model model) {
        App app = appService.getAppById(id);
        if (app == null) {
            return "redirect:/admin/apps";
        }
        model.addAttribute("app", app);
        model.addAttribute("apps", appService.getAllApps());
        model.addAttribute("groups", groupService.getAllGroups());
        return "admin/edit-app";
    }
    
    @PostMapping("/apps/{id}/edit")
    public String updateApp(@PathVariable Long id, @Valid @ModelAttribute("app") App app, 
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("apps", appService.getAllApps());
            model.addAttribute("groups", groupService.getAllGroups());
            return "admin/edit-app";
        }
        
        App existingApp = appService.getAppById(id);
        if (existingApp != null) {
            existingApp.setName(app.getName());
            existingApp.setDescription(app.getDescription());
            existingApp.setGroup(app.getGroup());
            appService.saveApp(existingApp);
        }
        
        return "redirect:/admin/apps";
    }
    
    @PostMapping("/apps/{id}/delete")
    public String deleteApp(@PathVariable Long id) {
        appService.deleteApp(id);
        return "redirect:/admin/apps";
    }
    
    // Health Check Management
    @GetMapping("/health-checks")
    public String listHealthChecks(Model model) {
        List<HealthCheck> healthChecks = healthCheckService.getAllHealthChecks();
        List<App> apps = appService.getAllApps();
        model.addAttribute("healthChecks", healthChecks);
        model.addAttribute("apps", apps);
        model.addAttribute("newHealthCheck", new HealthCheck());
        return "admin/health-checks";
    }
    
    @PostMapping("/health-checks")
    public String createHealthCheck(@Valid @ModelAttribute("newHealthCheck") HealthCheck healthCheck, 
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<HealthCheck> healthChecks = healthCheckService.getAllHealthChecks();
            List<App> apps = appService.getAllApps();
            model.addAttribute("healthChecks", healthChecks);
            model.addAttribute("apps", apps);
            return "admin/health-checks";
        }
        
        healthCheckService.saveHealthCheck(healthCheck);
        return "redirect:/admin/health-checks";
    }
    
    @GetMapping("/health-checks/{id}/edit")
    public String editHealthCheck(@PathVariable Long id, Model model) {
        HealthCheck healthCheck = healthCheckService.getHealthCheckById(id);
        if (healthCheck == null) {
            return "redirect:/admin/health-checks";
        }
        model.addAttribute("healthCheck", healthCheck);
        model.addAttribute("healthChecks", healthCheckService.getAllHealthChecks());
        model.addAttribute("apps", appService.getAllApps());
        return "admin/edit-health-check";
    }
    
    @PostMapping("/health-checks/{id}/edit")
    public String updateHealthCheck(@PathVariable Long id, @Valid @ModelAttribute("healthCheck") HealthCheck healthCheck, 
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("healthChecks", healthCheckService.getAllHealthChecks());
            model.addAttribute("apps", appService.getAllApps());
            return "admin/edit-health-check";
        }
        
        HealthCheck existingHealthCheck = healthCheckService.getHealthCheckById(id);
        if (existingHealthCheck != null) {
            existingHealthCheck.setName(healthCheck.getName());
            existingHealthCheck.setUrl(healthCheck.getUrl());
            existingHealthCheck.setDescription(healthCheck.getDescription());
            existingHealthCheck.setApp(healthCheck.getApp());
            healthCheckService.saveHealthCheck(existingHealthCheck);
        }
        
        return "redirect:/admin/health-checks";
    }
    
    @PostMapping("/health-checks/{id}/delete")
    public String deleteHealthCheck(@PathVariable Long id) {
        healthCheckService.deleteHealthCheck(id);
        return "redirect:/admin/health-checks";
    }
    
    // Operation Management
    @GetMapping("/operations")
    public String listOperations(Model model) {
        List<Operation> operations = operationService.getAllOperations();
        List<App> apps = appService.getAllApps();
        model.addAttribute("operations", operations);
        model.addAttribute("apps", apps);
        model.addAttribute("newOperation", new Operation());
        return "admin/operations";
    }
    
    @PostMapping("/operations")
    public String createOperation(@Valid @ModelAttribute("newOperation") Operation operation, 
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            List<Operation> operations = operationService.getAllOperations();
            List<App> apps = appService.getAllApps();
            model.addAttribute("operations", operations);
            model.addAttribute("apps", apps);
            return "admin/operations";
        }
        
        operationService.saveOperation(operation);
        return "redirect:/admin/operations";
    }
    
    @GetMapping("/operations/{id}/edit")
    public String editOperation(@PathVariable Long id, Model model) {
        Operation operation = operationService.getOperationById(id);
        if (operation == null) {
            return "redirect:/admin/operations";
        }
        model.addAttribute("operation", operation);
        model.addAttribute("operations", operationService.getAllOperations());
        model.addAttribute("apps", appService.getAllApps());
        return "admin/edit-operation";
    }
    
    @PostMapping("/operations/{id}/edit")
    public String updateOperation(@PathVariable Long id, @Valid @ModelAttribute("operation") Operation operation, 
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("operations", operationService.getAllOperations());
            model.addAttribute("apps", appService.getAllApps());
            return "admin/edit-operation";
        }
        
        Operation existingOperation = operationService.getOperationById(id);
        if (existingOperation != null) {
            existingOperation.setName(operation.getName());
            existingOperation.setUrl(operation.getUrl());
            existingOperation.setMethod(operation.getMethod());
            existingOperation.setDescription(operation.getDescription());
            existingOperation.setRequestBody(operation.getRequestBody());
            existingOperation.setHeaders(operation.getHeaders());
            existingOperation.setApp(operation.getApp());
            operationService.saveOperation(existingOperation);
        }
        
        return "redirect:/admin/operations";
    }
    
    @PostMapping("/operations/{id}/delete")
    public String deleteOperation(@PathVariable Long id) {
        operationService.deleteOperation(id);
        return "redirect:/admin/operations";
    }
} 