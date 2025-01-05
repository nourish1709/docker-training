package com.nourish1709.learning.configservice.api;

import com.nourish1709.learning.configservice.aspect.RefreshSettings;
import com.nourish1709.learning.configservice.errors.PropertyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/settings")
public class SettingsController {

    private final StringRedisTemplate redisTemplate;

    @GetMapping("/{application}")
    public Map<Object, Object> getAllProperties(@PathVariable String application) {
        return redisTemplate.opsForHash().entries(application);
    }

    @GetMapping("/{application}/{key}")
    public String getProperty(@PathVariable String application, @PathVariable String key) {
        return Optional.ofNullable(redisTemplate.opsForHash().get(application, key))
                .map(Objects::toString)
                .orElseThrow(() -> new PropertyNotFoundException("Could not find property with key: \"%s\" for application: \"%s\""
                        .formatted(key, application)));
    }

    @RefreshSettings
    @PostMapping("/{application}/{key}/{value}")
    public void setProperty(@PathVariable String application, @PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForHash().put(application, key, value);
    }
}
