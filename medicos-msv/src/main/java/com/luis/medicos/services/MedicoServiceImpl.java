package com.luis.medicos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.commons.dto.MedicoRequest;
import com.luis.commons.dto.MedicoResponse;
import com.luis.commons.enums.DisponibilidadMedico;
import com.luis.commons.enums.EspecialidadMedico;
import com.luis.commons.enums.EstadoRegistro;
import com.luis.commons.exceptions.RecursoNoEncontradoException;
import com.luis.medicos.entities.Medico;
import com.luis.medicos.mappers.MedicoMapper;
import com.luis.medicos.repositories.MedicoRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class MedicoServiceImpl implements MedicoService{

	private final MedicoRepository medicoRepository;
	private final MedicoMapper medicoMapper;
	
	@Override
	@Transactional(readOnly = true)
	public List<MedicoResponse> listar() {
		log.info("Listado de todos los medicos activos solicitados");
		return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(medicoMapper::entityToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public MedicoResponse obtenerPorId(Long id) {
		return medicoMapper.entityToResponse(obtenerMedicoOException(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public MedicoResponse obtenerMedicoPorIdSinEstado(Long id) {
		log.info("Buscando Medico sin estado con id: {}", id);
		return medicoMapper.entityToResponse(medicoRepository.findById(id).orElseThrow(() ->
			new RecursoNoEncontradoException("Medico sin estado no encontrado con id: " + id)));
	}

	@Override
	public MedicoResponse registrar(MedicoRequest request) {
		log.info("Registrando nuevo Medico");
		
		validarEmailUnico(request.email());
		validarTelefonoUnico(request.telefono());
		validarCedulaUnica(request.cedulaProfesional());
		
		Medico medico = medicoMapper.requestToEntity(request);
		
		medico.setEspecialidad(EspecialidadMedico.fromCodigo(request.idEspecialidad()));
		medico.setDisponibilidad(DisponibilidadMedico.DISPONIBLE);
		
		medicoRepository.save(medico);
		
		log.info("Medico registrado con exito: {}", medico.getNombre());
		return medicoMapper.entityToResponse(medico);
	}

	@Override
	public MedicoResponse actualizar(MedicoRequest request, Long id) {
		Medico medico = obtenerMedicoOException(id);
		
		log.info("Actualizando Medico con id: {}", id);
		
		validarCambiosUnicos(request, id);
		
		medicoMapper.updateEntityFromRequest(request, medico);
		
		medico.setEspecialidad(EspecialidadMedico.fromCodigo(request.idEspecialidad()));
		log.info("Medico actualizado con exito: {}", id);
		return medicoMapper.entityToResponse(medico);
	}

	@Override
	public void eliminar(Long id) {
		Medico medico = obtenerMedicoOException(id);
		
		medico.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		
		log.info("Medico con id {} ha sido marcadocomo eliminado", id);
		
	}
	
	private Medico obtenerMedicoOException(Long id) {
		log.info("Buscando Medico activo con id: {}", id);
		
		return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() -> 
			new RecursoNoEncontradoException("Medico activo no encontrado con id: " + id));
	}
	
	private void validarEmailUnico(String email) {
		log.info("Validando email unico...");
		if(medicoRepository.existsByEmailAndEstadoRegistro(email.toLowerCase(), EstadoRegistro.ACTIVO)) {
			throw new IllegalArgumentException("Ya existe un Medico registrado con el email: " + email);
		}
	}
	
	private void validarTelefonoUnico(String telefono) {
		log.info("Validando telefono unico...");
		if(medicoRepository.existsByTelefonoAndEstadoRegistro(telefono, EstadoRegistro.ACTIVO)) {
			throw new IllegalArgumentException("Ya existe un Medico registrado con el telefono: " + telefono);
		}
	}
	
	private void validarCedulaUnica(String cedula) {
		log.info("Validando cedula unico...");
		if(medicoRepository.existsByCedulaProfesionalAndEstadoRegistro(cedula, EstadoRegistro.ACTIVO)) {
			throw new IllegalArgumentException("Ya existe un Medico registrado con la cedula: " + cedula);
		}
	}
	
	private void validarCambiosUnicos(MedicoRequest request, Long id) {
		if(medicoRepository.existsByEmailAndEstadoRegistroAndIdNot(request.email().toLowerCase(), EstadoRegistro.ACTIVO, id)) {
			throw new IllegalArgumentException("Ya existe un Paciente registrado con el email: " + request.email());
		}
		
		if(medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(request.telefono(), EstadoRegistro.ACTIVO, id)) {
			throw new IllegalArgumentException("Ya existe un Paciente registrado con el telefono: " + request.telefono());
		}
		
		if(medicoRepository.existsByCedulaProfesionalAndIdNotAndEstadoRegistro(request.cedulaProfesional(), id, EstadoRegistro.ACTIVO)) {
			throw new IllegalArgumentException("Ya existe un Paciente registrado con la cedula profesional: " + request.cedulaProfesional());
		}
		
	}

}
