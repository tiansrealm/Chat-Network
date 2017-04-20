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


public class Hw4 {

    public static void main(String[] args) {
        JFrame jf = new JFrame("HW 4 NetWork - Tian Lin");
        jf.setSize(400,400);
        jf.setResizable(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
        GUI gui = new GUI();
        jf.add(gui);
        gui.updateUI();
    }
    
}


class GUI extends JPanel {
    TextArea display;
    TextArea input;
    GUI(){
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
        display.append(input.getText() + "\n");
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
        b.my_gui.send();
    }
}
class NetWork {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public void connect() throws IOException {
        // TODO code application logic here
        Socket s = new Socket("172.16.13.19", 1234);
        if (s.isConnected()){
            Scanner sin = new Scanner(s.getInputStream());
            PrintStream sout = new PrintStream(s.getOutputStream());
            int x = sin.nextInt();
            int num = sin.nextInt();
            int ans = num + 127 - 14 + 7 * 622 - 100;
            String send = ans + " " + "18941644\r\n";
            sout.print(send);
            System.out.println(send);
            while(sin.hasNext()){
                System.out.println(sin.nextLine());
            }
            s.close();
        }
        else { System.out.println("failed to connect"); }
        
                
    }
    
}
