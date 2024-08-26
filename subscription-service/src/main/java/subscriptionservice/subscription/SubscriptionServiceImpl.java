package subscriptionservice.subscription;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription newSubscription(Subscription subscription) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findById(subscription.getId());
        if (existingSubscription.isPresent()) {
            throw new SubscriptionAlreadyExistsException("A subscription with these details already exists.");
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscription(Long id) {
        return subscriptionRepository.findById(id).orElseThrow(() -> new SubscriptionAlreadyExistsException("Subscription with id: "+ id + "doesn't exist!"));
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return List.of();
    }

    @Override
    public List<Subscription> getAllSubscriptionsByUser(String userId) {
        return List.of();
    }

    @Override
    public boolean isSubscriptionValid(Long subscriptionId) {
        return false;
    }

    @Override
    public void cancelSubscription(Long id) {

    }
}
