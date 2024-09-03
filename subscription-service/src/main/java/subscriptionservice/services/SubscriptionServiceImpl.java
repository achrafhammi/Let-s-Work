package subscriptionservice.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subscriptionservice.exceptions.SubscriptionAlreadyActivatedException;
import subscriptionservice.exceptions.SubscriptionAlreadyCanceledException;
import subscriptionservice.exceptions.SubscriptionAlreadyExistsException;
import subscriptionservice.models.Subscription;
import subscriptionservice.repo.SubscriptionRepository;

import java.time.LocalDateTime;
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
        return subscriptionRepository.findAll();
    }

    @Override
    public List<Subscription> getAllSubscriptionsByUser(String userId) {
        return subscriptionRepository.findSubscriptionsByUserId(userId);
    }

    @Override
    public boolean isSubscriptionValid(Long subscriptionId) {
        Subscription subscriptionToValid = subscriptionRepository.findById(subscriptionId).get();

        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(subscriptionToValid.getStartDate()) && now.isBefore(subscriptionToValid.getEndDate());
    }

    @Override
    public boolean cancelSubscription(Long subscriptionId) {
        Subscription subscriptionToCancel = subscriptionRepository.findById(subscriptionId).get();
        if(!subscriptionToCancel.isStatus()){
            throw new SubscriptionAlreadyCanceledException("Subscription is already canceled");
        }
        subscriptionToCancel.setStatus(false);
        return false;
    }

    @Override
    public boolean activateSubscription(Long id) {
        Subscription subscriptionToActivate = subscriptionRepository.findById(id).get();
        if(subscriptionToActivate.isStatus()){
            throw new SubscriptionAlreadyActivatedException("Subscription is already canceled");
        }
        LocalDateTime now = LocalDateTime.now();

        if(now.isAfter(subscriptionToActivate.getStartDate()) && now.isBefore(subscriptionToActivate.getEndDate())){
            subscriptionToActivate.setStatus(true);
        }
        return true;
    }
}
