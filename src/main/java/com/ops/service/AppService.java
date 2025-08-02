package com.ops.service;

import com.ops.entity.App;
import com.ops.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {
    
    @Autowired
    private AppRepository appRepository;
    
    public List<App> getAllApps() {
        return appRepository.findAll();
    }
    
    public App getAppById(Long id) {
        return appRepository.findById(id).orElse(null);
    }
    
    public App getAppWithHealthChecks(Long id) {
        return appRepository.findByIdWithHealthChecks(id);
    }
    
    public App getAppWithOperations(Long id) {
        return appRepository.findByIdWithOperations(id);
    }
    
    public List<App> getAppsByGroupId(Long groupId) {
        return appRepository.findByGroupId(groupId);
    }
    
    public App saveApp(App app) {
        return appRepository.save(app);
    }
    
    public void deleteApp(Long id) {
        appRepository.deleteById(id);
    }
} 