package dev.salmonllama.fsbot.services;

import java.util.UUID;

public class IdService {

    public String generateShort() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    public String generate() {
        return UUID.randomUUID().toString();
    }
}
