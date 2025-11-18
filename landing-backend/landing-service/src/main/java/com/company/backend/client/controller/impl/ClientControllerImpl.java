package com.company.backend.client.controller.impl;


import com.company.backend.client.controller.ClientController;
import com.company.backend.client.dto.ClientDto;
import com.company.backend.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Primary
@RestController
@CrossOrigin("*")
@RequestMapping(path = "api")
public class ClientControllerImpl implements ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientControllerImpl(ClientService clientService)
    {
        this.clientService = clientService;
    }


    @Override
    public ResponseEntity<ClientDto> create(ClientDto clientDto) {
        return new ResponseEntity<>(this.clientService.create(clientDto) , HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(this.clientService.getAllClients());
    }

    @Override
    public ResponseEntity<ClientDto> update(ClientDto clientDto, String clientName) {
        return ResponseEntity.ok(this.clientService.update(clientDto , clientName));
    }

    @Override
    public void delete(String clientName) {
        this.clientService.delete(clientName);
    }
}
