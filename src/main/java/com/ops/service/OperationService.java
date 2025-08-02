package com.ops.service;

import com.ops.entity.Operation;
import com.ops.repository.OperationRepository;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationService {
    
    @Autowired
    private OperationRepository operationRepository;
    
    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }
    
    public Operation getOperationById(Long id) {
        return operationRepository.findById(id).orElse(null);
    }
    
    public List<Operation> getOperationsByAppId(Long appId) {
        return operationRepository.findByAppId(appId);
    }
    
    public Operation saveOperation(Operation operation) {
        return operationRepository.save(operation);
    }
    
    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }
    
    public String executeOperation(Operation operation) {
        try {
            HttpResponse<JsonNode> response = null;
            
            switch (operation.getMethod()) {
                case GET:
                    response = Unirest.get(operation.getUrl())
                            .header("Content-Type", "application/json")
                            .asJson();
                    break;
                case POST:
                    response = Unirest.post(operation.getUrl())
                            .header("Content-Type", "application/json")
                            .body(operation.getRequestBody())
                            .asJson();
                    break;
                case PUT:
                    response = Unirest.put(operation.getUrl())
                            .header("Content-Type", "application/json")
                            .body(operation.getRequestBody())
                            .asJson();
                    break;
                case DELETE:
                    response = Unirest.delete(operation.getUrl())
                            .header("Content-Type", "application/json")
                            .asJson();
                    break;
                case PATCH:
                    response = Unirest.patch(operation.getUrl())
                            .header("Content-Type", "application/json")
                            .body(operation.getRequestBody())
                            .asJson();
                    break;
            }
            
            if (response != null) {
                return "Status: " + response.getStatus() + "\nResponse: " + response.getBody().toString();
            }
            
            return "Operation executed successfully";
            
        } catch (UnirestException e) {
            return "Error executing operation: " + e.getMessage();
        }
    }
} 