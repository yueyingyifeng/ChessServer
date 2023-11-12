package org.yyyf;

import org.yyyf.game.manager.PlayerManager;
import org.yyyf.game.manager.RoomManager;
import org.yyyf.game.tool.Log;
import org.yyyf.net.ChessWebSocket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String ip = "localhost";
        if(args.length >= 1 && args[0] != null) ip = args[0];
        Log.i("Listen on " + ip);
        ChessWebSocket socket = new ChessWebSocket(new InetSocketAddress(ip,9900));
        socket.start();

        new Thread(()->{
            Scanner input = new Scanner(System.in);
           while (true){
               int code = input.nextInt();
               String list = "";
               switch (code){
                   case 1:
                       list ="player list: " +  PlayerManager.getInstance().getPlayerList().toString();
                       break;
                   case 2:
                       list ="room list: " + RoomManager.getInstance().getRoomList().toString();
                       break;
                   case -1:
                        System.exit(1);
                       break;
                   case 0:
                   default:
                       System.out.println("0:help\n1:check player list\n2:check room list");
               }
               System.out.println();
               System.out.println(list);
               System.out.println();
           }
        }).start();
    }
}