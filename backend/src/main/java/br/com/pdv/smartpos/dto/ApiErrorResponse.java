package br.com.pdv.smartpos.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(
    LocalDateTime timestamp,
    Integer status,
    String message,
    Map<String, String> errors
) {
}
