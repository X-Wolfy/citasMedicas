package com.luis.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.luis.commons.configuration.FeignClientConfig;

@FeignClient(name = "citas-msv", configuration = FeignClientConfig.class)
public interface CitaClient {
	
	@GetMapping("/medico/{idMedico}/tiene-citas-activas")
	boolean tieneCitasActivas(
			@PathVariable Long idMedico,
			@RequestParam(required = false) Long ignorarCitaId);
	
	@GetMapping("/paciente/{idPaciente}/tiene-citas-bloqueantes")
	boolean pacienteTieneCitasBloqueantes(@PathVariable Long idPaciente);
}
