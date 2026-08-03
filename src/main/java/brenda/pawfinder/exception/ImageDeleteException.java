package brenda.pawfinder.exception;

public class ImageDeleteException extends RuntimeException {
    public ImageDeleteException(String message) {
        super(message);
    }

    public ImageDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

}
