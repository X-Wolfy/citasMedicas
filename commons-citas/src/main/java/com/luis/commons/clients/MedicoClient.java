package com.luis.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.luis.commons.dto.MedicoResponse;

@FeignClient(name = "medicos-msv")
public interface MedicoClient {

	@GetMapping("/{id}")
	MedicoResponse obtenerMedicoPorId(@PathVariable Long id);
	
	@GetMapping("/id-medico/{id}")
	MedicoResponse obtenerMedicoPorIdSinEstado(@PathVariable Long id);
}
