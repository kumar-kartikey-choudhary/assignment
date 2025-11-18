package com.company.backend.contact.controller;

import com.company.backend.contact.dto.ContactDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ResponseBody
public interface ContactController {

    @PostMapping(path = "contact")
    ResponseEntity<ContactDto> save(@RequestBody ContactDto contactDto);

    @GetMapping(path = "admin/contacts")
    ResponseEntity<List<ContactDto>> getAllContact();

}
