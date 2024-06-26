package com.cotech.helpdesk.service;

import com.cotech.helpdesk.jpa.status.StatusEntity;
import com.cotech.helpdesk.jpa.status.StatusRepository;
import com.cotech.helpdesk.model.ConvertableToEntity;
import com.cotech.helpdesk.model.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatusService implements ConvertableToEntity<Integer, StatusEntity> {

    private final StatusRepository statusRepository;

    @Getter
    private final ModelMapper mapper;

    public ResponseEntity<List<Status>> getStatuses() {
        try {
            List<StatusEntity> entities = this.statusRepository.findAll();
            List<Status> statusList = new ArrayList<>();
            for (StatusEntity entity : entities) {
                statusList.add(this.getMapper().map(entity, Status.class));
            }
            log.trace("Returning statuses");
            return ResponseEntity.ok(statusList);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to get status");
        }
    }

    @Override
    public StatusEntity findEntityById(Integer id) {
        if (id == null) {
            return null;
        }
        return this.statusRepository.findById(id).orElse(null);
    }
}
