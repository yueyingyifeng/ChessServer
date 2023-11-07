package org.yyyf.game.tool;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

public class Vector2D {
    public int x,y;
    public Vector2D() {
        x = -1;
        y = -1;
    }
    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    //浅拷贝构造函数
    public Vector2D(Vector2D v){
        this(v.x,v.y);
    }

    //必须重写equals和hashCode才能使Set<E>.contains()为真
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return x == vector2D.x && y == vector2D.y;
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
