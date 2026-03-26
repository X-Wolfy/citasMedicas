package com.luis.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.luis.commons.configuration.FeignClientConfig;
import com.luis.commons.dto.MedicoResponse;

@FeignClient(name = "medicos-msv", configuration = FeignClientConfig.class)
public interface MedicoClient {

	@GetMapping("/{id}")
	MedicoResponse obtenerMedicoPorId(@PathVariable Long id);
	
	@GetMapping("/id-medico/{id}")
	MedicoResponse obtenerMedicoPorIdSinEstado(@PathVariable Long id);
	
	@PutMapping("/{idMedico}/disponibilidad/{idDisponibilidad}")
	MedicoResponse cambiarDisponibilidad(
			@PathVariable Long idMedico,
			@PathVariable("idDisponibilidad") Long Disponibilidad,
			@RequestParam(required = false) Long contextoCitaId
			);
}
