package com.luis.commons.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MedicoRequest(
		@NotBlank(message = "El nombre es requerido")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
        String nombre,
        
        @NotBlank(message = "El apellido paterno es requerido")
        @Size(min = 1, max = 50, message = "El apellido paterno debe tener entre 1 y 50 caracteres")
        String apellidoPaterno,
        
        @NotBlank(message = "El apellido materno es requerido")
        @Size(min = 1, max = 50, message = "El apellido materno debe tener entre 1 y 50 caracteres")
        String apellidoMaterno,
        
        @NotNull(message = "La edad es requerida")
        @Min(value = 18, message = "La edad mínima es de 18 años")
        @Max(value = 100, message = "La edad máxima es de 100 años")
        Short edad,
        
        @NotBlank(message = "El email es requerido")
        @Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
        @Email(message = "El email debe tener el formato correcto (correo@dominio)")
        String email,
        
        @NotBlank(message = "El teléfono es requerido")
        @Size(min = 10, max = 10, message = "El teléfono debe tener exactamente 10 dígitos")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener solo 10 dígitos numéricos")
        String telefono,
        
        @NotBlank(message = "La cédula profesional es requerida")
        @Size(min = 12, max = 12, message = "La cédula profesional debe tener exactamente 12 caracteres")
        String cedulaProfesional,
        
        @NotNull(message = "El id de la especialidad es requerido")
        @Positive(message = "El id de la especialidad debe ser positivo")
        Long idEspecialidad
) {

}
