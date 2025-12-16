package com.example.student_address.model;

import com.example.student_address.dto.CepResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String state;
    private String street;
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    public Address(CepResponse response) {
        this.city = response.localidade();
        this.state = response.uf();
        this.street = response.logradouro();
        this.zipCode = response.cep();
    }

}
