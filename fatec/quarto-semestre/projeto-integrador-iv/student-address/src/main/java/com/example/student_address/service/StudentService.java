package com.example.student_address.service;

import ch.qos.logback.core.util.StringUtil;
import com.example.student_address.model.Student;
import com.example.student_address.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;

    public List<Student> readAll() {
        return repository.findAll();
    }

    public Optional<Student> readById(Long id) {
        return repository.findById(id);
    }

    public Student create(Student request) throws Exception {
        if (repository.findByEmail(request.getEmail()).isPresent())
            throw new Exception("Email já cadastrado");

        return repository.save(request);
    }

    public Student update(Long id, Student request) throws Exception {
        repository.findById(id).map(s -> {
            s.setFirstName(request.getFirstName());
            s.setLastName(request.getLastName());
            s.setEmail(request.getEmail());
            return repository.save(s);
        }).orElseThrow(() -> new Exception("Studant não encontrado"));

        return request;
    }

    public void deleteById(Long id) throws Exception {
        if (!repository.existsById(id))
            throw new Exception("Student não encontrado");

        repository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        if (StringUtil.isNullOrEmpty(email))
            return false;
        return repository.existsByEmail(email);
    }
}


