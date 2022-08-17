package org.bd.notify.service;

import org.bd.notify.dto.TimeDto;
import org.bd.notify.exception.DBException;

public interface TimeService {
    String compareTime(TimeDto timeDto) throws DBException;
    String compareOnRequest(TimeDto timeDto) throws DBException;
}