package ru.otus.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;

@SuppressWarnings({"java:S1989"})
public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final transient DBServiceClient dbServiceClient;
    private final transient Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<Client> clients = new ArrayList<>();
        if (req.getPathInfo() != null) {
            Optional<Client> optionalClient = dbServiceClient.getClient(extractIdFromRequest(req));
            optionalClient.ifPresent(clients::add);
        } else {
            clients.addAll(dbServiceClient.findAll());
        }
        clients.forEach(client -> client.getPhones().forEach(phone -> phone.setClient(null)));
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = extractClientFromRequest(request);
        Client saved = dbServiceClient.saveClient(client);
        response.setStatus(SC_CREATED);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(new Id(saved.getId())));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1) ? path[ID_PATH_PARAM_POSITION] : String.valueOf(-1);
        return Long.parseLong(id);
    }

    @SneakyThrows
    private Client extractClientFromRequest(HttpServletRequest request) {
        String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        JsonObject json = gson.fromJson(test, JsonObject.class);
        String name = getStringFromJson(json, "name");
        Address address = new Address(null, getStringFromJson(json, "address"));
        List<Phone> phones = Arrays.stream(getStringFromJson(json, "phones").split("\\s*,\\s*"))
            .map(phone -> new Phone(null, phone)).toList();

        return new Client(null, name, address, phones);
    }

    private String getStringFromJson(JsonObject json, String nodeName) {
        return json.get(nodeName).getAsString().trim();
    }

    private record Id(long id) {
    }
}
