package com.example.student_address.service;

import ch.qos.logback.core.util.StringUtil;
import com.example.student_address.dto.CepResponse;
import com.example.student_address.model.Address;
import com.example.student_address.model.Student;
import com.example.student_address.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {
    @Autowired
    private AddressRepository repository;

    @Autowired
    private WebClient webClient;

    private final String URL = "https://viacep.com.br/ws/";

    public List<Address> readAll() {
        return repository.findAll();
    }

    public CepResponse findByCep(String cep) {
        if (StringUtil.isNullOrEmpty(cep))
            throw new IllegalArgumentException("CEP informado é nulo ou vazio");

        try {
            var response = webClient.get().uri(URL + cep + "/json/").retrieve().toEntity(CepResponse.class).block();
            if (response == null)
                throw new RuntimeException("Resposta nula");

            return response.getBody();
        } catch (HttpClientErrorException exception) {
            throw new RuntimeException(
                    String.format("CEP inválido ou não encontrado: %s\nErro: %s", cep, exception.getMessage()));
        }
    }

    public Optional<Address> readById(Long id) {
        return repository.findById(id);
    }

    public Address create(String request) throws Exception {
        var response = findByCep(request);

        var address = new Address(response);

        var studentDetails = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var studentId = studentDetails.getId();

        var student = repository.findById(studentId).orElseThrow(() -> new Exception("Nenhum student encontrado"));

        address.setStudent(student.getStudent());

        return repository.save(address);
    }

    public Address update(Long id, Address request) throws Exception {
        repository.findById(id).map(a -> {
            a.setCity(request.getCity());
            a.setState(request.getState());
            a.setStreet(request.getStreet());
            a.setZipCode(request.getZipCode());
            return repository.save(a);
        }).orElseThrow(() -> new Exception("Address não encontrado"));

        return request;
    }

    public void deleteById(Long id) throws Exception {
        if (!repository.existsById(id))
            throw new Exception("Address não encontrado");

        repository.deleteById(id);
    }
}


