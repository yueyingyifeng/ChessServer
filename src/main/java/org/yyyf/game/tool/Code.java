package org.yyyf.game.tool;

public class Code {
    public static final int LeaveServer = 100;
    public static final int LeaveRoom = 101;
    public static final int LoginServer = 102;
    public static final int JoinARoom = 103;
    public static final int PutChess = 104;
    public static final int CreateRoom = 105;
    public static final int GameReadyToStart_ToServer = 106;    //游戏准备开始，房主决定谁先谁后
    public static final int GetPlayerAndRoomList = 107;
    public static final int Restart = 108;
    public static final int Regret = 109;
    public static final int HeartBeat = 120;
    public static final int WrongClientMsg = 200;
    public static final int RoomList = 201;
    public static final int PlayerList = 202;
    public static final int GameReadyToStart_ToClient = 203;    //游戏准备开始，房主决定谁先谁后
    public static final int SomeOneJoinARoom = 204;
    public static final int SomeOnePutAChess = 205;
    public static final int HostLeave = 206;
    public static final int SomeOneLeaveRoom = 207;
    public static final int RestartList = 232;
    public static final int Winning = 233;
    public static final int Accept = 234;
    public static final int GiveIdToPlayer = 235;
    public static final int AcceptJoin = 236;
    public static final int ReceivingHeartBeatTooLong = 250;
}
