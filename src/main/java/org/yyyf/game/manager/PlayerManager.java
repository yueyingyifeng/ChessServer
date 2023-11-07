package org.yyyf.game.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.yyyf.game.entity.Player;
import org.yyyf.game.tool.Code;
import org.yyyf.game.tool.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager instance;
    private List<Player> playerList = null;
    private Map<Integer,Integer> playerAndRoom;     //记录玩家所在房间
    private PlayerManager() {
        playerList = new LinkedList<>();
        playerAndRoom = new HashMap<>();
    }

    public static PlayerManager getInstance() {
        if(instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    public void addPlayer(Player player){
        playerList.add(player);
    }

    public Player findPlayerById(int id){
        for(Player p : playerList)
            if(p.id == id) return p;
        return null;
    }

    public void removePlayerById(int id){
        playerList.removeIf(player -> player.id == id);
        PlayerIdManager.getInstance().releasePlayerId(id);
    }

    public Player findPlayerByConn(org.java_websocket.WebSocket conn){
        for(Player player: playerList){
            if(player.conn.equals(conn)){
                return player;
            }
        }
        return null;
    }

    public synchronized void removeDeadPlayer(){
        Log.i("Check dead");
        List<Player> playersToRemove = new LinkedList<>();

        for(Player player : playerList){
            if(!player.isAlive()){
                if(player.conn.isOpen()){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", Code.ReceivingHeartBeatTooLong);
                    player.conn.send(jsonObject.toString());
                }
                Log.i("Remove dead player: " + player.name);
                playersToRemove.add(player);
            }
        }

        // 现在在循环之外删除已标记为删除的玩家
        playerList.removeAll(playersToRemove);

    }

    public JSONArray getPlayerList(){
        JSONArray jsonArray = new JSONArray();
        for(Player player : playerList){
            jsonArray.add(player.name);
        }

        return jsonArray;
    }

    public void sendAllPlayer(String msg){
        for(Player player : playerList){
            if(player.conn.isOpen())
                player.conn.send(msg);
        }
    }

    //记录玩家id与房间id，K是玩家id，V是房间id
    public void recordPlayerAndRoom(int hostId, int guestId) {
        playerAndRoom.put(guestId,hostId);
    }

    public int findPlayerRoomId(int id){
        return playerAndRoom.get(id);
    }

    public synchronized void setAllPlayerDead() {
        for(Player p: playerList)
            p.setAlive(false);
    }

    public void removePlayerFromRoomById(int leaveId) {
        playerAndRoom.remove(leaveId);
    }
}
