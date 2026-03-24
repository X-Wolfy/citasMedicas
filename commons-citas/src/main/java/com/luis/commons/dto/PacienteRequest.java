package com.luis.commons.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PacienteRequest(
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
		@Min(value = 1, message = "La edad mínima es de 1 año")
		@Max(value = 100, message = "La edad máxima es de 100 años")
		Short edad,
		
		@NotNull(message = "El peso es requerido")
	    @DecimalMin(value = "0.1", message = "El peso debe ser mayor que 0kg")
	    @DecimalMax(value = "200", message = "El peso máximo permitido es de 200 kg")
		Double peso,
		
		@NotNull(message = "La estatura es requerida")
		@DecimalMin(value = "1.0", message = "La estatura debe ser mayor que 1 metro")
	    @DecimalMax(value = "2.0", message = "La estatura máxima permitida es de 2 metros")
	    Double estatura,
	    
	    @NotBlank(message = "El email es requerido")
		@Size(min = 1, max = 100, message = "El email debe tener entre 1 y 100 caracteres")
		@Email(message = "El email debe tener el formato correcto (correo@dominio)")
		String email,
		
		@NotNull(message = "El teléfono es requerido")
		@Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener solo 10 dígitos numéricos")
		String telefono,
		
		@NotBlank(message = "La dirección es requerida")
		@Size(min = 1, max = 150, message = "La dirección debe tener entre 1 y 150 caracteres")
		String direccion
) {}