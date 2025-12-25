package com.example.student_address.repository;

import com.example.student_address.model.Address;
import com.example.student_address.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByStudent(Student student);
}
