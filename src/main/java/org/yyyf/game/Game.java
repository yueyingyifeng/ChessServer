package org.yyyf.game;

import com.alibaba.fastjson.JSONObject;
import org.yyyf.game.entity.Board;
import org.yyyf.game.entity.Player;
import org.yyyf.game.entity.Room;
import org.yyyf.game.manager.PlayerIdManager;
import org.yyyf.game.manager.PlayerManager;
import org.yyyf.game.manager.RoomManager;

import org.java_websocket.WebSocket;
import org.yyyf.game.tool.Code;
import org.yyyf.game.tool.Log;
import org.yyyf.game.tool.Vector2D;

public class Game {



    public enum Type{
        LeaveServer, JoinARoom, CreateRoom, PutChess, Error, LeaveRoom, HeartBeat, LoginServer,GameReadyToStart,GetPlayerAndRoomList,Restart
    }
    public Type analyze(int type) {
        switch (type){
            case Code.LeaveServer:
                return Type.LeaveServer;
            case Code.LeaveRoom:
                return Type.LeaveRoom;
            case Code.LoginServer:
                return Type.LoginServer;
            case Code.JoinARoom:
                return Type.JoinARoom;
            case Code.PutChess:
                return Type.PutChess;
            case Code.CreateRoom:
                return Type.CreateRoom;
            case Code.GameReadyToStart_ToServer:
                return Type.GameReadyToStart;
            case Code.HeartBeat:
                return Type.HeartBeat;
            case Code.GetPlayerAndRoomList:
                return Type.GetPlayerAndRoomList;
            case Code.Restart:
                return Type.Restart;
            default:
                return Type.Error;
        }
    }
    PlayerManager playerManager;
    RoomManager roomManager;


    public Game() {
        playerManager = PlayerManager.getInstance();
        roomManager = RoomManager.getInstance();
    }

    public void sendPlayerAndRoomList(JSONObject data) {
        Integer id = data.getInteger("id");
        if(id != null && id != -1){
            Player player = playerManager.findPlayerById(id);
            player.conn.send(getPlayerList().toString());
            player.conn.send(getRoomList().toString());
        }
    }

