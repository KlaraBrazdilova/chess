package com.company;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import static java.lang.Math.abs;

public class Figure {
    Boolean notMoved;
    Boolean color;
    Piece piece;
    Boolean infinity;

    public Figure(Boolean color, Piece piece) {
        this.color=color;
        this.notMoved=true;
        this.piece=piece;
        this.infinity = (piece == Piece.queen) || (piece == Piece.rook) || (piece == Piece.bishop);
    }

    public void setNotMoved(Boolean notMoved) {
        this.notMoved = notMoved;
    }

    public Boolean getNotMoved() {
        return notMoved;
    }

    public Boolean getColor() {
        return color;
    }

    public Piece getPiece() {
        return piece;
    }

    public Boolean getInfinity() {
        return infinity;
    }
    //ulozeni figurky do soubnoru
    public void saveToFile(DataOutput dataWriter){
        try {
            dataWriter.writeBoolean(color);
            dataWriter.writeUTF(piece.name());
            dataWriter.writeBoolean(notMoved);
            dataWriter.writeBoolean(infinity);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //nejvic dulezita metoda
    //tesuji, jestli pohyb ze zadane souradnice na danou souradnici je validni vzhledem k pravidlum
    public Boolean testMove(Board board, int fromr, int fromc, int tor, int toc) {
        int pomr=1,pomc=1;
        if(fromr>tor){
            pomr=-1;
        }
        if(fromr==tor){
            pomr=0;
        }
        if (fromc>toc){
            pomc=-1;
        }
        if (fromc==toc){
            pomc=0;
        }
        int r=fromr,c=fromc;

        Figure fig = board.getSquare(tor,toc);

        //moznost rosada, oznacim krále a vez na kterou chci pohyb provést
        if(((piece==Piece.rook)&&(fig.getPiece()==Piece.king)&&(fig.getColor()==color)&&(getNotMoved())&&(fig.getNotMoved()))||
                ((piece==Piece.king)&&(fig.getPiece()==Piece.rook)&&(fig.getColor()==color)&&(getNotMoved())&&(fig.getNotMoved()))){
            while (c!=toc){
                c = c+pomc;
                if(board.getSquare(r,c)!=null){
                    return false;
                }
            }
            if(((fig.getPiece()==Piece.king)&&(pomc==1))||((fig.getPiece()==Piece.rook)&&(pomc==-1))){
                board.setSquare(r,6,new Figure(color,Piece.king));
                board.setSquare(r,5,new Figure(color,Piece.rook));
                board.setSquare(fromr,fromc,null);
                board.setSquare(tor,toc,null);
            }
            else {
                board.setSquare(r,2,new Figure(color,Piece.king));
                board.setSquare(r,3,new Figure(color,Piece.rook));
                board.setSquare(fromr,fromc,null);
                board.setSquare(tor,toc,null);
            }
            return true;
        }

        if(this.infinity)
        {
            //figurky, které nemají omezenou vzdalenost pohybu
            if((piece==Piece.rook) && ((abs(pomr)==abs(pomc)))){
                return false;
            }
            if((piece==Piece.bishop) && (abs(pomr)!=1) && (abs(pomc)!=1)){
                return false;
            }
            do {
                if ((board.getSquare(r ,c )!=null)&&((r!=fromr)&&(c!=fromc))) {
                    return false;
                }
                r=r+pomr;
                c=c+pomc;
            }
            while((r<8&&r>=0)&&(c<8&&c>=0)&&((r!=tor)||(pomr==0))&&((c!=toc)||(pomc==0)));

            return (r == tor) && (c == toc);
        }
        else {
            if(piece==Piece.pawn){
                if (notMoved) {//kdyz pesec se jeste nepohl, muze se pohnout o 2 dopredu
                    if ((board.getSquare(tor,toc)==null)&&((fromc == toc) && (fromr + (2 * pomr) == tor))) {
                        return true;
                    }
                }
                return ((fig == null) && ((fromc == toc) && (fromr + pomr == tor)))
                        || ((fig != null) && ((fromr + pomr == tor) && (fromc + pomc == toc) && (pomc!=0)));
            }
            if ((piece==Piece.knight)&&(abs((fromr-tor)*(fromc-toc))!=2)) // pokud nasobec absolutní hodnoty rozdílu souradnic from a to x a y souradnice neni roven 2, tak neni mozne provest tah
            {
                return false;
            }
            if(piece==Piece.knight){
                return ((fromr + (2 * pomr) == tor) && (fromc + pomc == toc)) || ((fromc + (2 * pomc) == toc) && (fromr+ pomr == tor));
            }
            if ((piece==Piece.king)&&((abs(tor-fromr)>1)||(abs(toc-fromc)>1))) //kral se nesmi pohnout vic jak o 1
            {
                return false;
            }
            return (fromr + pomr == tor) && (fromc + pomc == toc);
        }
    }
}
