package com.example.student_address.controller;

import com.example.student_address.dto.CepResponse;
import com.example.student_address.model.Address;
import com.example.student_address.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private AddressService service;

    @GetMapping
    public ResponseEntity<List<Address>> readAll() {
        if (service.readAll().isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(service.readAll());
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<CepResponse> getAddressByCep(@PathVariable String cep) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByCep(cep));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> readById(@PathVariable Long id) {
        return service.readById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{request}")
    public ResponseEntity<Address> create(@PathVariable String request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@PathVariable Long id, @RequestBody Address request) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) throws Exception {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
