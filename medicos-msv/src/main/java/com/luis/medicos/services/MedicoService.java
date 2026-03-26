package com.luis.medicos.services;

import com.luis.commons.dto.MedicoRequest;
import com.luis.commons.dto.MedicoResponse;
import com.luis.commons.services.CrudService;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {
	MedicoResponse obtenerMedicoPorIdSinEstado(Long id);
	
	MedicoResponse cambiarDisponibilidad(Long idMedico, Long idDisponibilidad, Long contextoCitaId);
}
