package com.ops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_checks")
public class HealthCheck {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Application name is required")
    @Column(nullable = false, length = 255)
    private String name;
    
    @NotBlank(message = "Health check URL is required")
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column(length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthStatus status = HealthStatus.UNKNOWN;
    
    private LocalDateTime lastChecked;
    
    @Column(length = 1000)
    private String lastResponse;
    
    private Integer responseTime;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;
    
    // Constructors
    public HealthCheck() {}
    
    public HealthCheck(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public HealthStatus getStatus() {
        return status;
    }
    
    public void setStatus(HealthStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getLastChecked() {
        return lastChecked;
    }
    
    public void setLastChecked(LocalDateTime lastChecked) {
        this.lastChecked = lastChecked;
    }
    
    public String getLastResponse() {
        return lastResponse;
    }
    
    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }
    
    public Integer getResponseTime() {
        return responseTime;
    }
    
    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }
    
    public App getApp() {
        return app;
    }
    
    public void setApp(App app) {
        this.app = app;
    }
    
    public enum HealthStatus {
        HEALTHY, UNHEALTHY, UNKNOWN
    }
} 