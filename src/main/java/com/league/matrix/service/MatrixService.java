package com.league.matrix.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.league.matrix.exception.MatrixProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MatrixService {

    public String echo(MultipartFile file) {
        List<List<BigInteger>> matrix = parseMatrix(file);
        return formatMatrix(matrix);
    }

    public String invert(MultipartFile file) {
        List<List<BigInteger>> matrix = parseMatrix(file);
        int size = matrix.size();
        List<List<BigInteger>> transposed = new ArrayList<>(size);
        for (int columnIndex = 0; columnIndex < size; columnIndex++) {
            List<BigInteger> column = new ArrayList<>(size);
            for (int rowIndex = 0; rowIndex < size; rowIndex++) {
                column.add(matrix.get(rowIndex).get(columnIndex));
            }
            transposed.add(column);
        }
        return formatMatrix(transposed);
    }

    public String flatten(MultipartFile file) {
        List<List<BigInteger>> matrix = parseMatrix(file);
        StringJoiner joiner = new StringJoiner(",");
        for (List<BigInteger> row : matrix) {
            for (BigInteger value : row) {
                joiner.add(value.toString());
            }
        }
        return joiner.toString();
    }

    public String sum(MultipartFile file) {
        List<List<BigInteger>> matrix = parseMatrix(file);
        BigInteger total = BigInteger.ZERO;
        for (List<BigInteger> row : matrix) {
            for (BigInteger value : row) {
                total = total.add(value);
            }
        }
        return total.toString();
    }

    public String multiply(MultipartFile file) {
        List<List<BigInteger>> matrix = parseMatrix(file);
        BigInteger product = BigInteger.ONE;
        for (List<BigInteger> row : matrix) {
            for (BigInteger value : row) {
                product = product.multiply(value);
            }
        }
        return product.toString();
    }

    private List<List<BigInteger>> parseMatrix(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new MatrixProcessingException("Uploaded file is empty");
        }

        List<List<BigInteger>> rows = new ArrayList<>();
        int expectedColumns = -1;
        int currentRow = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                currentRow++;
                if (line.trim().isEmpty()) {
                    throw new MatrixProcessingException("Row " + currentRow + " is empty");
                }
                String[] parts = line.split(",");
                if (expectedColumns == -1) {
                    expectedColumns = parts.length;
                } else if (parts.length != expectedColumns) {
                    throw new MatrixProcessingException("Matrix must be square; row " + currentRow
                            + " has " + parts.length + " values but expected " + expectedColumns);
                }
                List<BigInteger> row = new ArrayList<>(parts.length);
                for (int columnIndex = 0; columnIndex < parts.length; columnIndex++) {
                    String rawValue = parts[columnIndex].trim();
                    if (rawValue.isEmpty()) {
                        throw new MatrixProcessingException("Matrix contains empty value at row " + currentRow
                                + " column " + (columnIndex + 1));
                    }
                    try {
                        row.add(new BigInteger(rawValue));
                    } catch (NumberFormatException ex) {
                        throw new MatrixProcessingException("Invalid integer at row " + currentRow + " column "
                                + (columnIndex + 1), ex);
                    }
                }
                rows.add(row);
            }
        } catch (IOException ex) {
            throw new MatrixProcessingException("Unable to read uploaded file", ex);
        }

        if (rows.isEmpty()) {
            throw new MatrixProcessingException("Matrix must contain at least one row");
        }
        if (expectedColumns != rows.size()) {
            throw new MatrixProcessingException(
                    "Matrix must be square; got " + rows.size() + " rows and " + expectedColumns + " columns");
        }

        return rows;
    }

    private String formatMatrix(List<List<BigInteger>> matrix) {
        StringBuilder builder = new StringBuilder();
        for (int rowIndex = 0; rowIndex < matrix.size(); rowIndex++) {
            List<BigInteger> row = matrix.get(rowIndex);
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                if (columnIndex > 0) {
                    builder.append(',');
                }
                builder.append(row.get(columnIndex));
            }
            if (rowIndex < matrix.size() - 1) {
                builder.append('\n');
            }
        }
        return builder.toString();
    }
}
