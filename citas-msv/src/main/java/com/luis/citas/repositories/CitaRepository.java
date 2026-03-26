package com.luis.citas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luis.citas.entities.Cita;
import com.luis.citas.enums.EstadoCita;

import java.util.List;
import java.util.Optional;

import com.luis.commons.enums.EstadoRegistro;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long>{
	List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	boolean existsByIdMedicoAndEstadoCitaInAndEstadoRegistro(Long idMedico, List<EstadoCita> estados, EstadoRegistro estadoRegistro);
	
	boolean existsByIdMedicoAndEstadoCitaInAndEstadoRegistroAndIdNot(Long idMedico, List<EstadoCita> estados, EstadoRegistro estadoRegistro, Long idCita);
	
	boolean existsByIdPacienteAndEstadoCitaInAndEstadoRegistro(Long idPaciente, List<EstadoCita> estados, EstadoRegistro estadoRegistro);
}