package org.yyyf.game.entity;

import org.yyyf.game.tool.Vector2D;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Board {
    public enum State{
        //可以正常放棋，可以正常放棋且获胜，不能正常放棋
        ready,winning,refuse
    }
    private int id;
    private int winning_goal = 5;
    private Set<Vector2D> chess;

    public Board() {
        chess = new HashSet<>();
    }

    public Board(int id) {
        chess = new HashSet<>();
        this.id = id;
    }

    public void reset(){
        chess.clear();
    }

    public int getId() {
        return id;
    }

    public State putChess(Vector2D position){
        if (chess.contains(position)) return State.refuse;
        chess.add(position);
        return isWin(position)? State.winning : State.ready;
    }

    private boolean isWin(Vector2D position){
        //===============================================================水平
        int winning_count = 0;
        Vector2D temp = new Vector2D(position);
        boolean positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.x--;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Vector2D(position);
                    positive = false;
                    i--;
                }
            }
            else{
                temp.x++;
                if(chess.contains(temp))
                    winning_count++;
                else
                    break;
            }
            if (winning_count == winning_goal - 1) return true;
        }
        //===============================================================垂直
        winning_count = 0;
        temp = new Vector2D(position);
        positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.y--;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Vector2D(position);
                    positive = false;
                    i--;
                }
            }
            else{
                temp.y++;
                if(chess.contains(temp))
                    winning_count++;
                else
                    break;
            }
            if (winning_count == winning_goal - 1) return true;
        }
        //===============================================================斜向 /
        winning_count = 0;
        temp = new Vector2D(position);
        positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.x--;
                temp.y++;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Vector2D(position);
                    positive = false;
                    i--;
                }
            }
            else{
                temp.x++;
                temp.y--;
                if(chess.contains(temp))
                    winning_count++;
                else
                    break;
            }
            if (winning_count == winning_goal - 1) return true;
        }
        //===============================================================反斜向 \
        winning_count = 0;
        temp = new Vector2D(position);
        positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.x--;
                temp.y--;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Vector2D(position);
                    positive = false;
                    i--;
                }
            }
            else{
                temp.x++;
                temp.y++;
                if(chess.contains(temp))
                    winning_count++;
                else
                    break;
            }
            if (winning_count == winning_goal - 1) return true;
        }
        return false;
    }


}
