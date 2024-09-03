package subscriptionservice.exceptions;

public class SubscriptionAlreadyCanceledException extends RuntimeException {
    public SubscriptionAlreadyCanceledException(String message) {
        super(message);
    }
}
