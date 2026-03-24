package com.luis.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DisponibilidadMedico {
	
	DISPONIBLE(1L, "Disponible para atender pacientes"),
    EN_CONSULTA(2L, "Atendiendo a un paciente actualmente"),
    FUERA_DE_TURNO(3L, "No se encuentra en turno"),
    DE_GUARDIA(4L, "Disponible bajo guardia"),
    NO_DISPONIBLE(5L, "No disponible por el momento");
	
	private final Long codigo;
	
	private final String descripcion;
	
	public static DisponibilidadMedico fromCodigo(Long codigo) {
        for (DisponibilidadMedico estado : DisponibilidadMedico.values()) {
            if (estado.getCodigo() == codigo) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de disponibilidad no válido: " + codigo);
    }
}
