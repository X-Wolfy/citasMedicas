package com.luis.medicos.mappers;

import org.springframework.stereotype.Component;

import com.luis.commons.dto.MedicoRequest;
import com.luis.commons.dto.MedicoResponse;
import com.luis.commons.enums.EstadoRegistro;
import com.luis.commons.mappers.CommonMapper;
import com.luis.medicos.entities.Medico;

@Component
public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse, Medico>{

	@Override
	public Medico requestToEntity(MedicoRequest request) {
		 if (request == null) return null;
	     return Medico.builder()
	    		 .nombre(request.nombre())
	             .apellidoPaterno(request.apellidoPaterno())
	             .apellidoMaterno(request.apellidoMaterno())
	             .edad(request.edad())
	             .email(request.email())
	             .telefono(request.telefono())
	             .cedulaProfesional(request.cedulaProfesional())
	             .estadoRegistro(EstadoRegistro.ACTIVO)
	             .build();
	}

	@Override
	public MedicoResponse entityToResponse(Medico entity) {
		if(entity == null) return null;
		return new MedicoResponse(
				entity.getId(),
				String.join(" ", entity.getNombre(),
						entity.getApellidoPaterno(),
						entity.getApellidoMaterno()),
				entity.getEdad(),
				entity.getEmail(),
				entity.getTelefono(),
				entity.getCedulaProfesional(),
				entity.getEspecialidad().getDescripcion(),
				entity.getDisponibilidad().getDescripcion()
		);
	}

	@Override
	public Medico updateEntityFromRequest(MedicoRequest request, Medico entity) {
		if (request == null || entity == null) return null;

        entity.setNombre(request.nombre());
        entity.setApellidoPaterno(request.apellidoPaterno());
        entity.setApellidoMaterno(request.apellidoMaterno());
        entity.setEdad(request.edad());
        entity.setEmail(request.email());
        entity.setTelefono(request.telefono());
        entity.setCedulaProfesional(request.cedulaProfesional());

        return entity;
	}
	
}
