package com.luis.medicos.entities;

import com.luis.commons.enums.DisponibilidadMedico;
import com.luis.commons.enums.EspecialidadMedico;
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
@Table(name = "MEDICOS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Medico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_MEDICO")
	private Long id;
	
	@Column(name = "NOMBRE", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
	private String apellidoPaterno;
	
	@Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
	private String apellidoMaterno;
	
	@Column(name = "EDAD", nullable = false)
	private Short edad;
	
	@Column(name = "EMAIL", nullable = false, length = 100)
	private String email;
	
	@Column(name = "TELEFONO", nullable = false, length = 10)
	private String telefono;
	
	@Column(name = "CEDULA_PROFESIONAL", nullable = false, length = 12)
	private String cedulaProfesional;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESPECIALIDAD", nullable = false)
	private EspecialidadMedico especialidad;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "DISPONIBILIDAD", nullable = false)
	private DisponibilidadMedico disponibilidad;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_REGISTRO", nullable = false)
	private EstadoRegistro estadoRegistro;
}
