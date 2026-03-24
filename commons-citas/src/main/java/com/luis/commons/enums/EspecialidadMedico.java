package com.luis.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EspecialidadMedico {
	
	MEDICINA_GENERAL(1L, "Medicina General"),
	PEDIATRIA(2L, "Pediatría"),
	CARDIOLOGIA(3L, "Cardiología"),
	DERMATOLOGIA(4L, "Dermatología"),
	NEUROLOGIA(5L, "Neurología"),
	GINECOLOGIA(6L, "Ginecología y Obstetricia"),
	PSIQUIATRIA(7L, "Psiquiatría"),
	TRAUMATOLOGIA(8L, "Traumatología y Ortopedia"),
	ONCOLOGIA(9L, "Oncología"),
	OTORRINOLARINGOLOGIA(10L, "Otorrinolaringología"),
	OFTALMOLOGIA(11L, "Oftalmología"),
	ENDOCRINOLOGIA(12L, "Endocrinología"),
	NEFROLOGIA(13L, "Nefrología"),
	REUMATOLOGIA(14L, "Reumatología"),
	UROLOGIA(15L, "Urología");
	
	private final Long codigo;
	private final String descripcion;
	
	public static EspecialidadMedico fromCodigo(Long codigo) {
        for (EspecialidadMedico e : values()) {
            if (e.codigo == codigo) {
                return e;
            }
        }
        throw new IllegalArgumentException("Código de especialidad no válido: " + codigo);
    }
	
	public static EspecialidadMedico fromDescripcion(String descripcion) {
        for (EspecialidadMedico e : values()) {
        	String descEspecialidad = quitarAcentos(e.descripcion);
            if (descEspecialidad.equalsIgnoreCase(descripcion)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Descripción de especialidad no válida: " + descripcion);
    }
	
	private static String quitarAcentos(String s) {
        return s
                .replace("á", "a").replace("Á", "A")
                .replace("é", "e").replace("É", "E")
                .replace("í", "i").replace("Í", "I")
                .replace("ó", "o").replace("Ó", "O")
                .replace("ú", "u").replace("Ú", "U");
    }

}
