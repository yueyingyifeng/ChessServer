package org.yyyf.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.yyyf.game.Game;
import org.yyyf.game.entity.Player;
import org.yyyf.game.manager.PlayerManager;
import org.yyyf.game.tool.Log;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class ChessWebSocket extends WebSocketServer {
    Game game;


    public ChessWebSocket(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
        game = new Game();
    }

    public ChessWebSocket(InetSocketAddress address) {
        super(address);
        game = new Game();
    }

    @Override
    public void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {
        Log.i("onOpen:" + conn.getRemoteSocketAddress().toString());
    }

    @Override
    public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {
        Log.i("onClose:" + "code: " + code + " ,reason: " + reason);
        Player player = PlayerManager.getInstance().findPlayerByConn(conn);
        if (player != null){
            Log.i("onClose remove player who already leave");
            PlayerManager.getInstance().removePlayerById(player.id);
        }
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket conn, String message) {
        Log.i("Msg: " + message);
        JSONObject json = JSON.parseObject(message);
        try {
            if (json.get("type") == null)
                throw new Exception("Wrong Client Data");

            switch (game.analyze(json.getIntValue("type"))) {
                case LoginServer:
                    game.playerLogIn(json.getJSONObject("data"), conn);
                    break;
                case LeaveServer:
                    game.playerLeave(json.getJSONObject("data"));
                    break;
                case LeaveRoom:
                    game.playerLeaveRoom(json.getJSONObject("data"));
                    break;
                case JoinARoom:
                    game.playerJoinARoom(json.getJSONObject("data"));
                    break;
                case CreateRoom:
                    game.createRoom(json.getJSONObject("data"));
                    break;
                case PutChess:
                    game.putChess(json.getJSONObject("data"));
                    break;
                case HeartBeat:
                    game.receiveHeartBeat(json.getJSONObject("data"));
                    break;
                case GameReadyToStart:
                    game.gameReadyToStart(json.getJSONObject("data"));
                    break;
                case GetPlayerAndRoomList:
                    game.sendPlayerAndRoomList(json.getJSONObject("data"));
                    break;

                case Error:
                default:
                    if (conn != null && conn.isOpen()) {
                        Log.i("Error Msg From:\n" + conn.getRemoteSocketAddress().toString());
                        conn.send(game.ErrorMsg());
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(org.java_websocket.WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }


    @Override
    public void onStart() {
        Log.i("Chess Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
        checkHeartBeatPackage();
    }

    private void checkHeartBeatPackage(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                game.checkIsSomeOneDead();
            }
        };
        timer.schedule(task,30 * 1000, 60 * 1000);
    }
}