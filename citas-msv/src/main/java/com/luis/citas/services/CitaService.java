package com.luis.citas.services;

import com.luis.citas.dto.CitaRequest;
import com.luis.citas.dto.CitaResponse;
import com.luis.commons.services.CrudService;

public interface CitaService extends CrudService<CitaRequest, CitaResponse> {
	CitaResponse obtenerCitaPorIdSinEstado(Long id);
	
	boolean tieneCitasActivas(Long idMedico, Long ignorarCitaId);
	
	CitaResponse cambiarEstado(Long idCita, Long idEstadoCita);
	
	boolean pacienteTieneCitasBloqueantes(Long idPaciente);
}
