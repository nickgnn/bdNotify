package org.bd.notify.service;

import org.bd.notify.dto.TimeDto;

public interface TimeService {
    String compareOnTime(TimeDto timeDto);
    String compareOnRequest(TimeDto timeDto);
}