package com.luis.pacientes.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luis.pacientes.entities.Paciente;
import java.util.List;
import java.util.Optional;

import com.luis.commons.enums.EstadoRegistro;


@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>{
	List<Paciente> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Paciente> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	boolean existsByEmailAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);
	boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
	
	boolean existsByEmailAndEstadoRegistroAndIdNot(String email, EstadoRegistro estadoRegistro, Long id);
	boolean existsByTelefonoAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estadoRegistro, Long id);
}
