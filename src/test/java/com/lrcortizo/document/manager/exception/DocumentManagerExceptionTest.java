package com.lrcortizo.document.manager.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentManagerExceptionTest {

    private static final String TEST_MSG = "Test message";

    @Test
    void given_errorWithMsg_when_throwDocumentManagerException_then_assertException() {
        // Given
        // When
        final Exception exception = assertThrows(RuntimeException.class,
                () -> this.throwDocumentManagerException(TEST_MSG));

        // Then
        assertEquals(TEST_MSG, exception.getMessage());
        assertTrue(exception instanceof DocumentManagerException);
    }

    @Test
    void given_errorWithThrowable_when_throwDocumentManagerException_then_assertException() {
        // Given
        final Throwable innerException = new NullPointerException(TEST_MSG);

        // When
        final Exception exception = assertThrows(RuntimeException.class,
                () -> this.throwDocumentManagerException(innerException), "Target class doesn't extend HttpException");

        // Then
        assertEquals("java.lang.NullPointerException: " + TEST_MSG, exception.getMessage());
        assertEquals(TEST_MSG, exception.getCause().getMessage());
        assertTrue(exception instanceof DocumentManagerException);
    }

    private void throwDocumentManagerException(final String message) {
        throw new DocumentManagerException(message);
    }

    private void throwDocumentManagerException(final Throwable throwable) {
        throw new DocumentManagerException(throwable);
    }
}
