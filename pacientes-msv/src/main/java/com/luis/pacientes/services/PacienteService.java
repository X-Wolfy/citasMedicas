package com.luis.pacientes.services;

import com.luis.commons.dto.PacienteRequest;
import com.luis.commons.dto.PacienteResponse;
import com.luis.commons.services.CrudService;

public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse>{
	PacienteResponse obtenerPacientePorIdSinEstado(Long id);
}
