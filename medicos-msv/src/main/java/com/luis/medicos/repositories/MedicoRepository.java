package com.luis.medicos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luis.medicos.entities.Medico;
import java.util.List;
import java.util.Optional;

import com.luis.commons.enums.EstadoRegistro;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{
	List<Medico> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Medico> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	boolean existsByEmailAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);
	boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);
	boolean existsByCedulaProfesionalAndEstadoRegistro(String cedulaProfesional, EstadoRegistro estadoRegistro);
	
	boolean existsByEmailAndEstadoRegistroAndIdNot(String email, EstadoRegistro estadoRegistro, Long id);
	boolean existsByTelefonoAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estadoRegistro, Long id);
	boolean existsByCedulaProfesionalAndIdNotAndEstadoRegistro(String cedulaProfesional, Long id, EstadoRegistro estadoRegistro);
}
