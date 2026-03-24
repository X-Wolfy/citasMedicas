package com.luis.commons.controllers;

import com.luis.commons.services.CrudService;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
public class CommonController<RQ, RS, S extends CrudService<RQ, RS>> {
    protected final S service;

    @GetMapping
    public ResponseEntity<List<RS>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RS> obtenerPorId(
            @PathVariable @Positive(message="El Id debe ser positivo") Long id ){
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<RS> registrar(@Validated @RequestBody RQ request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RS> actualizar(
            @PathVariable @Positive(message="El Id debe ser positivo") Long id,
            @Validated @RequestBody RQ request){
        return ResponseEntity.ok(service.actualizar(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive(message="El Id debe ser positivo") Long id ){
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
