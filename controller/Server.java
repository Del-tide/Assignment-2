package application.controller;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<Socket> clients = new ArrayList<>();
    static ArrayList<Game> games = new ArrayList<>();
    public static void main(String[] args) {
        try{
            System.out.println("Server start...");
            ServerSocket serverSocket = new ServerSocket(7496);
            Socket socket = new Socket();
            while (true){
                socket = serverSocket.accept();
                clients.add(socket);
                MsgBackThread thread;
                if (clients.size() % 2 == 0){
                    thread = new MsgBackThread(socket,"The game will start soon, you're player 2.");
                    games.add(new Game(clients.get(clients.size()-2),clients.get(clients.size()-1),games.size()));
                    games.get(games.size()-1).start();
                }else thread = new MsgBackThread(socket,"Waiting for opponent, you're Player 1.");
                thread.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class MsgBackThread extends Thread{
    private Socket socket = null;
    String msg;

    public MsgBackThread(Socket socket, String msg){
        this.socket = socket;
        this.msg = msg;
    }

    @Override
    public void run(){
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        InputStream is = null;
        PrintWriter pw = null;
        try {
            os = socket.getOutputStream();
            pw = new PrintWriter(os,true);

            pw.write(msg);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(!pw.equals(null))pw.close();
                if(!br.equals(null))br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Game{
    Socket p1,p2;
    int id;
    public Game(Socket p1,Socket p2,int id){
        this.p1 = p1;
        this.p2 = p2;
        this.id = id;
    }
    public void start() throws IOException {
        try {
            PrintWriter pw1 = new PrintWriter(p1.getOutputStream(), true);
            PrintWriter pw2 = new PrintWriter(p2.getOutputStream(), true);
            pw1.write("Game start");
            pw2.write("Game start");
        } catch (IOException e){
            System.out.println("A player offline.");
        }
    }
}

