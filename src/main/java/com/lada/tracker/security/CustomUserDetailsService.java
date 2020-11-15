package com.lada.tracker.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lada.tracker.services.models.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class CustomUserDetailsService {

    @Value("${gateway.host}")
    private String gatewayHost;

    @Value("${gateway.port}")
    private String gatewayPort;

    public Optional<CustomUserDetails> getUserFromToken(String token) {
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + gatewayHost + ":" + gatewayPort + "/auth/validate_token"))
                    .POST(HttpRequest.BodyPublishers.ofString(token))
                    .header("Authorization","Bearer " + token)
                    .build();
            HttpResponse<Supplier<Response<User>>> response = client.send(request, new JsonBodyHandler<>());
            Response<User> userResponse = response.body().get();
            if (response.statusCode() == 200 && userResponse.isSuccess()) {
                ObjectMapper objectMapper = new ObjectMapper();
                User user = objectMapper.convertValue(userResponse.getBody(), User.class);
                return Optional.of(new CustomUserDetails(user));
            }
        } catch (Exception e) { }
        return Optional.empty();
    }
}
