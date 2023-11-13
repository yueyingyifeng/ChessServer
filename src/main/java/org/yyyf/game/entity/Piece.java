package org.yyyf.game.entity;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

public class Piece {
    public int x,y;
    public int no;
    public int id;
    public Piece(int id,int x, int y, int no) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.no = no;
    }
    //浅拷贝构造函数
    public Piece(Piece v){
        this(-1,v.x,v.y,-1);
    }

    //必须重写equals和hashCode才能使Set<E>.contains()为真
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return x == piece.x && y == piece.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x",x);
        jsonObject.put("y",y);
        return jsonObject;
    }
}
