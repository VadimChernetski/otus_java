package ru.otus.web.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.dto.ClientDto;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.mapper.ClientMapper;

import java.io.IOException;


public class ClientsApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceClient dbServiceClient;
    private final Gson gson;
    private final ClientMapper clientMapper;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson, ClientMapper clientMapper) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
        this.clientMapper = clientMapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var clientDto = gson.fromJson(req.getReader(), ClientDto.class);
        dbServiceClient.saveClient(clientMapper.toClient(clientDto));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var clients = dbServiceClient.findAll();

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(clients));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}
