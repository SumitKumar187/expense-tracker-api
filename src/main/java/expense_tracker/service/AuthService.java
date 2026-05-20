package expense_tracker.service;

import expense_tracker.dto.request.LoginRequest;
import expense_tracker.dto.request.RegisterRequest;
import expense_tracker.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}