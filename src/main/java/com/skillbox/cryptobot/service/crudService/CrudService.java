package com.skillbox.cryptobot.service.crudService;

public interface CrudService {
    void createUser(Long tId, Double price);
    void updateUser(Long tId, Double price);
}
