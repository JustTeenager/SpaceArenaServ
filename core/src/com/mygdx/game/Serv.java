package com.mygdx.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.util.ArrayList;
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
	private static ArrayList<RoomClass> roomClasses;
	static Timer timer;

	public static void main(String[] args) throws Exception {
		connectionsArray=new ArrayList<>();
		roomClasses=new ArrayList<>();
		RoomClass roomClassBegin=new RoomClass();
		roomClassBegin.setClassNumber(1);
		roomClasses.add(roomClassBegin);
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
		Boolean completed=false;
		RoomClass roomNonEmpty = null;
		for (RoomClass room:roomClasses){
			if (room.isFull()){
				System.out.println(c.getID()+" WALKED THROUGH THE FULL ROOM");
				continue;
			}
			else{
				System.out.println(c.getID()+" ENTERED THE ROOM");
				completed=true;
				roomNonEmpty=room;
				break;
			}
		}
		if (roomNonEmpty==null){
			roomNonEmpty=new RoomClass();
			roomClasses.add(roomNonEmpty);
			roomNonEmpty.enterConnection(c);
			System.out.println("CONNECTED FROM NULL");
			completed=false;
		}
		else {
			roomNonEmpty.enterConnection(c);
			System.out.println("CONNECTED FROM FULL");
			completed=false;
		}
		System.out.println("ROOM NUMBER = "+roomClasses.size());
	}

	//Используется когда клиент отправляет пакет серверу
	public void received(Connection c, Object p){
		try {
			for (RoomClass room:roomClasses){
				if (room.isFull() && (c.getID()==room.getFirstPlayerConnection().getID() ||
						c.getID()==room.getSecondPlayerConnection().getID())) room.getReceived(c, p);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	//Используется когда клиент покидает сервер
	public void disconnected(Connection c){
		connectionsArray.remove(c);
		try {
			for (RoomClass room : roomClasses) {
					if (room.getFirstPlayerConnection().getID()==c.getID() || (room.getSecondPlayerConnection().getID()==c.getID())){
						System.out.println(c.getID());
						System.out.println("DELETED");
					room.disconnetced(c);
					roomClasses.remove(room);
					}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println("ROOM NUMBER FINAL = "+roomClasses.size());
	}
}