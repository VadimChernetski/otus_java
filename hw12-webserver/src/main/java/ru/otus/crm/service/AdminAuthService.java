package ru.otus.crm.service;

public interface AdminAuthService {
    boolean authenticate(String email, String password);
}
