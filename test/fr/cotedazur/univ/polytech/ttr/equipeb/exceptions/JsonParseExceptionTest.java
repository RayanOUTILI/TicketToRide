package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonParseExceptionTest {

    @Test
    void jsonParseExceptionWithMessage() {
        JsonParseException exception = new JsonParseException("Error message");
        assertEquals("Error message", exception.getMessage());
    }

    @Test
    void jsonParseExceptionWithMessageAndCause() {
        Throwable cause = new Throwable("Cause message");
        JsonParseException exception = new JsonParseException("Error message", cause);
        assertEquals("Error message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}