package org.yyyf.game.entity;

import org.java_websocket.WebSocket;

public class Player {
    public int id;
    public String name;
    private boolean alive;
    private boolean ready;
    public org.java_websocket.WebSocket conn;

    @Deprecated
    public Player(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Player(int id,String name, WebSocket conn) {
        this.id = id;
        this.name = name;
        this.conn = conn;
        this.alive = true;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }
}
