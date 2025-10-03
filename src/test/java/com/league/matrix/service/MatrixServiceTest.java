package com.league.matrix.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.charset.StandardCharsets;

import com.league.matrix.exception.MatrixProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class MatrixServiceTest {

    private static final String SAMPLE = "1,2,3\n4,5,6\n7,8,9";

    private MatrixService service;

    @BeforeEach
    void setUp() {
        service = new MatrixService();
    }

    @Test
    void echoReturnsMatrix() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv",
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        String result = service.echo(file);

        assertThat(result).isEqualTo(SAMPLE);
    }

    @Test
    void invertReturnsTransposedMatrix() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv",
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        String result = service.invert(file);

        assertThat(result).isEqualTo("1,4,7\n2,5,8\n3,6,9");
    }

    @Test
    void flattenReturnsSingleRow() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv",
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        String result = service.flatten(file);

        assertThat(result).isEqualTo("1,2,3,4,5,6,7,8,9");
    }

    @Test
    void sumReturnsTotal() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv",
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        String result = service.sum(file);

        assertThat(result).isEqualTo("45");
    }

    @Test
    void multiplyReturnsProduct() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv",
                SAMPLE.getBytes(StandardCharsets.UTF_8));

        String result = service.multiply(file);

        assertThat(result).isEqualTo("362880");
    }

    @Test
    void rejectNonSquareMatrix() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv",
                "1,2\n3,4\n5,6".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> service.echo(file))
                .isInstanceOf(MatrixProcessingException.class)
                .hasMessageContaining("Matrix must be square");
    }

    @Test
    void rejectNonIntegerValue() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv",
                "Col1,2,3\n4,x,6\n7,8,9".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> service.echo(file))
                .isInstanceOf(MatrixProcessingException.class)
                .hasMessageContaining("Invalid integer");
    }

    @Test
    void rejectEmptyFile() {
        MockMultipartFile file = new MockMultipartFile("file", "matrix.csv", "text/csv", new byte[0]);

        assertThatThrownBy(() -> service.echo(file))
                .isInstanceOf(MatrixProcessingException.class)
                .hasMessageContaining("empty");
    }
}
