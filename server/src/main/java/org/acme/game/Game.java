package org.acme.game;

import org.sqids.Sqids;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Game {
    private final String id;
    private static final AtomicLong AL = new AtomicLong(2313L);
    private final String username;
    private static final Sqids SQIDS = Sqids.builder()
            .build();

    public Game(String username) {
        this.username = username;
        id = SQIDS.encode(List.of(AL.incrementAndGet()));
    }

    public String getId() {
        return id;
    }

    public static Game create(String username) {
        return new Game(username);
    }

    public String getUsername() {
        return username;
    }
}