    public JSONObject getPlayerList() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",Code.PlayerList);//玩家列表
        jsonObject.put("data",playerManager.getPlayerList());
        return  jsonObject;
    }

    public JSONObject getRoomList(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",Code.RoomList);//房间列表
        jsonObject.put("data",roomManager.getRoomList());
        return jsonObject;
    }

    public void playerJoinARoom(JSONObject data) {
        int hostId = data.getIntValue("hostId");        //要加入房间的id
        int guestId = data.getIntValue("guestId");      //加入者的id
        Player player = playerManager.findPlayerById(guestId);
        Room room = roomManager.findRoomById(hostId);

        if(room.isRoomFull()){
            sleep(500);
            sendPermissionToPlayer(player,!room.isRoomFull(),Code.AcceptJoin);
            sendPlayerAndRoomList();
            return;
        }
        sendPermissionToPlayer(player,!room.isRoomFull(),Code.AcceptJoin);

        room.addPlayer(player);
        playerManager.recordPlayerAndRoom(hostId,guestId);   //记录玩家所在房间

        JSONObject idPackage = new JSONObject();
        idPackage.put("id",guestId);
        idPackage.put("name",player.name);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",Code.SomeOneJoinARoom);
        jsonObject.put("data",idPackage);

        room.sendMsgToAllPlayerNotThisOne(jsonObject.toString(),guestId);
    }


    public void createRoom(JSONObject data) {
        int id = data.getIntValue("id");
        Player player = playerManager.findPlayerById(id);
        playerManager.recordPlayerAndRoom(id,id);
        sendPermissionToPlayer(player,true,Code.Accept);

        roomManager.createRoom(player);
        sendPlayerAndRoomList();
    }

    public void putChess(JSONObject data) {
        JSONObject v = data.getJSONObject("position");
        int id = data.getIntValue("id");
        Room room = roomManager.findRoomById(id);
        Player player = playerManager.findPlayerById(id);
        Vector2D position = new Vector2D(v.getInteger("x"),v.getIntValue("y"));

        sendPermissionToPlayer(player,true,Code.Accept);



        Board.State state = room.putAChess(id, position);
        switch (state){
            case winning:
                JSONObject winningPackage = new JSONObject();
                winningPackage.put("type",Code.Winning);
                winningPackage.put("winningId",id);
                room.sendMsgToAllPlayer(winningPackage.toString());

            case ready:
                JSONObject chess = new JSONObject();
                chess.put("id",id);
                chess.put("position",position.toJSON());

                JSONObject chessPackage = new JSONObject();
                chessPackage.put("type",Code.SomeOnePutAChess);
                chessPackage.put("data",chess);
                room.sendMsgToAllPlayerNotThisOne(chessPackage.toString(),id);
                sendPermissionToPlayer(player,true,Code.Accept);
                break;
            case refuse:
            default:
                sendPermissionToPlayer(player,false,Code.Accept);
        }

    }




    public void playerLeaveRoom(JSONObject data){
        int leaveId = data.getIntValue("id");
        Room room = roomManager.findRoomById(playerManager.findPlayerRoomId(leaveId));

        try{

            if(room == null){
//                Thread.sleep(1000);
                sendPlayerAndRoomList();
                playerManager.removePlayerFromRoomById(leaveId);
            }
            if(room != null && leaveId == room.getId()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type",Code.HostLeave);
                jsonObject.put("id",leaveId);
                room.sendMsgToAllPlayerNotThisOne(jsonObject.toString(),leaveId);
                roomManager.removeRoomById(leaveId);
//                Thread.sleep(1000);
                sendPlayerAndRoomList();
                return;
            }
            else if(room != null){
                room.removePlayerById(leaveId);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type",Code.SomeOneLeaveRoom);
                jsonObject.put("data",idPackage(leaveId));
                room.sendMsgToAllPlayerNotThisOne(jsonObject.toString(),leaveId);
                room.resetBoard();
            }

            if(room != null && room.capacity() == 0){
                roomManager.removeRoomById(room.getId());
            }
            playerManager.removePlayerFromRoomById(leaveId);
//            Thread.sleep(1000);
            sendPlayerAndRoomList();


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void receiveHeartBeat(JSONObject data) {
        int id = data.getIntValue("id");
        Player player = playerManager.findPlayerById(id);
        player.setAlive(true);
    }

    public void gameReadyToStart(JSONObject data) {
        int hostId = data.getIntValue("id");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",Code.GameReadyToStart_ToClient);
        jsonObject.put("whoFirst",!data.getBooleanValue("whoFirst"));

        Room room = roomManager.findRoomById(hostId);
        room.sendMsgToAllPlayerNotThisOne(jsonObject.toString(),hostId);
    }

    public void restartRequest(JSONObject data) {
        Integer id = data.getInteger("id");
        if(id == null){
            Log.e();
            return;
        }
        Player player = playerManager.findPlayerById(id);
        player.setReady(true);
        //第一步：通过id获取玩家，然后将此玩家准备状态设为真（准备状态在玩家创建后一直为真，第一次加入房间时也应该一直为真）
        //第二步：通过room返回房间内玩家的准备状态为真的列表

        //预后：游戏结束，玩家离开房间，都需要将玩家的状态设为真，确保下次加入房间时可以直接开始游戏

        Room room = roomManager.findRoomById(playerManager.findPlayerRoomId(id));
        JSONObject d = new JSONObject();
        d.put("ids",room.getReadyPlayerIds());
        d.put("allReady",room.isAllReady());
        JSONObject result = new JSONObject();
        result.put("type",Code.RestartList);
        result.put("data",d);
        room.sendMsgToAllPlayer(result.toString());
        if(room.isAllReady()){
            room.resetReadyState();
            room.resetBoard();
        }
    }

    public void playerLogIn(JSONObject data, WebSocket conn) {
        int id = PlayerIdManager.getInstance().getNewPlayerId();
        String name = data.getString("name");
        Player player = new Player(id,name,conn);
        playerManager.addPlayer(player);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",Code.GiveIdToPlayer);
        jsonObject.put("id",id);


        player.conn.send(jsonObject.toString());
        sendPlayerAndRoomList();
    }

    public void playerLeave(JSONObject data) {
        int id = data.getInteger("id");
        playerManager.removePlayerById(id);
        PlayerIdManager.getInstance().releasePlayerId(id);
        sendPlayerAndRoomList();
    }

    public synchronized void checkIsSomeOneDead(){
        playerManager.removeDeadPlayer();
        playerManager.setAllPlayerDead();
    }

    public String ErrorMsg(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",Code.WrongClientMsg);
        return jsonObject.toString();
    }

    private void sendPermissionToPlayer(Player player, boolean permit, int code){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",code);
        jsonObject.put("accept",permit);
        player.conn.send(jsonObject.toString());
    }


    private void sendPlayerAndRoomList(){
        playerManager.sendAllPlayer(getPlayerList().toString());
        playerManager.sendAllPlayer(getRoomList().toString());
    }

    private JSONObject idPackage(int id){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        return jsonObject;
    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
// 8197 3956