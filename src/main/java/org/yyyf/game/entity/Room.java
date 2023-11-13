package org.yyyf.game.entity;

import com.alibaba.fastjson.JSONArray;
import org.yyyf.game.Game;
import org.yyyf.game.manager.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final int maximumPlayer = 2;
    private final int hostId;
    private final List<Player> players;
    private final List<Board> boards;
    private final List<Integer> regretList;//悔棋名单

    public Room(Player host){
        players = new ArrayList<>(maximumPlayer);
        regretList = new ArrayList<>(maximumPlayer);
        boards = new ArrayList<>(maximumPlayer);

        players.add(host);
        boards.add(
                new Board(host.id)
        );
        hostId = host.id;
    }

    public Board findBoardById(int id){
        for(Board b : boards)
            if(b.getId() == id)
                return b;
        return null;
    }

    //添加玩家
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
    public Board.State putAChess(int id, Piece position){
        for(Board board : boards){
            if(board.getId() == id){
                return board.putChess(position);
            }
        }
        return Board.State.refuse;
    }

    public int size() {
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

    public boolean isOneOfUs(int id){
        boolean isOneOfUs = false;
        for(Player p : players)
            if(p.id == id)
                return true;
        return false;
    }

    public void someoneRegret(int id){
        if(!isOneOfUs(id)){
            Player player = PlayerManager.getInstance().findPlayerById(id);
            if(player != null)
                player.conn.send(Game.ErrorMsg());
        }

        regretList.add(id);
    }

    public void resetRegretList(){
        regretList.clear();
    }

    public void regret(boolean isMyTure,int id) {
        if(isMyTure){
            for(Board b : boards)
                b.resetLastPut();
        }
        else{
            Board board = findBoardById(id);
            board.resetLastPut();
        }
    }
    public JSONArray getRegretList(){
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(regretList);

        return jsonArray;
    }
    public boolean hasMajorityAgreedToRegret() {
            return regretList.size() > (players.size() / 2);
    }
}
