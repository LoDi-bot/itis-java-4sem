package ru.itis.stogram.validation.http;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class ValidationErrorResponse {
    private final List<Violation> violations;
}
