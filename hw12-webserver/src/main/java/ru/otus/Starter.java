package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Admin;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.AdminAuthServiceImpl;
import ru.otus.crm.service.DBServiceAdminImpl;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.mapper.ClientMapperImpl;
import ru.otus.web.server.WebServer;
import ru.otus.web.server.WebServerImpl;
import ru.otus.web.service.TemplateProcessorImpl;

public class Starter {
  public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
  private static final int WEB_SERVER_PORT = 8080;
  private static final String TEMPLATES_DIR = "/templates/";

  public static void main(String[] args) throws Exception {
    var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

    var dbUrl = configuration.getProperty("hibernate.connection.url");
    var dbUserName = configuration.getProperty("hibernate.connection.username");
    var dbPassword = configuration.getProperty("hibernate.connection.password");

    new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

    var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Admin.class, Client.class, Address.class, Phone.class);

    var transactionManager = new TransactionManagerHibernate(sessionFactory);

    var clientTemplate = new DataTemplateHibernate<>(Client.class);

    var adminTemplate = new DataTemplateHibernate<>(Admin.class);

    var clientMapper = new ClientMapperImpl();

    var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate, clientMapper);

    var dbAdminServiceClient = new DBServiceAdminImpl(transactionManager, adminTemplate);

    Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
    var authService = new AdminAuthServiceImpl(dbAdminServiceClient);

    WebServer usersWebServer = new WebServerImpl(WEB_SERVER_PORT,
      authService, dbServiceClient, gson, templateProcessor, clientMapper);

    usersWebServer.start();
    usersWebServer.join();

  }
}
