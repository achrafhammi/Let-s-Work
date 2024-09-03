package subscriptionservice.exceptions;

public class SubscriptionAlreadyActivatedException extends RuntimeException {
    public SubscriptionAlreadyActivatedException(String s) {
        super(s);
    }
}
