package dev.amir.dragon.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for Field Validator
 */
@DisplayName("Field Validator Tests")
class InputValidatorTest {
    private static final String ERROR_MESSAGE = "Invalid field";

    @Test
    @DisplayName("Should return the same value")
    void shouldReturnTheSameValue() {
        // Given
        String input = "A valid value";

        // When
        String actual = InputValidator.requireNonBlank(input, ERROR_MESSAGE);

        // Then
        assertEquals(input, actual);
    }

    @Test
    @DisplayName("Should return a trimmed value")
    void shouldReturnATrimmedValue() {
        // Given
        String input = "   A valid value    ";
        String expected = input.trim();

        // When
        String actual = InputValidator.requireNonBlank(input, ERROR_MESSAGE);

        // Then
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when input is blank")
    void shouldThrowExceptionWhenInputIsBlank(String input) {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.requireNonBlank(input, ERROR_MESSAGE));
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "ID"})
    @DisplayName("Should not throw any exception when input is non-null")
    void shouldNotThrowAnyExceptionWhenInputIsNonNull(String input) {
        // When & Then
        InputValidator.requireNonNull(input, ERROR_MESSAGE);
    }

    @Test
    @DisplayName("Should throw exception when input is null")
    void shouldThrowExceptionWhenInputIsNonNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.requireNonNull(null, ERROR_MESSAGE));
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Should not throw any exception when input is null")
    void shouldNotThrowAnyExceptionWhenInputIsNull() {
        // When & Then
        InputValidator.requireNull(null, ERROR_MESSAGE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "ID"})
    @DisplayName("Should throw exception when input is non-null")
    void shouldThrowExceptionWhenInputIsNonNull(String input) {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> InputValidator.requireNull(input, ERROR_MESSAGE));
        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }
}
