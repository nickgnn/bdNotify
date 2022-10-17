package org.bd.notify.service.impl;

import org.bd.notify.dto.TimeDto;
import org.bd.notify.dto.User;
import org.bd.notify.service.TimeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.logging.Logger;

@Service
public class TimeServiceImpl implements TimeService {
    private final Logger logger = Logger.getLogger(TimeServiceImpl.class.getName());
    private RestTemplate restTemplate;

    public TimeServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Value("${firstControlTime}")
    private String firstControlTime;

    @Value("${secondControlTime}")
    private String secondControlTime;

    @Value("${botUrl}")
    private String botUrl;

    @Value("${crudUrl}")
    private String crudUrl;

    @Override
    public String compareOnTime(TimeDto timeDto) {
        if (isTime(timeDto.getTime().substring(11, 13))) {
            return sendMessageToTg(getResult(timeDto));
        } else {
            logger.info("Time hasn't come");
            return sendMessageToTg("Time hasn't come");
        }
    }

    @Override
    public String compareOnRequest(TimeDto timeDto) {
        logger.info("(OnRequest)");
        return sendMessageToTg(getResult(timeDto));
    }

    private boolean isTime(String time) {
        return time.equals(firstControlTime) | time.equals(secondControlTime);
    }

    private String isBirthDay(Iterable<User> users, TimeDto timeDto) {
        Iterator<User> iterator = users.iterator();

        //Отсев по месяцам
        while (iterator.hasNext()) {
            if (!iterator.next().getMonth().equals(timeDto.getMonth())) {
                iterator.remove();
            }
        }

        //Отсев по числу месяца
        iterator = users.iterator();
        while (iterator.hasNext()) {
            if (!String.valueOf(iterator.next().getDay()).equals(timeDto.getDayOfMonth())) {
                iterator.remove();
            }
        }

        //Преобразование в ArrayList
        ArrayList<User> list = (ArrayList<User>)users;

        //Проверка на наличие ДР (если лист пустой, то сегодня ни у кого нет ДР)
        if (list.isEmpty()) {
            return "Сегодня ни у кого нет ДР :(";
        } else {
            return concatResult(list, timeDto);
        }
    }

    private String concatResult(ArrayList<User> list, TimeDto timeDto) {
        StringBuilder stringBuilder = new StringBuilder("Сегодня ДР у ");

        for (User user : list) {
            stringBuilder
                    .append(user.getName())
                    .append(" ").append(user.getYear())
                    .append(" ").append(user.getMonth())
                    .append(" ").append(user.getDay())
                    .append(", исполняется")
                    .append(" ").append(timeDto.getYear() - user.getYear()).append(" годиков;")
                    .append("\n");
        }

        return stringBuilder.toString();
    }

    private String getResult(TimeDto timeDto) {
        List<User> allUsers = getAllUsers();

        logger.info("Users successfully received from DB at " + Calendar.getInstance(Locale.getDefault()).getTime());

        String result = isBirthDay(allUsers, timeDto);
        logger.info(result);

        return result;
    }

    private String sendMessageToTg(String message) {
        String URL = botUrl + "api/telegramChat/sendMessage";
        return restTemplate.postForObject(URL, message, String.class);
    }

    private List<User> getAllUsers() {
        List<User> body = restTemplate.exchange(
                crudUrl + "/api/users/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                }
        ).getBody();

        return body.size()!=0 ? body : null;
    }

    private Map<String, String> parseMonth() {
        Map<String, String> map = new HashMap<>();

        map.put("Янв","Jan");
        map.put("Фев","Feb");
        map.put("Мар","Mar");
        map.put("Апр","Apr");
        map.put("Май","May");
        map.put("Июн","Jun");
        map.put("Июл","Jul");
        map.put("Авг","Aug");
        map.put("Сен","Sep");
        map.put("Окт","Oct");
        map.put("Ноя","Nov");
        map.put("Дек","Dec");

        return map;
    }
}