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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/game/{gameId}/user/{username}")
@ApplicationScoped
public class GameSocket {

    Map<String, Session> sessions = new ConcurrentHashMap<>();
    ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("gameId") String gameId,
            @PathParam("username") String username
    ) {
        broadcast("User " + username + " joined game: " + gameId);
        sessions.put(username, session);
    }

    @OnClose
    public void onClose(
            Session session,
            @PathParam("gameId") String gameId,
            @PathParam("username") String username
    ) {
        sessions.remove(username);
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(
            Session session,
            @PathParam("gameId") String gameId,
            @PathParam("username") String username,
            Throwable throwable
    ) {
        sessions.remove(username);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(
            String message,
            Session session,
            @PathParam("gameId") String gameId,
            @PathParam("username") String username
    )  {
//        try {
//            var lobbyMessage = mapper.readValue(message, LobbyMessage.class);
//            session.getAsyncRemote().sendText(mapper.writeValueAsString(r));
//        } catch (JsonProcessingException e) {
            // LOG
//            session.getAsyncRemote().sendText("ERROR BAD DATA");
//            System.out.println("e.getMessage() = " + e.getMessage());
//        }
        System.out.println("message = " + message);
        broadcast(">> " + username + ": " + message);
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