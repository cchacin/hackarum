package org.acme.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/lobby/{username}")
@ApplicationScoped
public class LobbySocket {

    Map<String, Session> sessions = new ConcurrentHashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    public enum LobbyAction {
        CREATE,
        JOIN,
    }

    public record LobbyMessage(LobbyAction action, Map<String, Object> data) {}

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
//        broadcast("User " + username + " joined");
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
//        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
//        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username)  {
        try {
            var lobbyMessage = mapper.readValue(message, LobbyMessage.class);
            System.out.println("lobbyMessage = " + lobbyMessage);
            var r = switch (lobbyMessage.action()) {
                case CREATE -> {
                    var newGame = Game.create(username);
                    Storage.GAMES.put(newGame.getId(), newGame);
                    yield Map.of(
                            "action",
                            "CREATED",
                            "data",
                            Map.of("gameId", newGame.getId())
                    );
                }
                case JOIN -> Map.of("data", "");
            };
            System.out.println("r = " + r);
            session.getAsyncRemote().sendText(mapper.writeValueAsString(r));
        } catch (JsonProcessingException e) {
            // LOG
            session.getAsyncRemote().sendText("ERROR BAD DATA");
            System.out.println("e.getMessage() = " + e.getMessage());
        }
        System.out.println("message = " + message);
//        broadcast(">> " + username + ": " + message);
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

}