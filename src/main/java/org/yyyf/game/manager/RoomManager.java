package org.yyyf.game.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.yyyf.game.entity.Player;
import org.yyyf.game.entity.Room;

import java.util.LinkedList;
import java.util.List;

public class RoomManager {
    private static RoomManager instance;

    private List<Room> rooms;

    private RoomManager(){
        rooms = new LinkedList<>();
    }

    public static RoomManager getInstance() {
        if(instance == null){
            instance = new RoomManager();
        }
        return instance;
    }

    public Room findRoomById(int id){
        for(Room room : rooms)
            if(room.isThisIdInTheRoom(id))
                return room;
        return null;
    }

    public void createRoom(Player host){
        rooms.add(
                new Room(host)
        );
    }

    public void removeRoomById(int id){
        rooms.removeIf(room -> room.getId() == id);
    }

    public synchronized JSONArray getRoomList(){
        JSONArray jsonArray = new JSONArray();
        for(Room room : rooms){
            JSONObject jsonObject = new JSONObject();//导致的原因:每次都需要new一个对象，不然总是同一个引用
            jsonObject.put("id",room.getId());

            Player player = PlayerManager.getInstance().findPlayerById(room.getId());
            if(player != null){
                jsonObject.put("name",player.name);
                jsonArray.add(jsonObject);
            }
        }

        return jsonArray;
    }
}
