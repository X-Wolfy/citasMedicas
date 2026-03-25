package com.luis.citas.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EstadoCita {
	PENDIENTE(1L, "Pendiente de confirmar"),
    CONFIRMADA(2L, "Confirmada por el paciente"),
    EN_CURSO(3L, "Paciente llegó a su cita"),
    FINALIZADA(4L, "Cita finalizada"),
	CANCELADA(5L, "Cita cancelada");
    
    private final Long codigo;
	private final String descripcion;
	
	public static EstadoCita fromCodigo(Long codigo) {
        for (EstadoCita e : values()) {
            if (e.codigo == codigo) {
                return e;
            }
        }
        throw new IllegalArgumentException("Código de cita no válido: " + codigo);
    }
	
	public static EstadoCita fromDescripcion(String descripcion) {
        for (EstadoCita e : values()) {
            if (e.descripcion.equalsIgnoreCase(descripcion)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Descripción de cita no válida: " + descripcion);
    }
}
