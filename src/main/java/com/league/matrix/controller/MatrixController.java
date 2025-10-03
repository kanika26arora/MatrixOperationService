package com.league.matrix.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.league.matrix.dto.MatrixUploadRequest;
import com.league.matrix.service.MatrixService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
@Tag(name = "Matrix operations", description = "Endpoints that process square CSV matrices uploaded as multipart files")
public class MatrixController {

    private final MatrixService matrixService;

    public MatrixController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @PostMapping(value = "/echo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Echo matrix",
            description = "Returns the uploaded matrix exactly as received.",
            requestBody = @RequestBody(description = MatrixUploadRequest.DESCRIPTION, required = true, content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = MatrixUploadRequest.class)
            )),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matrix echoed successfully", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "1,2,3\\n4,5,6\\n7,8,9"))),
                    @ApiResponse(responseCode = "400", description = "Invalid matrix input", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE))
            }
    )
    public String echo(@RequestPart("file") MultipartFile file) {
        return matrixService.echo(file);
    }

    @PostMapping(value = "/invert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Invert matrix",
            description = "Returns the transpose of the uploaded matrix.",
            requestBody = @RequestBody(description = MatrixUploadRequest.DESCRIPTION, required = true, content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = MatrixUploadRequest.class)
            )),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matrix inverted successfully", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "1,4,7\\n2,5,8\\n3,6,9"))),
                    @ApiResponse(responseCode = "400", description = "Invalid matrix input", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE))
            }
    )
    public String invert(@RequestPart("file") MultipartFile file) {
        return matrixService.invert(file);
    }

    @PostMapping(value = "/flatten", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Flatten matrix",
            description = "Returns all matrix values as a single comma separated line.",
            requestBody = @RequestBody(description = MatrixUploadRequest.DESCRIPTION, required = true, content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = MatrixUploadRequest.class)
            )),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matrix flattened successfully", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "1,2,3,4,5,6,7,8,9"))),
                    @ApiResponse(responseCode = "400", description = "Invalid matrix input", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE))
            }
    )
    public String flatten(@RequestPart("file") MultipartFile file) {
        return matrixService.flatten(file);
    }

    @PostMapping(value = "/sum", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Sum matrix",
            description = "Returns the sum of all matrix values.",
            requestBody = @RequestBody(description = MatrixUploadRequest.DESCRIPTION, required = true, content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = MatrixUploadRequest.class)
            )),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matrix summed successfully", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "45"))),
                    @ApiResponse(responseCode = "400", description = "Invalid matrix input", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE))
            }
    )
    public String sum(@RequestPart("file") MultipartFile file) {
        return matrixService.sum(file);
    }

    @PostMapping(value = "/multiply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Multiply matrix",
            description = "Returns the product of all matrix values.",
            requestBody = @RequestBody(description = MatrixUploadRequest.DESCRIPTION, required = true, content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = MatrixUploadRequest.class)
            )),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Matrix multiplied successfully", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(type = "string", example = "362880"))),
                    @ApiResponse(responseCode = "400", description = "Invalid matrix input", content = @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE))
            }
    )
    public String multiply(@RequestPart("file") MultipartFile file) {
        return matrixService.multiply(file);
    }
}
