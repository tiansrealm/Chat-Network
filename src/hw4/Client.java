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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Client{
    
    Scanner sin;
    PrintStream sout;
    String username;
    String serverName = null;
    Socket s; //server
    GUI gui;
    Client() throws IOException {
        JFrame jf = new JFrame("HW 4 NetWork - Tian Lin");
        jf.setSize(400,400);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        gui = new GUI(this);
        jf.add(gui);
        gui.updateUI();
        
        //
        s = new Socket();
        gui.display.append("Please enter server to connect to.\n");
        if(!s.isConnected()){
            while(serverName == null){
                Thread.currentThread().yield();
            }
        }
    }
    public static void main(String[] args){
        try { 
            Client c = new Client();
        } catch (IOException ex) {
        }
    }
    void connect() throws IOException{
        //attempts to connect to server
        if(serverName == null){
            serverName = gui.input.getText();
        }
        gui.display.append("Attempping to connect to " + 
                serverName + " on port 5190 \n");
        try{
            s = new Socket(serverName, 5190);
        }
        catch(Exception e){
        }
        if (s.isConnected()){
            gui.display.append("Sucessfully connected.\n");
            sin = new Scanner(s.getInputStream());
            sout = new PrintStream(s.getOutputStream());
            new RecieveFromServer(this).start();
        }
        else { 
            gui.display.append("Failed to connect\n"
                    + "Please enter server to connect to.\n"); 
            serverName = null;
        }
    }
}
class RecieveFromServer extends Thread{
    Client c; //
    RecieveFromServer(Client theClient){c = theClient;}
    @Override
    public void run(){
        String line = c.sin.nextLine();
        while (!line.equalsIgnoreCase("exit")){
            c.gui.display.append(line + "\n");
            line = c.sin.nextLine();
        }
        try {
            c.s.close();
        } catch (IOException ex) {
        }
    }
}
class GUI extends JPanel {
    Client my_client;
    TextArea display;
    TextArea input;
    GUI(Client c){
        my_client = c;
        setLayout(new GridLayout(0,1));
        display = new TextArea(10, 20);
        display.setEditable(false);
        display.setBackground(Color.LIGHT_GRAY);
        input = new TextArea(10, 20);
        input.setBackground(new Color(135, 206, 250));
        add(display);
        add(input);
        add(new MyButton("SEND", this));
    }
    void send(){
        //display.append(input.getText() + "\n");
        my_client.sout.print(input.getText() + "\r\n");
        input.setText("");
    }
}
class MyButton extends JButton{
    GUI my_gui; //reference to container
    MyButton(String text,  GUI gui){
        super(text);
        my_gui = gui;
        this.addActionListener(new SendButtonPress());
    } 
}

class SendButtonPress implements ActionListener{
    //used for mybutton.
    //changes self button color
    SendButtonPress(){}
    @Override
    public void actionPerformed(ActionEvent ae){
        MyButton b = (MyButton) ae.getSource();
        if(b.my_gui.my_client.s.isConnected()){
            b.my_gui.send();
        }
        else{ 
            try { 
                b.my_gui.my_client.connect();
            } catch (IOException ex) {}
        }
    }
}