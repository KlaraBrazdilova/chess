package com.company;

import java.io.*;
import java.util.Scanner;

public class Judge {
    Board board;
    boolean player;
    int scoreA;
    int scoreB;

    public Judge() {
        this.board = new Board();
        this.player=true;
        this.scoreA=0;
        this.scoreB=0;
    }

    //pocita skore hracu
    public void Score(int tor, int toc){
        if(board.getSquare(tor, toc)!=null){
            if (player){
                scoreA++;
            }
            else {
                scoreB++;
            }
        }
    }

    //provede samotný pohyb figurky
    public void Move(int fromr, int fromc, int tor, int toc){
        board.setSquare(tor,toc,board.getSquare(fromr,fromc));
        board.setSquare(fromr,fromc,null);
    }

    //pomocný switch pri nacitani sloupce
    private int inputCol(String input){
        char charakter = input.charAt(0);
        int fromc = switch (charakter) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 6;
            case 'H' -> 7;
            default -> 10;
        };
        return fromc;
    }

    //nacitani vstupu hrace odkud chce figurkou pohnout a kam
    public Integer[] PlayerInput(){
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Zadejte souradnice figurky, kterou chcete pohnout: ");
            String input = sc.next();
            int number = Character.getNumericValue(input.charAt(1));
            int fromc = inputCol(input);
            int fromr = 8 - number;

            System.out.println("Zadejte souradnice na kterou chcete figurku posunout: ");
            input = sc.next();
            number = Character.getNumericValue(input.charAt(1));
            int toc = inputCol(input);
            int tor = 8 - number;

            Integer[] pole = new Integer[4];
            pole[0]=fromr;
            pole[1]=fromc;
            pole[2]=tor;
            pole[3]=toc;

            if(board.getSquare(fromr,fromc).getColor()!=player){
                throw new Exception("toto neni tvoje figurka");
            }
            return pole;
        }
        catch (Exception e){
            System.out.println("Zadani souradnic se nevydarilo, zkuste to prosim znovu.");
            return  PlayerInput();
        }
    }

    //pomocna pro chackmate, nalezne krále daného hráce
    public int[] findKing(boolean color){
        for(int i = 0;i<8;i++){
            for(int j =0;j<8;j++){
                try {
                    if ((board.getSquare(i, j).getPiece() == Piece.king) && (board.getSquare(i, j).getColor() == color)) {
                        int[] pom = new int[2];
                        pom[0] = i;
                        pom[1] = j;
                        return pom;
                    }
                }
                catch (Exception ignore){
                }
            }
        }
        return null;
    }
    //pomocná pro check aby nebyl redundantni kod
    public boolean checkKnight(int r, int c){
        try{
            return (board.getSquare(r,c).getPiece()==Piece.knight) &&(board.getSquare(r,c).getColor()==player);
        }
        catch (Exception e){
            return false;
        }
    }
    //stejne jako checkKinght jen pro Pawn
    public boolean checkPawn(int r, int c){
        try {
            return (board.getSquare(r, c).getPiece()==Piece.pawn);
        }
        catch (Exception e){
            return false;
        }
    }
    //postupne testuje jestli nahodou některý typ figurky protihráce neutoci na krále
    public boolean check(int kingr, int kingc){
        boolean checkpom = false;

       if(!checkpom){
            checkpom =  ((checkKnight(kingr+2,kingc+1)) ||
                    (checkKnight(kingr-2,kingc+1)) ||
                    (checkKnight(kingr+2,kingc-1)) ||
                    (checkKnight(kingr-2,kingc-1)) ||
                    (checkKnight(kingr+1,kingc+2)) ||
                    (checkKnight(kingr+1,kingc-2)) ||
                    (checkKnight(kingr-1,kingc+2)) ||
                    (checkKnight(kingr-1,kingc-2)));
        }
        if(!checkpom){
            int pom = 1;
            if(!player){
                pom = -1;
            }
            checkpom = ((checkPawn(kingr+1, kingc+pom))||
                        (checkPawn(kingr-1, kingc+pom)));
        }
        if(!checkpom){
            int r = kingr;
            int c = kingc+1;
            while((c<8)&&(board.getSquare(r,c)==null)){
                c++;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.rook))){
                checkpom = true;
            }
            c = kingc-1;
            while((c>=0)&&(board.getSquare(r,c)==null)){
                c--;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.rook))){
                checkpom = true;
            }
            c = kingc;
            r = kingr+1;
            while((r<8)&&(board.getSquare(r,c)==null)){
                r++;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.rook))){
                checkpom = true;
            }
            r = kingr-1;
            while((r>=0)&&(board.getSquare(r,c)==null)){
                r--;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.rook))){
                checkpom = true;
            }
        }
        if(!checkpom)
        {
            int r = kingr+1;
            int c = kingc+1;
            while((c<8)&&(r<8)&&(board.getSquare(r,c)==null)){
                c++;
                r++;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.bishop))){
                checkpom = true;
            }
            r = kingr-1;
            c = kingc+1;
            while((c<8)&&(r>=0)&&(board.getSquare(r,c)==null)){
                c++;
                r--;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.bishop))){
                checkpom = true;
            }
            r = kingr+1;
            c = kingc-1;
            while((r<8)&&(c>=0)&&(board.getSquare(r,c)==null)){
                r++;
                c--;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.bishop))){
                checkpom = true;
            }
            r = kingr-1;
            c = kingc-1;
            while((c>=0)&&(r>=0)&&(board.getSquare(r,c)==null)){
                c--;
                r--;
            }
            if((board.getSquare(r,c).getColor()==player)&&((board.getSquare(r,c).getPiece()==Piece.queen)||(board.getSquare(r,c).getPiece()==Piece.bishop))){
                checkpom = true;
            }
        }
        return checkpom;
    }
    //pomocná pro zjednodušení a zpřehlednění checkmate
    public boolean tryCheck(int r, int c){
        try {
            return ((board.getSquare(r, c) == null) && (check( r, c)))||((board.getSquare(r, c) != null)&&(board.getSquare(r, c).getColor()==player));
        }
        catch (Exception e){
            return true;
        }
    }
    //testuje checkmate po každém tahu
    //metoda neni plne funkci pro vsechny mozne pripady
    public boolean checkmate(){
        int pokracuj=0;
        int[] pom = findKing(!player);
        if(pom==null){
            pokracuj=2;
        }
        else {
            int kingr = pom[0], kingc = pom[1];
            try { //zkousim, jestli kdyz se kral nekam pohne, jestli je na nej dan check
                if ((tryCheck(kingr + 1, kingc + 1)) &&
                        (tryCheck(kingr + 1, kingc - 1)) &&
                        (tryCheck(kingr - 1, kingc + 1)) &&
                        (tryCheck(kingr - 1, kingc - 1)) &&
                        (tryCheck(kingr, kingc + 1)) &&
                        (tryCheck(kingr + 1, kingc)) &&
                        (tryCheck(kingr, kingc - 1)) &&
                        (tryCheck(kingr - 1, kingc))) {
                    if (check(kingc, kingc)) {
                        pokracuj = 2; // pokud je dan sach i na krale tam kde stoji, checkmate
                    } else {
                        pokracuj = 1; //kdyz se nemuze pohnout, tak Pat
                    }
                }
                else {
                    if (check(kingc, kingc)){
                        System.out.println("Šach!");
                    }
                }
            }
            catch (Exception ignore){

            }
        }
        if(pokracuj==2){
            System.out.println("Sach mat! Vyhrává hráč: "+player);
            return false;
        }
        if (pokracuj==1){
            System.out.println("Doslo k Patu. Vyhrává hráč: "+player);
            return false;
        }
        player=!player;
        return true;
    }

    //kdyz dojde pesec na konec sachovnice muzu ho zmenit na neco jineho
    public void PawnChange(int r, int c){
        if(((player)&&(r==7))||((!player)&&(r==0))){
            try {
                Scanner sc = new Scanner(System.in);
                System.out.println("Dostal jsi se na konec sachovnice, nyni muzes vymenit figurku za libovolnou. Napis dane pismeno pro tvou volbu.");
                System.out.println("Král - K");
                System.out.println("Dáma - Q");
                System.out.println("Věž - R");
                System.out.println("Střelec - B");
                System.out.println("Kůň - N");
                System.out.println("Pešec - P");
                String input = sc.next();
                char charakter = input.charAt(0);
                Figure newFig=null;
                switch (charakter) {
                    case 'K' :
                        newFig= new Figure(player,Piece.king);
                        break;
                    case 'Q' :
                        newFig= new Figure(player,Piece.queen);
                        break;
                    case 'R' :
                        newFig= new Figure(player,Piece.rook);
                        break;
                    case 'B' :
                        newFig= new Figure(player,Piece.bishop);
                        break;
                    case 'N' :
                        newFig= new Figure(player,Piece.knight);
                        break;
                    case 'P':
                        newFig= new Figure(player,Piece.pawn);
                        break;
                }
                if(newFig!=null){
                    board.setSquare(r,c,newFig);
                }
                else {
                    System.out.println("Neco se nepovedlo. Zkuste to znovu.");
                    PawnChange(r, c);
                }
            }
            catch (Exception e){
                PawnChange(r, c);
            }
        }

    }
    //ulozeni hry
    public void saveData(String filename) throws FileNotFoundException {
        OutputStream outputStream = null;
        DataOutput dataWriter = null;
        outputStream = new FileOutputStream(filename,false);
        dataWriter = new DataOutputStream(outputStream);

        try {
            dataWriter.writeBoolean(player);
            dataWriter.writeInt(scoreA);
            dataWriter.writeInt(scoreB);
            board.saveToFile(dataWriter);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //nacteni hry ze souboru
    public void loadData(String filename) throws IOException {
        InputStream inputStream = null;
        DataInput dataReader = null;

        try {
            inputStream= new FileInputStream(filename);
            dataReader = new DataInputStream(inputStream);
            player=dataReader.readBoolean();
            scoreA=dataReader.readInt();
            scoreB=dataReader.readInt();
            board.loadFromFile(dataReader);
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private boolean giveUp(){
        System.out.println("Chete se vzdát?");
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("ANO(true), NE(false): ");
            return sc.nextBoolean();
        }
        catch (Exception e){
            return giveUp();
        }
    }
    private boolean saveGame(){
        System.out.println("Chcete hru odročit?");
        Scanner sc = new Scanner(System.in);
        System.out.println("ANO(true), NE(false): ");
        try {
            Boolean input = sc.nextBoolean();
            if(input){
                System.out.println("Zadejte název souboru do kterého hru ulozim.");
                String filename = sc.next();
                try {
                    saveData(filename);
                    return true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            return saveGame();
        }

        return false;
    }
    private void loadGame() {
        System.out.println("Chcete novou šachovou partii nebo chce pokračovat v některé z vašich předešlích?");
        System.out.println("Novou (false), načíst již rozehrálou (true)");
        Scanner sc = new Scanner(System.in);
        Boolean input = sc.nextBoolean();
        if(input){
            System.out.println("Zadejte název souboru hry, ve které chcete pokračovat.");
            String filename = sc.next();
            try {
                loadData(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //beh cele hry
    public void loop() {
        int fromr,fromc,tor,toc;
        boolean begin=true;
        loadGame();
        while((begin)||(checkmate())) {
            board.printBoard();
            if(player){
                System.out.println("PRÁVĚ HRAJE HRÁČ: BÍLÁ");
            }
            else {
                System.out.println("PRÁVĚ HRAJE HRÁČ: ČERNÁ");
            }
            if(saveGame()){
                break;
            }
            else {
                begin = false;
                if (giveUp()) {
                    if (player)
                        System.out.println("Hráč bílý se vzdal. Vyhrává ČERNÝ.");
                    else
                        System.out.println("Hráč černý se vzdal. Vyhrává BÍLÝ.");
                    break;
                } else {
                    Integer[] pole = PlayerInput();
                    fromr = pole[0];
                    fromc = pole[1];
                    tor = pole[2];
                    toc = pole[3];
                    try {
                        if (board.getSquare(fromr, fromc).testMove(board, fromr, fromc, tor, toc)) {
                            Score(tor, toc);
                            Move(fromr, fromc, tor, toc);
                            if (board.getSquare(tor, toc).getPiece() == Piece.pawn)
                                PawnChange(tor, toc);
                        } else {
                            System.out.println("Tento pohyb nelze provezt");
                            begin = true;
                        }

                    } catch (Exception e) {
                        System.out.println("Tento pohyb nelze provezt");
                        begin = true;
                    }
                    System.out.println("Aktuální skore hraču je: bílý - " + scoreA + ", černý - " + scoreB);
                }
            }
        }
    }
}
