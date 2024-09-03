package subscriptionservice.api;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subscriptionservice.models.Subscription;
import subscriptionservice.services.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@AllArgsConstructor
public class SubscriptionApi {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription newSubscription = subscriptionService.newSubscription(subscription);
        return new ResponseEntity<>(newSubscription, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscription(id);
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByUser(@PathVariable String userId) {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsByUser(userId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long id) {
        subscriptionService.cancelSubscription(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/validate")
    public ResponseEntity<Boolean> isSubscriptionValid(@PathVariable Long id) {
        boolean isValid = subscriptionService.isSubscriptionValid(id);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
}
