package com.mygdx.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

public class Serv extends Listener {
	static Server server;
	static int udpPort = 54555; // Поставить другой порт
	static int tcpPort = 54555; // Порт на котором будет работать наш сервер
	static int playersID=1;
	static boolean isTimerStarted;
	private static int count=0;
	private boolean full;
	private static ArrayList<Connection> connectionsArray;

	static Timer timer;

	public static void main(String[] args) throws Exception {
		connectionsArray=new ArrayList<>();
		//Создаем сервер
		server = new Server();
		System.out.println("Сервер создан");

		isTimerStarted=false;


		//Регистрируем пакет класс
		server.getKryo().register(MessageBox.class);
		server.getKryo().register(CoordBox.class);
		server.getKryo().register(com.badlogic.gdx.math.Vector2.class);
		server.getKryo().register(com.badlogic.gdx.math.Rectangle.class);
		server.getKryo().register(PlayersWaitingBox.class);
		server.getKryo().register(PlayerNameBox.class);
		//Регистрируем порт
		server.bind(tcpPort,udpPort);
		server.start();
		server.addListener(new Serv());
	}

	//Используется когда клиент подключается к серверу
	public void connected(Connection c){
		connectionsArray.add(c);
		count=connectionsArray.size();
		if (count==2) {
			timer=new Timer();
			timer.schedule(new TimerPlay(), 0, 1000);
		}
		System.out.println("Соединение установлено");
		/*MessageBox message = new MessageBox(); //Создаем сообщения пакета.
		message.message = "Сейчас время: "+new Date().getHours()+":"+new Date().getMinutes(); //Пишем текст который будем отправлять клиенту.
		c.sendTCP(message); // Так же можно отправить через UDP c.sendUDP(packetMessage);*/
	}

	//Используется когда клиент отправляет пакет серверу
	public void received(Connection c, Object p){
		System.out.println(TimerPlay.time);
		System.out.println(TimerPlay.seconds);

		if (p instanceof MessageBox){
			server.sendToAllExceptTCP(c.getID(),p);
		}

		if (p instanceof PlayerNameBox){
			server.sendToAllExceptTCP(c.getID(),p);
		}


		if (p instanceof PlayersWaitingBox){
			PlayersWaitingBox box=(PlayersWaitingBox) p;
			box.count=count;
			server.sendToUDP(c.getID(),box);
		}
		if (p instanceof CoordBox){
			CoordBox box=(CoordBox) p;
			if (box.getPlayerIdentify()==0) {
				box.Btime=TimerPlay.time;
				System.out.println(box.Btime);
				box.seconds=TimerPlay.seconds;
				box.setPlayerIdentify(playersID);
				server.sendToTCP(c.getID(),box);
				System.out.println("Пакет coord отправлен,playersID= "+playersID);
				playersID++;
			}
			else {
				//И здесь добавим коробку
				box.Btime=TimerPlay.time;
				box.seconds=TimerPlay.seconds;
				//System.out.println("Another coord pack received from " +c.getID());
				//System.out.println(box.BpositionPlayer.x+"                            "+box.BpositionPlayer.y);
				server.sendToAllExceptTCP(c.getID(),box);
				//server.sendToTCP(c.getID(),box);
			}
		}
	}

	//Используется когда клиент покидает сервер
	public void disconnected(Connection c){
		count=0;
		playersID=1;
		TimerPlay.seconds=80;
		TimerPlay.time="-1";
		PlayersWaitingBox pw=new PlayersWaitingBox();
		pw.count=-1;
		try {
			connectionsArray.remove(0);
		}catch (Exception e){}
		try {
			connectionsArray.remove(1);
		}catch (Exception e){}
		server.sendToAllExceptTCP(c.getID(),pw);
		try{
			timer.cancel();
		}
		catch(Exception e){}
		System.out.println("DISCONNECTED");
		System.out.println(TimerPlay.time);
		System.out.println(TimerPlay.seconds);
	}
}