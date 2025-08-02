package com.ops.controller;

import com.ops.entity.App;
import com.ops.entity.Operation;
import com.ops.service.AppService;
import com.ops.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/operations")
public class OperationsController {
    
    @Autowired
    private AppService appService;
    
    @Autowired
    private OperationService operationService;
    
    @GetMapping
    public String operations(Model model) {
        List<App> apps = appService.getAllApps();
        model.addAttribute("apps", apps);
        return "operations";
    }
    
    @GetMapping("/app/{appId}")
    public String appOperations(@PathVariable Long appId, Model model) {
        App app = appService.getAppWithOperations(appId);
        if (app == null) {
            return "redirect:/operations";
        }
        
        List<Operation> operations = operationService.getOperationsByAppId(appId);
        model.addAttribute("app", app);
        model.addAttribute("operations", operations);
        return "app-operations";
    }
    
    @PostMapping("/execute/{operationId}")
    @ResponseBody
    public String executeOperation(@PathVariable Long operationId) {
        Operation operation = operationService.getOperationById(operationId);
        if (operation != null) {
            return operationService.executeOperation(operation);
        }
        return "Operation not found";
    }
} 