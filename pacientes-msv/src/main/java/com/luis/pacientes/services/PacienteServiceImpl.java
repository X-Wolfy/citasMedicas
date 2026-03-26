package com.luis.pacientes.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luis.commons.clients.CitaClient;
import com.luis.commons.dto.PacienteRequest;
import com.luis.commons.dto.PacienteResponse;
import com.luis.commons.enums.EstadoRegistro;
import com.luis.commons.exceptions.EntidadRelacionadaException;
import com.luis.commons.exceptions.RecursoNoEncontradoException;
import com.luis.pacientes.entities.Paciente;
import com.luis.pacientes.mappers.PacienteMapper;
import com.luis.pacientes.repositories.PacienteRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PacienteServiceImpl implements PacienteService {
	private final PacienteRepository pacienteRepository;
	private final PacienteMapper pacienteMapper;
	private final CitaClient citaClient;
	
	@Override
	@Transactional(readOnly = true)
	public List<PacienteResponse> listar() {
		log.info("Listado de todos los pacientes activos solicitados");
		return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(pacienteMapper::entityToResponse).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public PacienteResponse obtenerPorId(Long id) {
		return pacienteMapper.entityToResponse(obtenerPacienteOException(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public PacienteResponse obtenerPacientePorIdSinEstado(Long id) {
		log.info("Buscando Paciente sin estado con id: {}", id);
		return pacienteMapper.entityToResponse(pacienteRepository.findById(id).orElseThrow(() ->
			new RecursoNoEncontradoException("Paciente sin estado no encontrado con id: " + id)));
	}

	@Override
	public PacienteResponse registrar(PacienteRequest request) {
		log.info("registrando nuevo paciente: {}", request.nombre());
		
		validarDatosUnicos(request.email(), request.telefono(), null);
		
		Paciente paciente = pacienteMapper.requestToEntity(request);
		
		paciente.setImc(calcularImc(request.peso(), request.estatura()));
		paciente.setNumExpediente(generarNumExpediente(request.telefono()));
		
		pacienteRepository.save(paciente);
		
		log.info("Nuevo paciente registrado: {}", request.nombre());
		return pacienteMapper.entityToResponse(paciente);
	}

	@Override
	public PacienteResponse actualizar(PacienteRequest request, Long id) {
		Paciente paciente = obtenerPacienteOException(id);
		log.info("Actualizando paciente con id: {}", id);
		
		if(citaClient.pacienteTieneCitasBloqueantes(id)) {
			throw new EntidadRelacionadaException("No se puede actualizar el paciente porque tiene citas CONFIRMADAS o EN_CURSO.");
		}
		
		validarDatosUnicos(request.email(), request.telefono(), id);
		
		boolean telefonoCambio = !paciente.getTelefono().equals(request.telefono());
		pacienteMapper.updateEntityFromRequest(request, paciente);
		
		if(telefonoCambio) {
			paciente.setNumExpediente(generarNumExpediente(request.telefono()));
			paciente.setTelefono(request.telefono());
		}
		
		boolean cambioImc = !paciente.getPeso().equals(request.peso()) || !paciente.getEstatura().equals(request.estatura());
		
		if(cambioImc) {
			paciente.setImc(calcularImc(request.peso(), request.estatura()));
			paciente.setPeso(request.peso());
			paciente.setEstatura(request.estatura());
		}
		
		//pacienteRepository.save(paciente);
		log.info("Paciente con id {} actualizado", id);
		
		return pacienteMapper.entityToResponse(paciente);
	}

	@Override
	public void eliminar(Long id) {
		Paciente paciente = obtenerPacienteOException(id);
		log.info("Eliminando Paciente con id: {}", id);
		
		if(citaClient.pacienteTieneCitasBloqueantes(id)) {
			throw new EntidadRelacionadaException("No se puede eliminar el paciente porque tiene citas CONFIRMADAS o EN_CURSO.");
		}
		
		paciente.setEstadoRegistro(EstadoRegistro.ELIMINADO);
		log.info("Paciente con id {} ha sido marcado como eliminado", id);
	}
	
	private Paciente obtenerPacienteOException(Long id) {
		log.info("Buscando Paciente activo con id: {}", id);
		
		return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(() -> 
			new RecursoNoEncontradoException("Paciente activo no encontrado con id: " + id));
	}
	
	private void validarDatosUnicos(String email, String telefono, Long id) {
		String emailLower = email.toLowerCase();
		boolean emailExiste;
		boolean telefonoExiste;
		
		if(id == null) {
			emailExiste = pacienteRepository.existsByEmailAndEstadoRegistro(emailLower, EstadoRegistro.ACTIVO);
			telefonoExiste = pacienteRepository.existsByTelefonoAndEstadoRegistro(telefono, EstadoRegistro.ACTIVO);
		} else {
			emailExiste = pacienteRepository.existsByEmailAndEstadoRegistroAndIdNot(emailLower, EstadoRegistro.ACTIVO, id);
			telefonoExiste = pacienteRepository.existsByTelefonoAndEstadoRegistroAndIdNot(telefono, EstadoRegistro.ACTIVO, id);
		}
		
		if(emailExiste) {
			throw new IllegalArgumentException("El email ya está registrado en otro paciente activo.");
		}
		if(telefonoExiste) {
			throw new IllegalArgumentException("El telefono ya está registrado en otro paciente activo.");
		}
	}
	
	private Double calcularImc(Double peso, Double estatura) {
		if(estatura == null || estatura <= 0) return 0.0;
		
		double imc = peso / Math.pow(estatura, 2);
		
		return Math.round(imc * 100.0) / 100.0;
	}
	
	private String generarNumExpediente(String telefono) {
		if (telefono == null || telefono.isEmpty()) return "";
		
		StringBuilder expediente = new StringBuilder();
		for (char digito : telefono.toCharArray()) {
			expediente.append(digito).append("X");
		}
		return expediente.toString();
	}
}
