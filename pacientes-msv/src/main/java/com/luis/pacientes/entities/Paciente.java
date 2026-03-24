package com.luis.pacientes.entities;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@Table(name = "PACIENTES")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Paciente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_PACIENTE")
	private Long id;
	
	@Column(name = "NOMBRE", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
	private String apellidoPaterno;
	
	@Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
	private String apellidoMaterno;
	
	@Column(name = "EDAD", nullable = false)
	private Short edad;
	
	@Column(name = "PESO", nullable = false)
	private Double peso;
	
	@Column(name = "ESTATURA", nullable = false)
	private Double estatura;

	@Column(name = "IMC", nullable = false)
	private Double imc;
	
	@Column(name = "EMAIL", nullable = false, length = 100)
	private String email;
	
	@Column(name = "TELEFONO", nullable = false, length = 10)
	private String telefono;
	
	@Column(name = "DIRECCION", nullable = false, length = 150)
	private String direccion;
	
	@Column(name = "NUM_EXPEDIENTE", nullable = false, length = 20)
	private String numExpediente;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_REGISTRO", nullable = false)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	private EstadoRegistro estadoRegistro;
}
