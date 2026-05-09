package com.outforce.desafio.controller;

import com.outforce.desafio.dto.CuponDTO;
import com.outforce.desafio.service.CuponService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cupon")
public class CuponController {

    private final CuponService cuponService;

    @Operation(summary = "Criação de um Cupom")
    @PostMapping
    public ResponseEntity<CuponDTO> postCupon(@Valid @RequestBody CuponDTO cuponDTO) {
        CuponDTO cupon = cuponService.create(cuponDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cupon);
    }

    @Operation(summary = "Buscar um Cupom")
    @GetMapping(value = "/{id}")
    public ResponseEntity<CuponDTO> getCupon(@PathVariable String id) {
        Optional<CuponDTO> cupon = cuponService.get(id);
        return cupon.map(cuponDTO -> ResponseEntity.status(HttpStatus.OK).body(cuponDTO)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @Operation(summary = "Deletar um Cupom")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteCupon(@PathVariable String id) {
        Optional<CuponDTO> cupon = cuponService.delete(id);
        return cupon.map(cuponDTO -> ResponseEntity.status(HttpStatus.NO_CONTENT).build()).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
