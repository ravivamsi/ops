package com.ops.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "operations")
public class Operation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Operation name is required")
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @NotBlank(message = "Operation URL is required")
    @Column(nullable = false)
    private String url;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HttpMethod method = HttpMethod.GET;
    
    private String requestBody;
    
    private String headers;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id", nullable = false)
    private App app;
    
    // Constructors
    public Operation() {}
    
    public Operation(String name, String description, String url, HttpMethod method) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.method = method;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public HttpMethod getMethod() {
        return method;
    }
    
    public void setMethod(HttpMethod method) {
        this.method = method;
    }
    
    public String getRequestBody() {
        return requestBody;
    }
    
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }
    
    public String getHeaders() {
        return headers;
    }
    
    public void setHeaders(String headers) {
        this.headers = headers;
    }
    
    public App getApp() {
        return app;
    }
    
    public void setApp(App app) {
        this.app = app;
    }
    
    public enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH
    }
} 