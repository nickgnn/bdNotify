package org.bd.notify.service.impl;

import org.bd.notify.entity.User;
import org.bd.notify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private RestTemplate restTemplate;
    private String crudUrl;

    @Autowired
    public UserServiceImpl(RestTemplateBuilder restTemplateBuilder, @Value("${crudUrl}") String crudUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.crudUrl = crudUrl;
    }

    @Override
    public String getAllUsers() {
        List<User> body = restTemplate.exchange(
                crudUrl + "/api/users/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {
                }
        ).getBody();

        return body.size()!=0 ? concatResult(body) : "У тебя нету дружочков, балбес :)";
    }

    @Override
    public User getOneUser(Long id) {
        return restTemplate.exchange(
                crudUrl + "/api/users" + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<User>() {
                }
        ).getBody();
    }

    @Override
    public User createUser(User user) {
        return restTemplate.postForObject(
                crudUrl + "/api/users/createUser",
                new HttpEntity<>(user),
                User.class);
    }

    @Override
    public User updateUser(Long id, User user) {
        restTemplate.put(crudUrl + "/api/users" + id, user);

        return getOneUser(id);
    }

    @Override
    public void deleteUser(Long id) {
        restTemplate.delete(crudUrl + "/api/users/" + id);
    }

    @Override
    public String getBirthday(String url) {
        String URL = url + "api/seebd/ask";
        return restTemplate.getForObject(URL, String.class);
    }

    private String concatResult(List<User> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (User user : list) {
            stringBuilder
                    .append("{").append(user.getId()).append("} ")
                    .append(" ").append(user.getName())
                    .append(" ").append(user.getYear())
                    .append(", ").append(user.getMonth())
                    .append(" ").append(user.getDay()).append(",")
                    .append(" ").append(Calendar.getInstance(Locale.getDefault()).get(Calendar.YEAR) - user.getYear()).append(" годиков;")
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}