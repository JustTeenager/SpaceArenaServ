package com.mygdx.game;

import com.esotericsoftware.kryonet.Connection;
import java.util.Timer;

public class RoomClass {

    private int classNumber;
    private boolean full;
    private int count;
    private Connection firstPlayerConnection;
    private Connection secondPlayerConnection;
    private Timer timer;

    private TimerPlay timerPlay;

    public RoomClass(){
        count=0;
        full=false;
        timerPlay=new TimerPlay();
    }

    public void enterConnection(Connection connection) {
        if (firstPlayerConnection == null) {
            firstPlayerConnection = connection;
            System.out.println("FIRST PLAYER CONNECTED");
            count++;
        } else {
            secondPlayerConnection = connection;
            System.out.println("SECOND PLAYER CONNECTED");
            count++;
        }
        if (count == 2) {
            timer = new Timer();
            timer.schedule(timerPlay, 0, 1000);
            full = true;

            System.out.println("ROOM IS FULL");
        }
    }

    public void getReceived(Connection c, Object p){
        if (full) {
            if (p instanceof MessageBox) {
                if (c.getID()==firstPlayerConnection.getID())
                Serv.server.sendToTCP(secondPlayerConnection.getID(), p);
                else if (c.getID()==secondPlayerConnection.getID()) Serv.server.sendToTCP(firstPlayerConnection.getID(),p);
            }

            if (p instanceof PlayerNameBox) {
                if (c.getID()==firstPlayerConnection.getID())
                    Serv.server.sendToTCP(secondPlayerConnection.getID(), p);
                else if (c.getID()==secondPlayerConnection.getID()) Serv.server.sendToTCP(firstPlayerConnection.getID(),p);
            }


            if (p instanceof PlayersWaitingBox) {
                PlayersWaitingBox box = (PlayersWaitingBox) p;
                box.count = count;
                if (c.getID()==firstPlayerConnection.getID())
                    Serv.server.sendToUDP(firstPlayerConnection.getID(), p);
                else if (c.getID()==secondPlayerConnection.getID()) Serv.server.sendToUDP(secondPlayerConnection.getID(),p);
                //Serv.server.sendToUDP(c.getID(), box);
            }
            if (p instanceof CoordBox) {
                CoordBox box = (CoordBox) p;
                if (box.getPlayerIdentify() == -1) {
                    box.Btime = timerPlay.getTime();
                    System.out.println(box.Btime);
                    box.seconds = timerPlay.getSeconds();
                    box.setPlayerIdentify(c.getID());
                    if (c.getID()==firstPlayerConnection.getID())
                        Serv.server.sendToTCP(firstPlayerConnection.getID(), box);
                    else if (c.getID()==secondPlayerConnection.getID()) Serv.server.sendToTCP(secondPlayerConnection.getID(),box);
                    //Serv.server.sendToTCP(c.getID(), box);
                    System.out.println("Пакет coord отправлен,playersID= " + c.getID()%2+"       "+c.getID());
                    //playersID++;
                } else {
                    //И здесь добавим коробку
                    box.Btime = timerPlay.getTime();
                    box.seconds = timerPlay.getSeconds();
                    if (c.getID()==firstPlayerConnection.getID())
                        Serv.server.sendToTCP(secondPlayerConnection.getID(), p);
                    else if (c.getID()==secondPlayerConnection.getID()) Serv.server.sendToTCP(firstPlayerConnection.getID(),p);
                    //Serv.server.sendToAllExceptTCP(c.getID(), box);
                    //server.sendToTCP(c.getID(),box);
                }
            }
        }
    }

    public void disconnetced(Connection c){
        count=0;
        full=false;
        timerPlay.setSeconds(120);
        timerPlay.setTime("-1");
        PlayersWaitingBox pw=new PlayersWaitingBox();
        pw.count=-1;
        if (c.getID()==firstPlayerConnection.getID())
            Serv.server.sendToTCP(secondPlayerConnection.getID(), pw);
        else if (c.getID()==secondPlayerConnection.getID()) Serv.server.sendToTCP(firstPlayerConnection.getID(),pw);
        //Serv.server.sendToAllExceptTCP(c.getID(),pw);
        try{
            timer.cancel();
        }
        catch(Exception e){}
        System.out.println("DISCONNECTED");
    }

    public boolean isFull() {
        return full;
    }

    public int getCount() {
        return count;
    }

    public Connection getFirstPlayerConnection() {
        return firstPlayerConnection;
    }

    public Connection getSecondPlayerConnection() {
        return secondPlayerConnection;
    }

    public Timer getTimer() {
        return timer;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }
}
