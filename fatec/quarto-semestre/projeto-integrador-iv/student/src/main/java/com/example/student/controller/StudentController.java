package com.example.student.controller;

import com.example.student.entitiy.Student;
import com.example.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public ResponseEntity<List<Student>> readAll() {
        if (service.readAll().isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(service.readAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> readById(@PathVariable Long id) {
        return service.readById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student request) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student request) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) throws Exception {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

