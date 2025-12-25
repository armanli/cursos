package com.example.student.service;

import com.example.student.entitiy.Student;
import com.example.student.repository.StudentRepository;
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
            s.setNome(request.getNome());
            s.setEmail(request.getEmail());
            s.setIdade(request.getIdade());
            return repository.save(s);
        }).orElseThrow(() -> new Exception("Studant não encontrado"));

        return request;
    }

    public void deleteById(Long id) throws Exception {
        if (!repository.existsById(id))
            throw new Exception("Student não encontrado");

        repository.deleteById(id);
    }
}

