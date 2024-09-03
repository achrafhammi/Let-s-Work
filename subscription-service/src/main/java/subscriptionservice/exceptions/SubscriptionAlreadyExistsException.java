package subscriptionservice.exceptions;

public class SubscriptionAlreadyExistsException extends RuntimeException {
    public SubscriptionAlreadyExistsException(String s) {
        super(s);
    }
}
