package subscriptionservice.api;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello-world")
@AllArgsConstructor
public class TestAPI {

    @GetMapping
    public String helloWorld() {
        return "hello-world";
    }
}
