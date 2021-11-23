package com.bakyna;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class Board extends JPanel {
    private static final int M= 24;
    private static final int N= 24;

    public static final int ST_DRAW =0;
    public static final int ST_WiN =1;
    public static final int ST_NORMAL =2;

    private EndgameListener endgameListener;

    private Image imageO;
    private  Image imageX;
    private Cell matrix[][] = new Cell[N][M];
    private String currentPlayer = Cell.EMPTY_VALUE;
    public Board(String player){
        this();
        this.currentPlayer = player;
    }



    public Board(){
        this.initMatrix();
        // tính toán xem x, y rơi vào ô nào trong board, sau đó điền o x vào


     addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
             super.mousePressed(e);
             int x= e.getX();
             int y = e.getY();
             soundClick();
             if(currentPlayer.equals(Cell.EMPTY_VALUE)){
                 return;
             }


             for(int i=0; i<N; i++){
                 for(int j=0; j<N; j++){
                     Cell cell = matrix[i][j];
                     int cXstart = cell.getX();
                     int cYstart = cell.getY();

                     int cXend = cXstart + cell.getW();
                     int cYend = cYstart + cell.getH();

                     if(x>=cXstart && x<=cXend && y>=cYstart && y <= cYend){
                        if(cell.getValue()== Cell.EMPTY_VALUE){
                            cell.setValue(currentPlayer);
                            int result = checkWin(currentPlayer, i, j);
                            if(endgameListener != null){
                                endgameListener.end(currentPlayer, checkWin(currentPlayer, i , j));
                            }
                            if(result == ST_NORMAL){
                                currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE:Cell.O_VALUE;
                            }


                            repaint();
                         }

                     }
                 }
             }

         }


     });
        try{
            imageO = ImageIO.read(getClass().getResource("imageO.png"));
            imageX = ImageIO.read(getClass().getResource("imageX.jpg"));

        }catch (IOException e){
            e.printStackTrace();
        }




    }
    public void reset(){
        this.initMatrix();
        this.setCurrentPlayer(Cell.EMPTY_VALUE);
        repaint();

    }
    private void soundClick(){
        try{
            Clip clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("Mouse Click Fast.wav"));
            clip.open(audioInputStream);
            clip.start();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    private void initMatrix(){
        for(int i=0; i<N; i++){
            for(int j=0; j<N; j++){
                Cell cell = new Cell();
                matrix[i][j] =cell;
            }
        }
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setEndgameListener(EndgameListener endgameListener) {
        this.endgameListener = endgameListener;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    private boolean isFull(){
        int number = M * N;
        int k = 0;
        for(int i= 0; i< M; i++){

            for(int j= 0; j<M; j++) {
                Cell cell = matrix[i][j];
                if(!cell.getValue().equals(Cell.EMPTY_VALUE)){
                    k++;
                }
            }
        }
        return k == number;
    }
    public int checkWin(String player, int i , int j){
        // duong cheo thu nhat
//        if(this.matrix[0][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)){
//            return ST_WiN;
//        }
//        // duong cheo thu 2
//        if(this.matrix[0][2].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][0].getValue().equals(player)){
//            return ST_WiN;
//        }
//        // dong thu 1
//        if(this.matrix[0][0].getValue().equals(player) && this.matrix[0][1].getValue().equals(player) && this.matrix[0][2].getValue().equals(player)){
//            return ST_WiN;
//        }
//        //dong thu 2\
//        if(this.matrix[1][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[1][2].getValue().equals(player)){
//            return ST_WiN;
//        }
//        //dong thu 3
//        if(this.matrix[2][0].getValue().equals(player) && this.matrix[2][1].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)){
//            return ST_WiN;
//        }
//        //cot thu 1
//        if(this.matrix[0][0].getValue().equals(player) && this.matrix[1][0].getValue().equals(player) && this.matrix[2][0].getValue().equals(player)){
//            return ST_WiN;
//        }
//        if(this.matrix[0][1].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][1].getValue().equals(player)){
//            return ST_WiN;
//        }
//        if(this.matrix[0][2].getValue().equals(player) && this.matrix[1][2].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)){
//            return ST_WiN;
//        }

        // check ngang
        int count=0;
        for(int col = 0; col <M; col ++){
            Cell cell = matrix[i][col];
            if(cell.getValue().equals(player)){
                count ++;
                if(count== 5){
                    return ST_WiN;
                }
            }else{
                count = 0;
            }
        }

        // check doc
        for(int row = 0; row <M; row ++){
            Cell cell = matrix[row][j];
            if(cell.getValue().equals(player)){
                count ++;
                if(count== 5){
                    return ST_WiN;
                }
            }else{
                count = 0;
            }
        }

        // cheo trai
        int k= i<j?i:j;
        int row = i-k;
        int col= j-k;
        count = 0;
      for(; row<M && col<M; row++, col ++){
           Cell cell = matrix[row][col];
           if(cell.getValue().equals(player)){
               count ++;
               if(count== 5){
                   return ST_WiN;
               }
           }else{
               count=0;
           }
      }

        // cheo phai
         row= i-k;
         col= j+k;
         count =0;
        for(; row<M && col>0; row++, col--){
            Cell cell = matrix[row][col];
            if(cell.getValue().equals(player)){
                count ++;
                if(count== 5){
                    return ST_WiN;
                }
            }else{
                count=0;
            }
        }

        if(col>=M){
            int du = col-(M-1);
            row = row + du;
            col = M-1;

        }

        if(this.isFull()){
            return  ST_DRAW;
        }

        return  ST_NORMAL;
    }

    @Override
    public void paint(Graphics g) {
        int w = getWidth()/M;
        int h = getHeight()/N;

        Graphics2D graphics2D = (Graphics2D) g;

        for(int i=0; i<N; i++ ){
            int k = i;
            for(int j =0; j<N;j++){
                int x= j*w;
                int y = i*h;
                Cell cell = matrix[i][j];
                cell.setY(y);
                cell.setX(x);
                cell.setW(w);
                cell.setH(h);
                Color coler = (k%2)==0?Color.WHITE:Color.GRAY;
                graphics2D.setColor(coler);
                graphics2D.fillRect(x, y, w, h);

                if(cell.getValue().equals(Cell.O_VALUE)){
                    Image image = imageO;
                    graphics2D.drawImage(image,x, y, w, h, this );
                }else if(cell.getValue().equals(Cell.X_VALUE)){
                    Image image = imageX;
                    graphics2D.drawImage(image,x, y, w, h, this );
                }
//                Image img = k%2==0?imageX : imageO;
//                graphics2D.drawImage(img, x, y, w, h, this);

                k++;
            }
        }

    }
}
