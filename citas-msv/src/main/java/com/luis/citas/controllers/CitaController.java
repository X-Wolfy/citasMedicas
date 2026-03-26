package com.luis.citas.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.luis.citas.dto.CitaRequest;
import com.luis.citas.dto.CitaResponse;
import com.luis.citas.services.CitaService;
import com.luis.commons.controllers.CommonController;

import jakarta.validation.constraints.Positive;

@RestController
@Validated
public class CitaController extends CommonController<CitaRequest, CitaResponse, CitaService>{

	public CitaController(CitaService service) {
		super(service);
	}
	
	@GetMapping("/id-cita/{id}")
	public ResponseEntity<CitaResponse> obtenerCitaPorIdSinEstado(
			@PathVariable
			@Positive(message = "El id debe ser positivo") Long id) {
		return ResponseEntity.ok(service.obtenerCitaPorIdSinEstado(id));
	}
	
	@PatchMapping("/{idCita}/estado/{idEstado}")
	public ResponseEntity<CitaResponse> cambiarEstado(
			@PathVariable @Positive(message = "El id de la cita debe ser positivo") Long idCita,
			@PathVariable @Positive(message = "El id del estado debe ser positivo") Long idEstado) {
		return ResponseEntity.ok(service.cambiarEstado(idCita, idEstado));
	}
	
	@GetMapping("/medico/{idMedico}/tiene-citas-activas")
	public ResponseEntity<Boolean> tieneCitasActivas(
			@PathVariable @Positive(message = "El id del medico debe ser positivo") Long idMedico,
			@RequestParam(required = false) Long ignorarCitaId) {
		return ResponseEntity.ok(service.tieneCitasActivas(idMedico, ignorarCitaId));
	}
	
	@GetMapping("/paciente/{idPaciente}/tiene-citas-bloqueantes")
	public ResponseEntity<Boolean> pacienteTieneCitaBloqueantes(
			@PathVariable @Positive(message = "El id del paciente debe ser positivo") Long idPaciente) {
		return ResponseEntity.ok(service.pacienteTieneCitasBloqueantes(idPaciente));
	}

}
