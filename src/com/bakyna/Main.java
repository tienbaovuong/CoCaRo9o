package com.bakyna;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;
import java.util.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

public class Main {
    private static Board board;
    private static int sec = 0;
    private static Timer timer = new Timer();
    private static JLabel lblTime;
    private  static JButton btnStart;


    public static void main(String[] args) {
	// write your code here


        board = new Board();
        board.setEndgameListener(new EndgameListener() {
            @Override
            public void end(String player, int st) {
                if(st== board.ST_WiN){
                    JOptionPane.showMessageDialog(null, "Người chơi "+player + " thắng!!");
                    stopGame();
                }else if(st == board.ST_DRAW){
                    JOptionPane.showMessageDialog(null, "Hoà rồi!!");
                    stopGame();
                }


            }

        });

         Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel jPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(jPanel, BoxLayout.Y_AXIS);
        jPanel.setLayout(boxLayout);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 0, 0);


        board.setPreferredSize(new Dimension(600, 600));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(flowLayout);


         btnStart = new JButton("Start");
         lblTime = new JLabel("0:0");

        bottomPanel.add(lblTime);
        bottomPanel.add(btnStart);
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(btnStart.getText().equals("Start")){
                    startGame();
                }else{
                    stopGame();
                }

            }
        });

        jPanel.add(board);
        jPanel.add(bottomPanel);


        JFrame jFrame = new JFrame(" Game co ca ro 9 o");
        jFrame.setSize(600, 600);
        jFrame.setResizable(false);
        jFrame.add(jPanel);

        int  x = (int) (dim.width/2 -(jFrame.getWidth()/2)) ;
        int y = (int) (dim.height/2 -(jFrame.getHeight()/2)) ;

        jFrame.setLocation(x, y);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);

    }
    private static  void startGame(){
        int choice = JOptionPane.showConfirmDialog(null, "Bạn chọn O đi trước phải không ?", "Chọn người đi trước", JOptionPane.YES_NO_OPTION);
        board.reset();
        String currentPlayer = (choice == 0) ? Cell.O_VALUE : Cell.X_VALUE;
        board.setCurrentPlayer(currentPlayer);

        //dem nguoc
        sec = 0;
        lblTime.setText("0:0");
        timer.cancel();
        timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sec ++ ;
                String time = sec/60 + ":" + sec%60;
                lblTime.setText(time);

            }

        }, 1000, 1000);
        btnStart.setText("Stop");



    }
    private static void stopGame(){
        btnStart.setText("Start");
        sec = 0;
       lblTime.setText("0:0");

       timer.cancel();
       timer = new Timer();

        board.reset();


    }



}
