package com.luis.citas.entities;

import java.time.LocalDateTime;

import com.luis.citas.enums.EstadoCita;
import com.luis.commons.enums.EstadoRegistro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CITAS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Cita {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_CITA")
    private Long id;
	
	@Column(name = "ID_PACIENTE", nullable = false)
	private Long idPaciente;
	
	@Column(name = "ID_MEDICO", nullable = false)
	private Long idMedico;
	
	@Column(name = "FECHA_CITA", nullable = false)
	private LocalDateTime fechaCita;
	
	@Column(name = "SINTOMAS", nullable = false, length = 500)
	private String sintomas;
	
    @Column(name = "ESTADO_CITA", nullable = false)
    @Enumerated(EnumType.STRING)
	private EstadoCita estadoCita;
	
    @Column(name = "ESTADO_REGISTRO", length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
	private EstadoRegistro estadoRegistro;
}
