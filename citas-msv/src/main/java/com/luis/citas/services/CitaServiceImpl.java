package com.luis.citas.services;

import java.util.Arrays;
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
import com.luis.commons.enums.DisponibilidadMedico;
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
	@Transactional(readOnly = true)
	public boolean tieneCitasActivas(Long idMedico, Long ignorarCitaId) {
		log.info("Verificando si el medico {} tiene citas activas. Ignorando cita: {}", idMedico, ignorarCitaId);
		List<EstadoCita> estadosActivos = Arrays.asList(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);
		if (ignorarCitaId != null) {
			return citaRepository.existsByIdMedicoAndEstadoCitaInAndEstadoRegistroAndIdNot(idMedico, estadosActivos, EstadoRegistro.ACTIVO, ignorarCitaId);
		}
		return citaRepository.existsByIdMedicoAndEstadoCitaInAndEstadoRegistro(idMedico, estadosActivos, EstadoRegistro.ACTIVO);
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean pacienteTieneCitasBloqueantes(Long idPaciente) {
		log.info("Verificando si el paciente {} tiene citas CONFIRMADAS o EN_CURSO", idPaciente);
		List<EstadoCita> estadosBloqueantes = Arrays.asList(EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);
		return citaRepository.existsByIdPacienteAndEstadoCitaInAndEstadoRegistro(idPaciente, estadosBloqueantes, EstadoRegistro.ACTIVO);
	}

	@Override
	public CitaResponse cambiarEstado(Long idCita, Long idEstadoCita) {
		Cita cita = obtenerCitaOException(idCita);
		EstadoCita estadoCita = EstadoCita.fromCodigo(idEstadoCita);
		
		log.info("Intentando cambiar estao de cita {} de {} a {}", idCita, cita.getEstadoCita(), estadoCita);
		
		validarTransicionEstado(cita.getEstadoCita(), estadoCita);
		
		cita.setEstadoCita(estadoCita);
		sincronizarDisponibilidadMedico(cita.getIdMedico(), estadoCita, cita.getId());
		
		return citaMapper.entityToResponse(cita, obtenerPacienteResponseSinEstado(cita.getIdPaciente()), obtenerMedicoResponseSinEstado(cita.getIdMedico()));
	}
	
	@Override
	public CitaResponse registrar(CitaRequest request) {
		log.info("Registrando nueva Cita: {}", request);
		validarPacienteSinCitasActivas(request.idPaciente());
		
		PacienteResponse paciente = obtenerPacienteResponse(request.idPaciente());
		MedicoResponse medico = obtenerMedicoResponse(request.idMedico());
		
		if(!medico.disponibilidad().equals(DisponibilidadMedico.DISPONIBLE.getDescripcion())) {
			throw new IllegalArgumentException("El medico seleccionado no esta disponible actualmente.");
		}
		
		Cita cita = citaMapper.requestToEntity(request);
		cita = citaRepository.save(cita);
		
		sincronizarDisponibilidadMedico(cita.getIdMedico(), EstadoCita.PENDIENTE, cita.getId());
		
		log.info("Cita registrada exitosamente: {}", cita);
		return citaMapper.entityToResponse(cita, paciente, medico);
	}
	
	@Override
	public CitaResponse actualizar(CitaRequest request, Long id) {
		Cita cita = obtenerCitaOException(id);
		log.info("Actualizando Cita con id: {}", id);
		
		if (cita.getEstadoCita() != EstadoCita.PENDIENTE && cita.getEstadoCita() != EstadoCita.CONFIRMADA) {
			throw new IllegalArgumentException("No se puede editar una cita que está " + cita.getEstadoCita().getDescripcion());
		}
		
		Long idMedicoAnterior = cita.getIdMedico();
		Long idMedicoNuevo = request.idMedico();
		
		if(!idMedicoAnterior.equals(idMedicoNuevo)) {
			MedicoResponse medicoNuevo = obtenerMedicoResponse(idMedicoNuevo);
			if(!medicoNuevo.disponibilidad().equals(DisponibilidadMedico.DISPONIBLE.getDescripcion())) {
				throw new IllegalArgumentException("El nuevo medico seleccionado no esta disponible");
			}
			medicoClient.cambiarDisponibilidad(idMedicoAnterior, DisponibilidadMedico.DISPONIBLE.getCodigo(), cita.getId());
		}
		
		PacienteResponse paciente = obtenerPacienteResponse(request.idPaciente());
		MedicoResponse medico = obtenerMedicoResponse(idMedicoNuevo);
		
		citaMapper.updateEntityFromRequest(request, cita);
		
		if(!idMedicoAnterior.equals(idMedicoNuevo)) {
			sincronizarDisponibilidadMedico(idMedicoNuevo, cita.getEstadoCita(), cita.getId());
		}
		
		log.info("Cita actualizada con id: {}", id);
		return citaMapper.entityToResponse(cita, paciente, medico);
	}
	
	@Override
	public void eliminar(Long id) {
		Cita cita = obtenerCitaOException(id);
		log.info("Eliminando Cita con id: {}", id);
		
		validarEstadoCitaAlEliminar(cita);
		
		if(cita.getEstadoCita() == EstadoCita.PENDIENTE) {
			sincronizarDisponibilidadMedico(cita.getIdMedico(), EstadoCita.CANCELADA, cita.getId());
		}
		
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
	
	private void validarTransicionEstado(EstadoCita estadoActual, EstadoCita estadoNuevo) {
		if(estadoActual == EstadoCita.FINALIZADA || estadoActual == EstadoCita.CANCELADA) {
			throw new IllegalStateException("No se pudo cambiar el estado de una cita que ya fue " + estadoActual.name());
		}
		
		boolean transicionValida = false;
		switch(estadoActual) {
			case PENDIENTE -> transicionValida = (estadoNuevo == EstadoCita.CONFIRMADA || estadoNuevo == EstadoCita.CANCELADA);
			case CONFIRMADA -> transicionValida = (estadoNuevo == EstadoCita.EN_CURSO || estadoNuevo == EstadoCita.CANCELADA);
			case EN_CURSO -> transicionValida = (estadoNuevo == EstadoCita.FINALIZADA);
			default -> transicionValida = true;
		}
		
		if(!transicionValida) {
			throw new IllegalArgumentException("Transicion de estado no valida de " + estadoActual.name() + " a " + estadoNuevo.name());
		}
	}
	
	private void sincronizarDisponibilidadMedico(Long idMedico, EstadoCita estadoCita, Long idCita) {
		Long idDisponibilidad = switch(estadoCita) {
			case PENDIENTE, CONFIRMADA -> DisponibilidadMedico.NO_DISPONIBLE.getCodigo();
			case EN_CURSO -> DisponibilidadMedico.EN_CONSULTA.getCodigo();
			case FINALIZADA, CANCELADA -> DisponibilidadMedico.DISPONIBLE.getCodigo();
		};
		
		log.info("Sincronizando disponibilidad del medico {} a codigo {}", idMedico, idDisponibilidad);
		medicoClient.cambiarDisponibilidad(idMedico, idDisponibilidad, idCita);
	}
	
	private void validarPacienteSinCitasActivas(Long idPaciente) {
		List<EstadoCita> estadosActivos = Arrays.asList(EstadoCita.PENDIENTE, EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO);
		if (citaRepository.existsByIdPacienteAndEstadoCitaInAndEstadoRegistro(idPaciente, estadosActivos, EstadoRegistro.ACTIVO)) {
			throw new IllegalArgumentException("El paciente ya tiene una cita activa (PENDIENTE, CONFIRMADA o EN_CURSO). No puede tener más de una.");
		}
	}

}
