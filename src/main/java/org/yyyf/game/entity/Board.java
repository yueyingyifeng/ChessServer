package org.yyyf.game.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class Board {
    public enum State{
        //可以正常放棋，可以正常放棋且获胜，不能正常放棋
        ready,winning,refuse
    }

    private int id;
    private int winning_goal = 5;
    private Set<Piece> chess;

    public Board() {
        chess = new LinkedHashSet<>();
    }

    public Board(int id) {
        chess = new HashSet<>();
        this.id = id;
    }

    public Piece getLastPiece() {
        Piece[] array = chess.toArray(new Piece[0]);
        return array[array.length-1];
    }

    public void reset(){
        chess.clear();
    }

    public void resetLastPut() {
        Iterator<Piece> iterator = chess.iterator();
        Piece lastPutPiece = null;
        while (iterator.hasNext()){
            lastPutPiece = iterator.next();
        }
        if(lastPutPiece != null)
            chess.remove(lastPutPiece);
    }

    public int getId() {
        return id;
    }

    public State putChess(Piece position){
        if (chess.contains(position)) return State.refuse;
        chess.add(position);
        return isWin(position)? State.winning : State.ready;
    }

    private boolean isWin(Piece position){
        //===============================================================水平
        int winning_count = 0;
        Piece temp = new Piece(position);
        boolean positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.x--;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Piece(position);
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
        temp = new Piece(position);
        positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.y--;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Piece(position);
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
        temp = new Piece(position);
        positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.x--;
                temp.y++;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Piece(position);
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
        temp = new Piece(position);
        positive = true;
        for (int i = 0; i < winning_goal; i++) {
            if(positive){
                temp.x--;
                temp.y--;
                if(chess.contains(temp)){
                    winning_count++;
                }
                else{
                    temp = new Piece(position);
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
