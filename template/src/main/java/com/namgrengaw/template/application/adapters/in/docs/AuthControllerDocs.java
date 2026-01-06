package com.namgrengaw.template.application.adapters.in.docs;

import com.namgrengaw.template.application.adapters.in.dtos.AccountCredentialsDto;
import com.namgrengaw.template.application.adapters.in.dtos.TokenDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AuthControllerDocs {

    @Operation(
            summary = "Create new user endpoint",
            description = "Create new user for the application",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = AccountCredentialsDto.class))
                            )
                    }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server error", responseCode = "500", content = @Content)
            }
    )
    @PostMapping(value = "/create-user",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    AccountCredentialsDto create(@RequestBody AccountCredentialsDto user);


    @Operation(
            summary = "Sign In endpoint",
            description = "Returns a token with the credentials for using the API",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = TokenDto.class))
                            )
                    }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server error", responseCode = "500", content = @Content)
            }
    )
    @PostMapping("/signin")
    ResponseEntity<?> signIn(
            @RequestBody AccountCredentialsDto credentials
    );

    @Operation(
            summary = "Refresh token endpoint",
            description = "Returns a refresh token by the username and old refresh token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = TokenDto.class))
                            )
                    }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server error", responseCode = "500", content = @Content)
            }
    )
    @PutMapping("/refresh/{username}")
    ResponseEntity<?> refresh(
            @PathVariable("username") String username,
            @RequestHeader("Authorization") String refreshToken
    );
}
