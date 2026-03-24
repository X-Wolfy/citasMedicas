package com.luis.commons.dto;

public record MedicoResponse(
		Long id,
		String nombre,
		Short edad,
		String email,
		String telefono,
		String cedulaProfesional,
		String especialidad,
		String disponibilidad) {
	
}
