package io.github.dfnabiullin.taskmanager.task.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${application.config.user-url}", path = "/api/v1/users")
public interface UserClient {
    @GetMapping("/{uuid}")
    void checkUserExists(@PathVariable("uuid") UUID uuid);
}