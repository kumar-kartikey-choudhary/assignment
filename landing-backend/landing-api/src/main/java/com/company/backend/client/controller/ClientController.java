package com.company.backend.client.controller;

import com.company.backend.client.dto.ClientDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ResponseBody
public interface ClientController {

    @PostMapping(path = "admin/client")
    ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto);

    @GetMapping(path = "clients")
    ResponseEntity<List<ClientDto>> getAllClients();

    @PutMapping(path = "admin/client/{clientName}")
    ResponseEntity<ClientDto> update(@RequestBody ClientDto clientDto , @PathVariable(name = "clientName") String clientName);

    @DeleteMapping(path = "admin/client/{clientName}")
    void delete(@PathVariable(name = "clientName") String clientName);
}
