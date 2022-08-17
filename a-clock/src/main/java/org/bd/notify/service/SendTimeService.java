package org.bd.notify.service;

import org.bd.notify.dto.TimeDto;

public interface SendTimeService {
    void sendTime();
    TimeDto onRequest();
}