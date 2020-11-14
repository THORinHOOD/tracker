package com.lada.tracker.services.models;

import com.lada.tracker.entities.Request;
import com.lada.tracker.entities.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class KanbanColumn {

    private RequestStatus statusInfo;
    private List<Request> requests;

}
