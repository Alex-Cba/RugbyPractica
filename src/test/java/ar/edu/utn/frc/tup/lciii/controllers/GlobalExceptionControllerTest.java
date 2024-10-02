package ar.edu.utn.frc.tup.lciii.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionController.class)
@Import(GlobalExceptionController.class)
public class GlobalExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testNotFoundExceptionHandler() throws Exception {
        mockMvc.perform(get("/your-endpoint")  // Cambia "/your-endpoint" por una ruta que lance NotFoundException
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.NOT_FOUND.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Teams not found"));
    }

    @Test
    public void testIllegalArgumentExceptionHandler() throws Exception {
        // Simula una excepción lanzada en tu controlador
        mockMvc.perform(get("/your-endpoint")  // Cambia "/your-endpoint" por una ruta que lance IllegalArgumentException
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.BAD_REQUEST.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Bad argument passed"));
    }

    @Test
    public void testGeneralExceptionHandler() throws Exception {
        // Simula una excepción lanzada en tu controlador
        mockMvc.perform(get("/your-endpoint")  // Cambia "/your-endpoint" por una ruta que lance Exception genérica
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.error").value(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .andExpect(jsonPath("$.message").value("Internal server error"));
    }
}