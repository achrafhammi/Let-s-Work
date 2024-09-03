package subscriptionservice.services;

import subscriptionservice.models.Subscription;

import java.util.List;

public interface SubscriptionService {
    Subscription newSubscription(Subscription subscription);
    Subscription getSubscription(Long id);
    List<Subscription> getAllSubscriptions();
    List<Subscription> getAllSubscriptionsByUser(String userId);
    boolean isSubscriptionValid(Long subscriptionId);
    boolean cancelSubscription(Long id);
    boolean activateSubscription(Long id);
}
