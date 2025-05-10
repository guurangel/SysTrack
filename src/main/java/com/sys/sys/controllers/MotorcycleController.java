package com.sys.sys.controllers;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sys.sys.model.Motorcycle;
import com.sys.sys.repository.MotorcycleRepository;
import com.sys.sys.service.MotorcycleService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("motorcycles")
public class MotorcycleController {
    
    @Autowired
    private MotorcycleService motorcycleService;

    @Autowired
    private MotorcycleRepository repository;

    @GetMapping
    @Cacheable("motorcycles")
    public List<Motorcycle> index() {
        return repository.findAll();
    }

    @PostMapping
    @CacheEvict(value = "motorcycles", allEntries = true)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Motorcycle create(@RequestBody @Valid Motorcycle motorcycle) {
        System.out.println("Cadastrando moto: " + motorcycle.getMarca());
        return motorcycleService.saveMotorcycle(motorcycle);
    }

    @GetMapping("{id}")
    public ResponseEntity<Motorcycle> get(@PathVariable Long id) {
        return ResponseEntity.ok(getMotorcycle(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Motorcycle> delete(@PathVariable Long id) {
        repository.delete(getMotorcycle(id));
        return ResponseEntity.noContent().build();
    }
    
     @PutMapping("{id}")
    public ResponseEntity<Motorcycle> update(@PathVariable Long id, @RequestBody @Valid Motorcycle motorcycle) {
        getMotorcycle(id);
        motorcycle.setId(id);
        return ResponseEntity.ok(motorcycleService.saveMotorcycle(motorcycle));
    }

    private Motorcycle getMotorcycle(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada"));
    }
}