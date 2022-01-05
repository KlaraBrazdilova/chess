package com.company;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

public class Board {
    Figure[][] board;

    public Board() {
        this.board = newBoard();
    }

    public Figure[][] getBoard() {
        return board;
    }

    public Figure getSquare(int r, int c)
    {
        return board[r][c];
    }
    public void setSquare(int r, int c, Figure value)
    {
        board[r][c] = value;
    }

    //vytvoreni pocatecniho stavu sachovnice
    public Figure[][] newBoard(){
        this.board = new Figure[8][8];
        setSquare(0,0, new Figure(false,Piece.rook));
        setSquare(7,0, new Figure(true,Piece.rook));
        setSquare(0,7, new Figure(false, Piece.rook));
        setSquare(7,7, new Figure(true,Piece.rook));
        setSquare(0,1,new Figure(false,  Piece.knight));
        setSquare(0,6,new Figure(false,  Piece.knight));
        setSquare(7,1,new Figure(true, Piece.knight));
        setSquare(7,6,new Figure(true,  Piece.knight));
        setSquare(0,2,new Figure(false,  Piece.bishop));
        setSquare(0,5,new Figure(false,  Piece.bishop));
        setSquare(7,5,new Figure(true,  Piece.bishop));
        setSquare(7,2,new Figure(true,  Piece.bishop));
        setSquare(7,3,new Figure(true,  Piece.queen));
        setSquare(7,4,new Figure(true,  Piece.king));
        setSquare(0,3,new Figure(false,  Piece.queen));
        setSquare(0,4,new Figure(false,  Piece.king));
        for(int j = 0;j<8;j++){
            setSquare(1,j, new Figure(false, Piece.pawn));
        }
        for (int i = 2; i < 6; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                setSquare(i,j,null);
            }
        }
        for(int j = 0;j<8;j++){
            setSquare(6,j, new Figure(true, Piece.pawn));
        }
        return board;
    }
    //ulozeni sachovnice
    public void saveToFile(DataOutput dataWriter){
        for(int i = 0;i<8;i++){
            for(int j = 0;j<8;j++){
                try {
                    if(getSquare(i,j)==null) {

                        dataWriter.writeInt(0);

                    }
                    else {
                        dataWriter.writeInt(1);
                        getSquare(i,j).saveToFile(dataWriter);
                    }
                } catch (IOException e) {
                e.printStackTrace();
                }
            }
        }
    }
    //nacteni sachovnice
    public void loadFromFile(DataInput dataReader){
        Figure[][] board = new Figure[8][8];
        for (int i =0;i<8;i++){
            for(int j = 0;j<8;j++){
                try {
                    if(dataReader.readInt()==0){
                        setSquare(i,j,null);
                    }
                    else
                    {
                        Figure pom = new Figure(dataReader.readBoolean(),Piece.valueOf(dataReader.readUTF()));
                        pom.notMoved=dataReader.readBoolean();
                        pom.infinity=dataReader.readBoolean();
                        setSquare(i,j,pom);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_LIGHT_BACKGROUND = "\u001B[44m";
    public static final String ANSI_DARK_BACKGROUND = "\u001B[46m";
    public static final String ANSI_RESET = "\u001B[0m";

    //pomocna aby byly hezke ctverecky
    public String printSquare(String my){
        StringBuilder builder = new StringBuilder();
        int myLength = my.length();
        int pocetMezer = 6-myLength;
        for(int i =0;i<pocetMezer/2;i++){
            builder.append(" ");
        }
        builder.append(my);
        for(int i =0;i<pocetMezer-(pocetMezer/2);i++){
            builder.append(" ");
        }
        return  builder.toString();
    }
    //nejhezci vypis sachovnice do konzole
    public void printBoard(){
        String pom = ANSI_LIGHT_BACKGROUND;
        Character[] sloupec = new Character[] {'A','B','C','D','E','F','G','H'};
        Integer[] radek = new Integer[] {8,7,6,5,4,3,2,1};
        for(int i = 0; i<8;i++){
            System.out.print(radek[i]+" ");
            for(int j = 0;j<8;j++){
                if(getSquare(i,j)!=null)
                {
                    if(getSquare(i,j).getColor()){
                        System.out.print(pom+ANSI_WHITE+printSquare(getSquare(i,j).getPiece().name()));
                    }
                    else {
                        System.out.print(pom+ANSI_BLACK+printSquare(getSquare(i,j).getPiece().name()));
                    }

                }
                else {
                    System.out.print(pom+printSquare(""));
                }
                if(pom.equals(ANSI_LIGHT_BACKGROUND)){
                    pom = ANSI_DARK_BACKGROUND;
                }
                else {
                    pom = ANSI_LIGHT_BACKGROUND;
                }
            }
            System.out.println(""+ANSI_RESET);
            if(pom.equals(ANSI_LIGHT_BACKGROUND)){
                pom = ANSI_DARK_BACKGROUND;
            }
            else {
                pom = ANSI_LIGHT_BACKGROUND;
            }
        }
        System.out.print(" ");
        for(int i = 0;i<8;i++){
            System.out.print(printSquare(sloupec[i].toString()));
        }
        System.out.println();
    }
}
