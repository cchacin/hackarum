package org.acme.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Storage {
    Map<String, Game> GAMES = new ConcurrentHashMap<>();
}
