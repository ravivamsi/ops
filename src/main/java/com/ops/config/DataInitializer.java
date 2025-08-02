package com.ops.config;

import com.ops.entity.*;
import com.ops.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private AppRepository appRepository;
    
    @Autowired
    private HealthCheckRepository healthCheckRepository;
    
    @Autowired
    private OperationRepository operationRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample groups
        Group webServices = new Group("Web Services", "External web services and APIs");
        groupRepository.save(webServices);
        
        Group internalApps = new Group("Internal Applications", "Internal business applications");
        groupRepository.save(internalApps);
        
        Group microservices = new Group("Microservices", "Microservice architecture components");
        groupRepository.save(microservices);
        
        // Create sample apps
        App userService = new App("User Service", "User management and authentication service");
        userService.setGroup(webServices);
        appRepository.save(userService);
        
        App paymentService = new App("Payment Service", "Payment processing and billing service");
        paymentService.setGroup(webServices);
        appRepository.save(paymentService);
        
        App inventoryApp = new App("Inventory App", "Inventory management application");
        inventoryApp.setGroup(internalApps);
        appRepository.save(inventoryApp);
        
        App orderService = new App("Order Service", "Order processing and fulfillment service");
        orderService.setGroup(microservices);
        appRepository.save(orderService);
        
        // Create sample health checks
        HealthCheck userHealth = new HealthCheck("User Service Health", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/gis3", "Main health endpoint");
        userHealth.setApp(userService);
        healthCheckRepository.save(userHealth);

        HealthCheck userHealth2 = new HealthCheck("User Service Health 2", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/gis2", "Main health endpoint");
        userHealth2.setApp(userService);
        healthCheckRepository.save(userHealth2);

        HealthCheck userHealth3 = new HealthCheck("User Service Health 3", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/gis1", "Main health endpoint");
        userHealth3.setApp(userService);
        healthCheckRepository.save(userHealth3);
        
        HealthCheck paymentHealth = new HealthCheck("Payment Service Health", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/poc3", "Payment service health check");
        paymentHealth.setApp(paymentService);
        healthCheckRepository.save(paymentHealth);
        
        HealthCheck inventoryHealth = new HealthCheck("Inventory App Health", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/poc2", "Inventory application health");
        inventoryHealth.setApp(inventoryApp);
        healthCheckRepository.save(inventoryHealth);

        HealthCheck paymentHealth2 = new HealthCheck("Payment Service Health 2", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/options2", "Payment service health check");
        paymentHealth2.setApp(paymentService);
        healthCheckRepository.save(paymentHealth2);
        
        HealthCheck inventoryHealth2 = new HealthCheck("Inventory App Health 2", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/option3", "Inventory application health");
        inventoryHealth2.setApp(inventoryApp);
        healthCheckRepository.save(inventoryHealth2);
        
        HealthCheck orderHealth = new HealthCheck("Order Service Health", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/poc1", "Order service health endpoint");
        orderHealth.setApp(orderService);
        healthCheckRepository.save(orderHealth);

        HealthCheck orderHealth2 = new HealthCheck("Order Service Health 2", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/oc1", "Order service health endpoint");
        orderHealth2.setApp(orderService);
        healthCheckRepository.save(orderHealth2);
        
        // Create sample operations
        Operation getUserOp = new Operation("Get User", "Retrieve user information", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/gis2", Operation.HttpMethod.GET);
        getUserOp.setApp(userService);
        operationRepository.save(getUserOp);
        
        Operation createUserOp = new Operation("Create User", "Create new user account", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/gis1", Operation.HttpMethod.GET);
        createUserOp.setApp(userService);
        operationRepository.save(createUserOp);
        
        Operation processPaymentOp = new Operation("Process Payment", "Process payment transaction", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/rmd3", Operation.HttpMethod.GET);
        processPaymentOp.setApp(paymentService);
        operationRepository.save(processPaymentOp);
        
        Operation getInventoryOp = new Operation("Get Inventory", "Retrieve inventory levels", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/rmd2", Operation.HttpMethod.GET);
        getInventoryOp.setApp(inventoryApp);
        operationRepository.save(getInventoryOp);
        
        Operation createOrderOp = new Operation("Create Order", "Create new order", "https://healthcheckgenerator-b0cb9fb728d6.herokuapp.com/api/health/rmd1", Operation.HttpMethod.GET);
        createOrderOp.setApp(orderService);
        operationRepository.save(createOrderOp);
    }
} 