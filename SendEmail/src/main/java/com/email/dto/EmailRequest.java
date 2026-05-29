package com.email.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Email request payload")
public class EmailRequest {
    @NotBlank(message = "Recipient email is required")
    @Schema(description = "Recipient email address", example = "recipient@example.com")
    private String to;

    @NotBlank(message = "Subject is required")
    @Schema(description = "Email subject", example = "Test Subject")
    private String subject;

    @NotBlank(message = "Body is required")
    @Schema(description = "Email body content", example = "This is the email body.")
    private String body;
}
