package com.league.matrix.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MatrixControllerTest {

    private static final String SAMPLE = "1,2,3\n4,5,6\n7,8,9";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void echoEndpointReturnsMatrix() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "matrix.csv",
                MediaType.TEXT_PLAIN_VALUE,
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/echo").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string(SAMPLE));
    }

    @Test
    void invertEndpointReturnsTransposedMatrix() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "matrix.csv",
                MediaType.TEXT_PLAIN_VALUE,
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/invert").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("1,4,7\n2,5,8\n3,6,9"));
    }

    @Test
    void flattenEndpointReturnsSingleRow() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "matrix.csv",
                MediaType.TEXT_PLAIN_VALUE,
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/flatten").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("1,2,3,4,5,6,7,8,9"));
    }

    @Test
    void sumEndpointReturnsTotal() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "matrix.csv",
                MediaType.TEXT_PLAIN_VALUE,
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/sum").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("45"));
    }

    @Test
    void multiplyEndpointReturnsProduct() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "matrix.csv",
                MediaType.TEXT_PLAIN_VALUE,
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/multiply").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("362880"));
    }

    @Test
    void missingFileReturnsBadRequest() throws Exception {
        mockMvc.perform(multipart("/echo"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Missing or invalid file")));
    }

    @Test
    void invalidMatrixReturnsBadRequest() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "matrix.csv",
                MediaType.TEXT_PLAIN_VALUE,
                "1,2\n3,4\n5,6".getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/echo").file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Matrix must be square")));
    }
}
