package com.company.backend.client.service.impl;

import com.company.backend.client.dto.ClientDto;
import com.company.backend.client.model.Client;
import com.company.backend.client.repository.ClientRepository;
import com.company.backend.client.service.ClientService;
import com.company.backend.utilty.MapperUtility;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository)
    {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public ClientDto create(ClientDto clientDto) {
        log.info("Inside @class ClientServiceImpl @method create  @Param clientDto :{}", clientDto);
        try {

            Client client = MapperUtility.sourceToTarget(clientDto, Client.class);
            client = this.clientRepository.saveAndFlush(client);
            log.info("Our Client data sent to the database and stored successfully");
            return MapperUtility.sourceToTarget(client,  ClientDto.class, "uuid");
        } catch (Exception e) {
            throw new RuntimeException("Error to map client :" + clientDto.getUuid());
        }
    }

    @Override
    public List<ClientDto> getAllClients() {
        log.info("Inside @class ClientServiceImpl @method getAllClients ");
        List<Client> all = this.clientRepository.findAll();
        return all.stream().map(client -> {
            try {
                return  MapperUtility.sourceToTarget(client, ClientDto.class);
            } catch (Exception e) {
                throw new RuntimeException("Mapping error for client: " , e);
            }
        }).toList();
    }

    @Override
    @Transactional
    public ClientDto update(ClientDto clientDto, String name) {
        log.info("Inside @class ClientServiceImpl @method update  @Param clientDto :{} clientName :{}", clientDto,name);
        try {
            Client client = this.clientRepository.findByName(name).orElseThrow(() -> new RuntimeException("Client can not found"));
            client.setName(clientDto.getName());
            client.setDescription(clientDto.getDescription());
            client.setDesignation(clientDto.getDesignation());
            client = this.clientRepository.saveAndFlush(client);
            log.info("Client updated successfully  and stored to database...");
            return MapperUtility.sourceToTarget(client, ClientDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void delete(String clientName) {
        log.info("Inside @class ClientServiceImpl @method delete  @Param  clientName :{}", clientName);
        try {
            Client client = this.clientRepository.findByName(clientName).orElseThrow(() -> new RuntimeException("Client can not found"));
            this.clientRepository.delete(client);
        } catch (Exception e) {
            throw new RuntimeException("Client deletion is  not  possible...");
        }

    }
}
