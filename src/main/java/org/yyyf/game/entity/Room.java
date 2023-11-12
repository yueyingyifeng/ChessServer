package org.yyyf.game.entity;

import com.alibaba.fastjson.JSONArray;
import org.yyyf.game.tool.Vector2D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Room {
    private final int maximumPlayer = 2;
    private final int hostId;
    private final List<Player> players;
    private final List<Board> boards;

    public Room(Player host){
        players = new ArrayList<>(maximumPlayer);
        boards = new LinkedList<>();
        players.add(host);
        boards.add(
                new Board(host.id)
        );
        hostId = host.id;
    }
    //添加玩家，如果满了就返回假
    public void addPlayer(Player player){
        if(players.size() >= maximumPlayer) return;
        players.add(player);
        boards.add(
                new Board(player.id)
        );
    }

    public boolean isRoomFull(){
        return players.size() >= maximumPlayer;
    }

    public void removePlayerById(int id){
        players.removeIf(p -> p.id == id);
    }

    public int getId() {
        return hostId;
    }

    public boolean isThisIdInTheRoom(int id) {
        for (Player p : players){
            if(p.id == id) return true;
        }
        return false;
    }

    public void sendMsgToAllPlayerNotThisOne(String msg,int id){
        for(Player p : players){
            if(p.id != id && p.conn.isOpen())
                p.conn.send(msg);
        }
    }
    public void sendMsgToAllPlayer(String msg){
        for(Player p : players)
            p.conn.send(msg);
    }
    public Board.State putAChess(int id, Vector2D position){
        for(Board board : boards){
            if(board.getId() == id){
                return board.putChess(position);
            }
        }
        return Board.State.refuse;
    }

    public int capacity() {
        return players.size();
    }

    public JSONArray getReadyPlayerIds() {
        JSONArray jsonArray = new JSONArray();
        for(Player p : players){
            if(p.isReady()){
                jsonArray.add(p.id);
            }
        }
        return jsonArray;
    }

    public boolean isAllReady() {
        for(Player p : players){
            if(!p.isReady()) return false;
        }
        return true;
    }

    public void resetReadyState() {
        for(Player p : players){
            p.setReady(false);
        }
    }

    public void resetBoard(){
        for(Board b : boards){
            b.reset();
        }
    }
}
