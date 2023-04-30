package ru.otus.crm.service;

public class AdminAuthServiceImpl implements AdminAuthService {

    private final DBServiceAdmin dbServiceAdmin;

    public AdminAuthServiceImpl(DBServiceAdmin dbServiceAdmin) {
        this.dbServiceAdmin = dbServiceAdmin;
    }

    @Override
    public boolean authenticate(String email, String password) {
        return dbServiceAdmin.findByEmail(email)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

}
