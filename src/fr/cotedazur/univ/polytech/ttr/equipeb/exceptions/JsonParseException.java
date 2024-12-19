package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

/**
 * Exception personnalisée pour la gestion des erreurs de parsing JSON.
 */
public class JsonParseException extends Exception {
    public JsonParseException(String message) {
        super(message);
    }

    public JsonParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
