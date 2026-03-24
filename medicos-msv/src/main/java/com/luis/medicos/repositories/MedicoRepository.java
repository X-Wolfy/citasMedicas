package com.luis.medicos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luis.medicos.entities.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>{

}
