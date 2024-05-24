package com.cotech.helpdesk.service;

import com.cotech.helpdesk.jpa.priority.PriorityEntity;
import com.cotech.helpdesk.jpa.priority.PriorityRepository;
import com.cotech.helpdesk.model.Priority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriorityService extends BaseService {

    private final PriorityRepository priorityRepository;

    public List<Priority> getPriorities() {
        List<PriorityEntity> entities = this.priorityRepository.findAll();
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }
        List<Priority> priorities = new ArrayList<>();
        for (PriorityEntity entity : entities) {
            priorities.add(this.getMapper().map(entity, Priority.class));
        }
        log.trace("Returning priorities");
        return priorities;
    }
}