package org.bd.notify.service.impl;

import org.bd.notify.dto.TimeDto;
import org.bd.notify.service.SendTimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@Service
@EnableScheduling
@PropertySource("classpath:application.properties")
public class SendTimeServiceImpl implements SendTimeService {
    private final Logger logger = Logger.getLogger(SendTimeServiceImpl.class.getName());
    private RestTemplate restTemplate;

    public SendTimeServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Value("${matcherUrl}")
    private String matcherUrl;

//    @Scheduled(cron = "0 0 10,18 * * *", zone = "Europe/Moscow")
    @Override
    @Scheduled(fixedDelay = 3500000, initialDelay = 50000)
    public void sendTime() {
        send(getTimeDto());

        logger.info("(OnDelay)Time successfully sent to Matcher at " + Calendar.getInstance(Locale.getDefault()).getTime());
    }

    @Override
    public TimeDto onRequest() {
        String URL = matcherUrl + "/api/getTime/onRequest";
        return restTemplate.postForObject(URL, getTimeDto(), TimeDto.class);
    }

    private TimeDto send(TimeDto timeDto) {
        String URL = matcherUrl + "/api/getTime/currentTime";
        return restTemplate.postForObject(URL, timeDto, TimeDto.class);
    }

    private TimeDto getTimeDto() {
        return new TimeDto(
                Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR),
                parseMonth().get(Calendar.getInstance(Locale.getDefault()).getTime().toString().substring(4, 7)),
                Calendar.getInstance(Locale.getDefault()).get(Calendar.DATE) + "",
                Calendar.getInstance(Locale.getDefault()).getTime().toString().substring(0, 3),
                Calendar.getInstance(Locale.getDefault()).getTime().toString()
        );
    }

    private Map<String, String> parseMonth() {
        Map<String, String> map = new HashMap<>();

        map.put("Jan","??????");
        map.put("Feb","??????");
        map.put("Mar","??????");
        map.put("Apr","??????");
        map.put("May","??????");
        map.put("Jun","??????");
        map.put("Jul","??????");
        map.put("Aug","??????");
        map.put("Sep","??????");
        map.put("Oct","??????");
        map.put("Nov","??????");
        map.put("Dec","??????");

        return map;
    }
}