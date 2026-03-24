package com.luis.medicos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.luis.commons.controllers.CommonController;
import com.luis.commons.dto.MedicoRequest;
import com.luis.commons.dto.MedicoResponse;
import com.luis.medicos.services.MedicoService;

import jakarta.validation.constraints.Positive;

@RestController
@Validated
public class MedicoController extends CommonController<MedicoRequest, MedicoResponse, MedicoService> {

	public MedicoController(MedicoService service) {
		super(service);
	}
	
	@GetMapping("/id-medico/{id}")
	public ResponseEntity<MedicoResponse> obtenerMedicoPorIdSinEstado(
			@PathVariable
			@Positive(message = "El ID debe ser positivo") Long id) {
		return ResponseEntity.ok(service.obtenerMedicoPorIdSinEstado(id));
	}

}
