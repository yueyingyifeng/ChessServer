package org.yyyf.game.tool;

import java.util.Date;

public class Log {
    public static void i(String msg){
        System.out.println(new Date() + "> " + msg);
    }
    public static void i(String msg, int code){
        System.out.println(new Date() + "> "  + msg + "\ncode: " + code);
    }
}
