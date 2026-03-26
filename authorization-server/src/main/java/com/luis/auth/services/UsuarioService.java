package com.luis.auth.services;

import java.util.Set;

import com.luis.auth.dto.UsuarioRequest;
import com.luis.auth.dto.UsuarioResponse;

public interface UsuarioService {
	Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}
