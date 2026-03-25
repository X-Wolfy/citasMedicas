package com.luis.citas.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.citas.dto.CitaRequest;
import com.luis.citas.dto.CitaResponse;
import com.luis.citas.entities.Cita;
import com.luis.citas.enums.EstadoCita;
import com.luis.citas.mappers.CitaMapper;
import com.luis.citas.repositories.CitaRepository;
import com.luis.commons.clients.MedicoClient;
import com.luis.commons.clients.PacienteClient;
import com.luis.commons.dto.MedicoResponse;
import com.luis.commons.dto.PacienteResponse;
import com.luis.commons.enums.EstadoRegistro;
import com.luis.commons.exceptions.RecursoNoEncontradoException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CitaServiceImpl implements CitaService {
	private final CitaRepository citaRepository;
	private final CitaMapper citaMapper;
	private final PacienteClient pacienteClient;
	private final MedicoClient medicoClient;
	
	@Override
	@Transactional(readOnly = true)
	public List<CitaResponse> listar() {
		log.info("Listado de todas las citas activas solicitadas");
		return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(cita -> 
				citaMapper.entityToResponse(
						cita, 
						obtenerPacienteResponseSinEstado(cita.getIdPaciente()),
						obtenerMedicoResponseSinEstado(cita.getIdMedico()))
				).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CitaResponse obtenerPorId(Long id) {
		Cita cita = obtenerCitaOException(id);
		return citaMapper.entityToResponse(
				cita, 
				obtenerPacienteResponseSinEstado(cita.getIdPaciente()),
				obtenerMedicoResponseSinEstado(cita.getIdMedico()));
	}
	
	@Override
	@Transactional(readOnly = true)
	public CitaResponse obtenerCitaPorIdSinEstado(Long id) {
		log.info("Buscando Cita sin estado con id: {}", id);
		
		Cita cita = citaRepository.findById(id).orElseThrow(() ->
				new RecursoNoEncontradoException("Cita sin estado no encontrada con id: " + id));
		
		return citaMapper.entityToResponse(
				cita, 
				obtenerPacienteResponseSinEstado(cita.getIdPaciente()),
				obtenerMedicoResponseSinEstado(cita.getIdMedico()));
	}
	
	@Override
	public CitaResponse registrar(CitaRequest request) {
		log.info("Registrando nueva Cita: {}", request);
		
		// Validar que existan paciente y medico
		PacienteResponse paciente = obtenerPacienteResponse(request.idPaciente());
		MedicoResponse medico = obtenerMedicoResponse(request.idMedico());
		
		Cita cita = citaRepository.save(citaMapper.requestToEntity(request));
		
		log.info("Cita registrada exitosamente: {}", cita);
		return citaMapper.entityToResponse(cita, paciente, medico);
	}
	
	@Override
	public CitaResponse actualizar(CitaRequest request, Long id) {
		Cita cita = obtenerCitaOException(id);
		log.info("Actualizando Cita con id: {}", id);
		
		// Validar que exista Paciente y Medico
		PacienteResponse paciente = obtenerPacienteResponse(request.idPaciente());
		MedicoResponse medico = obtenerMedicoResponse(request.idMedico());
		
		EstadoCita estadoNuevo = EstadoCita.fromCodigo(request.idEstadoCita());
		
		citaMapper.updateEntityFromRequest(request, cita, estadoNuevo);
		
		log.info("Cita actualizada con id: {}", id);
		return citaMapper.entityToResponse(cita, paciente, medico);
	}
	
	@Override
	public void eliminar(Long id) {
		Cita cita = obtenerCitaOException(id);
		log.info("Eliminando Cita con id: {}", id);
		
		validarEstadoCitaAlEliminar(cita);
		
		//Cambiar disponibilidad del medico a disponible solo si esta en pendiente
		
		cita.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		log.info("Cita con id {} ha sido marcada como eliminada", id);
	}
	
	private Cita obtenerCitaOException(Long id) {
		log.info("Buscando Cita activa con id: {}", id);
		
		return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() ->
				new RecursoNoEncontradoException("Cita activa no encontrada con id: " + id));
	}

	private void validarEstadoCitaAlEliminar(Cita cita) {
		if(cita.getEstadoCita() == EstadoCita.CONFIRMADA || cita.getEstadoCita() == EstadoCita.EN_CURSO) {
			throw new IllegalStateException("No se puede eliminar una cita " +
					EstadoCita.CONFIRMADA.getDescripcion() + " o "
					+ EstadoCita.EN_CURSO.getDescripcion());
		}
	}
	
	private PacienteResponse obtenerPacienteResponse(Long idPaciente) {
		return pacienteClient.obtenerPacientePorId(idPaciente);
	}
	
	private PacienteResponse obtenerPacienteResponseSinEstado(Long idPaciente) {
		return pacienteClient.obtenerPacientePorIdSinEstado(idPaciente);
	}
	
	private MedicoResponse obtenerMedicoResponse(Long idMedico) {
		return medicoClient.obtenerMedicoPorId(idMedico);
	}
	
	private MedicoResponse obtenerMedicoResponseSinEstado(Long idMedico) {
		return medicoClient.obtenerMedicoPorIdSinEstado(idMedico);
	}

}
