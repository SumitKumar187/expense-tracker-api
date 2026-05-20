package expense_tracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import expense_tracker.dto.request.LoginRequest;
import expense_tracker.dto.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnCreated() throws Exception {

        String email =
                "test" + System.currentTimeMillis() + "@test.com";

        RegisterRequest request = new RegisterRequest();

        request.setUsername("user" + System.currentTimeMillis());
        request.setEmail(email);
        request.setPassword("password123");

        mockMvc.perform(

                        post("/api/auth/register")

                                .contentType(MediaType.APPLICATION_JSON)

                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )

                .andExpect(status().isCreated());
    }

    @Test
    void login_ShouldReturnOk() throws Exception {

        String email =
                "test" + System.currentTimeMillis() + "@test.com";

        // register first

        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setUsername("user" + System.currentTimeMillis());
        registerRequest.setEmail(email);
        registerRequest.setPassword("password123");

        mockMvc.perform(

                        post("/api/auth/register")

                                .contentType(MediaType.APPLICATION_JSON)

                                .content(
                                        objectMapper.writeValueAsString(registerRequest)
                                )
                )

                .andExpect(status().isCreated());

        // login

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setEmail(email);
        loginRequest.setPassword("password123");

        mockMvc.perform(

                        post("/api/auth/login")

                                .contentType(MediaType.APPLICATION_JSON)

                                .content(
                                        objectMapper.writeValueAsString(loginRequest)
                                )
                )

                .andExpect(status().isOk());
    }
}