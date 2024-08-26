package subscriptionservice.subscription;

public class SubscriptionAlreadyExistsException extends RuntimeException {
    public SubscriptionAlreadyExistsException(String s) {
        super(s);
    }
}
