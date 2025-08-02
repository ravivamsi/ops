package com.ops.service;

import com.ops.entity.Group;
import com.ops.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
    
    public Group getGroupById(Long id) {
        return groupRepository.findById(id).orElse(null);
    }
    
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }
    
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
    
    public Optional<Group> findByName(String name) {
        return groupRepository.findByName(name);
    }
    
    public boolean existsByName(String name) {
        return groupRepository.existsByName(name);
    }
} 