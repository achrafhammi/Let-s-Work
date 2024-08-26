package subscriptionservice.subscription;

public class SubscriptionNotExistException extends Exception{
    public SubscriptionNotExistException(String s) {
        super(s);
    }
}
