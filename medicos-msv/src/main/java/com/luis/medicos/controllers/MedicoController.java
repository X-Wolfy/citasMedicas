package com.luis.medicos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@PutMapping("/{idMedico}/disponibilidad/{idDisponibilidad}")
	public ResponseEntity<MedicoResponse> cambiarDisponibilidad(
			@PathVariable @Positive(message = "El ID del médico debe ser positivo") Long idMedico,
			@PathVariable @Positive(message = "El ID de disponibilidad debe ser positivo") Long idDisponibilidad,
			@RequestParam(required = false) Long contextoCitaId) {
		return ResponseEntity.ok(service.cambiarDisponibilidad(idMedico, idDisponibilidad, contextoCitaId));
	}

}
