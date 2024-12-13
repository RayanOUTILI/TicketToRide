package fr.cotedazur.univ.polytech.ttr.equipeb.exceptions;

public class ExceedMaxWagonsException extends RuntimeException {
    public ExceedMaxWagonsException(String message) {
        super(message);
    }
}