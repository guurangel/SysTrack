package com.sys.sys.controllers;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
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

import com.sys.sys.model.Yard;
import com.sys.sys.repository.YardRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("yards")
public class YardController {
    
    @Autowired
    private YardRepository repository;

    @GetMapping
    @Cacheable("yards")
    public List<Yard> index() {
        return repository.findAll();
    }

    @PostMapping
    @CacheEvict(value = "yards", allEntries = true)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Yard create(@RequestBody @Valid Yard yards) {
        System.out.println("Cadastrando pátio: " + yards.getName());
        return repository.save(yards);
    }

    @GetMapping("{id}")
    public ResponseEntity<Yard> get(@PathVariable Long id) {
        return ResponseEntity.ok(getYard(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Yard> delete(@PathVariable Long id) {
        repository.delete(getYard(id));
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("{id}")
    public ResponseEntity<Yard> update(@PathVariable Long id, @RequestBody @Valid Yard yard) {
        getYard(id);
        yard.setId(id);
        repository.save(yard);
        return ResponseEntity.ok(yard);
    }

    private Yard getYard(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pátio não encontrado"));
    }
}
