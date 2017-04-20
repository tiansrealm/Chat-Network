/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw4;
/**
 *
 * @author Tian
 */

import java.awt.TextArea;
import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
    ArrayList<ProcessClient> clientList;
    Server(){
        ServerSocket ss = null;
        try{
            ss = new ServerSocket(5190);
            //System.out.println(ss.getInetAddress());
            clientList = new ArrayList();
            while(true){
                System.out.println("Waiting for a connection on port 5190");
                Socket client = ss.accept();
                ProcessClient newpc = new ProcessClient(client, this);
                clientList.add(newpc);
                newpc.start();
            }
        }
        catch (IOException e){
            System.out.println("Could not get the socket to work!");
            System.exit(1);
        }
    }
    public static void main(String[] args) {
        Server s = new Server();
    }
}


class ProcessClient extends Thread{
    Socket client;
    Server my_server;
    String username;
    PrintStream sout;
    Scanner sin;
    //String username;
    ProcessClient(Socket newclient, Server s){
        client=newclient;
        my_server = s;
    }
    @Override
    public void run(){
        try{
            System.out.println(": Got connection on port 5190 from: "+
                    client.getInetAddress().toString());
  
            sout = new PrintStream(client.getOutputStream());
            sin = new Scanner(client.getInputStream());
            sout.print("Enter your chat name?\r\n");
            username = sin.nextLine();
            sout.print("Hello " + username + ". You may chat now.\r\n");
            String line = sin.nextLine();
            while (!line.equalsIgnoreCase("exit")){
                System.out.println(": " +line);
                for(ProcessClient pc: my_server.clientList){
                    pc.sout.print(username + ": " + line + "\r\n");
                }
                line = sin.nextLine();
            }
            client.close();
        }
        catch(IOException e){}
    }
}