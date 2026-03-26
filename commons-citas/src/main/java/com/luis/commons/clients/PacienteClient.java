package com.luis.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.luis.commons.configuration.FeignClientConfig;
import com.luis.commons.dto.PacienteResponse;

@FeignClient(name = "pacientes-msv", configuration = FeignClientConfig.class)
public interface PacienteClient {
	
	@GetMapping("/{id}")
	PacienteResponse obtenerPacientePorId(@PathVariable Long id);
	
	@GetMapping("/id-paciente/{id}")
	PacienteResponse obtenerPacientePorIdSinEstado(@PathVariable Long id);
}
