package application.controller;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    Socket socket;
    public void login() {
        try{
            socket = new Socket("localhost",7496);
            MsgForeThread thread = new MsgForeThread(socket);
            thread.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chess(int x, int y) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.write("P"+x+""+y+"");
    }
}
class MsgForeThread extends Thread {
    private Socket socket = null;

    public MsgForeThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        InputStream is = null;
        PrintWriter pw = null;
        try {

            os = socket.getOutputStream();
            pw = new PrintWriter(os);

            pw.write("");
            pw.flush();
            socket.shutdownOutput();
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            String line = null;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!pw.equals(null)) pw.close();
                if (!os.equals(null)) os.close();
                if (!br.equals(null)) br.close();
                if (!isr.equals(null)) isr.close();
                if (!is.equals(null)) is.close();
                if (!socket.equals(null)) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

