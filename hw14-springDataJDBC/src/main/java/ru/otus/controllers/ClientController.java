package ru.otus.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.otus.dto.ClientDto;
import ru.otus.service.ClientService;

import java.util.List;

@Controller
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String getStartPage() {
        return "redirect:/index.html";
    }

    @GetMapping("/client")
    public String getClientsPage() {
        return "/clients.html";
    }

    @PostMapping(path="/api/client")
    @ResponseBody
    public void addClient(@RequestBody ClientDto clientDto) {
        clientService.saveClient(clientDto);
    }

    @GetMapping("/api/client")
    @ResponseBody
    public List<ClientDto> getClients() {
        return clientService.findAll();
    }
}
