package com.luis.auth.services;

import com.luis.auth.dto.LoginRequest;
import com.luis.auth.dto.TokenResponse;

public interface AuthService {
	TokenResponse autenticar(LoginRequest request) throws Exception;
}
