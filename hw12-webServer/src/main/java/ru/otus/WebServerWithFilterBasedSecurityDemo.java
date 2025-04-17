package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DBServiceClientInitializer;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.server.ClientsWebServer;
import ru.otus.server.ClientsWebServerWithFilterBasedSecurity;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

/*
    �������� ��� ���� ������

    // ��������� ��������
    http://localhost:8080

    // �������� ��������
    http://localhost:8080/api/client

    // REST ������ (GET) - �������� ������ ���� ��������
    http://localhost:8080/api/client
    // REST ������ (GET) - �������� ������� �� id
    http://localhost:8080/api/client/*
    // REST ������ (POST) - ������� �������. body: Client
    http://localhost:8080/api/client
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);
        DBServiceClient dbServiceClient = new DBServiceClientInitializer().init(HIBERNATE_CFG_FILE);

        ClientsWebServer clientsWebServer = new ClientsWebServerWithFilterBasedSecurity(
            WEB_SERVER_PORT, authService, dbServiceClient, gson, templateProcessor);

        clientsWebServer.start();
        clientsWebServer.join();
    }
}
