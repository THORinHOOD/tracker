package com.lada.tracker.services.models;

import com.lada.tracker.entities.RequestFromWard;
import com.lada.tracker.entities.RequestFromWardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class KanbanColumn {

    private RequestFromWardStatus statusInfo;
    private List<RequestFromWard> requests;

}
