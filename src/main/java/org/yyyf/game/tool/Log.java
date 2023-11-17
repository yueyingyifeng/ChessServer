package org.yyyf.game.tool;

import java.util.Date;

public class Log {
    /**
     * 获取当前程序行数，类名，方法名
     *
     * @return 行数，类名，方法名
     */
    private static String getTraceInfo() {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        sb.append("class: ").append(stacks[1].getClassName())
                .append("; method: ").append(stacks[1].getMethodName())
                .append("; Line: ").append(stacks[1].getLineNumber());
        return sb.toString();
    }

    public static void i(String msg){
        System.out.println(new Date() + "> " + msg);
    }
    public static void i(String msg, int code){
        System.out.println(new Date() + "> "  + msg + "\ncode: " + code);
    }

    public static void e(){
        System.out.println("Something wrong in" + getTraceInfo());
    }
}
