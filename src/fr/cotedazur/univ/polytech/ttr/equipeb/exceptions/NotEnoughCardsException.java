package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

/**
 * Exception thrown when a player does not have enough wagon cards to claim a route.
 */
public class NotEnoughCardsException extends RuntimeException {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message to explain the exception.
     */
    public NotEnoughCardsException(String message) {
        super(message);
    }
}
