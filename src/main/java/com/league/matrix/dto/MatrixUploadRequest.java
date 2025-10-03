package com.league.matrix.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MatrixUploadRequest", description = MatrixUploadRequest.DESCRIPTION, requiredProperties = {
        "file" })
public class MatrixUploadRequest {

    public static final String DESCRIPTION = "Square matrix provided as a CSV file (no header, integers only).";

    @Schema(type = "string", format = "binary", description = "CSV file containing a square integer matrix")
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
