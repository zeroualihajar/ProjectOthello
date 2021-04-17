/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import Interface.Home;
import Tools.*;
import java.awt.Color;
import java.util.ArrayList;


public class Othello extends GameSearch {
    
    static int milliseconds = 0;            //----- delay
    static int seconds = 0;
    static boolean state = true;
    
    
    /***************************************************************************
    * On declare un tableau sur lequel on met des valeurs tactiques qui
    * representent l'interet de chaque case
    * *************************************************************************/
    
    private int [] force = {500, -150, 30, 10, 10, 30, -150, 500,
                            -150, -250, 0, 0, 0, 0, -250, -150,
                            30, 0, 1, 2, 2, 1 , 0, 30 ,
                            10, 0, 2, 16, 16, 2, 0, 10,
                            10, 0, 2, 16, 16, 2, 0, 10,
                            30, 0, 1, 2, 2, 1, 0, 30,
                            -150, -250, 0, 0, 0, 0, -250, -150,
                            500, -150, 30, 10, 10, 30, -150, 500};
    
    
    //--------- Fonction : drawnPsoition ------------
    @Override
    public boolean drawnPosition(Position p) {
        if (GameSearch.DEBUG) 
            System.out.println("drawnPosition("+p+")");
        boolean ret = true;
        OthelloPosition pos = (OthelloPosition)p;
        for (int i = 0; i < 64; i++) 
        {
            if(pos.board[i] == OthelloPosition.BLANK){
                ret = false;
                break;
            }
        }
        if (GameSearch.DEBUG) 
            System.out.println("     ret="+ret);
        return ret;
    }
    

    //--------- Fonction : wonPosition -------------
    @Override
    public boolean wonPosition(Position p, boolean player) {
        if (GameSearch.DEBUG) 
            System.out.println("wonPosition("+p+","+player+")");
        boolean ret = false;
        OthelloPosition pos = (OthelloPosition)p;
        int Hnum = 0;
        int Pnum = 0;
        int Bnum = 0;
        
        for (int i = 0; i < 64; i++) {
            if (pos.board[i] == -1)      //------ si la position represente le pion blanc
                Pnum++;
            else if (pos.board[i] == 1)  //------ si la position represente le pion noir
                Hnum++;
            else                         //------ si la case est vide
                Bnum++;
        }
        
        
        /***********************************************************************
         * Si aucun pion blanc n'est sur le plateau => le noir a gangé,s'il y a 
         * des cases encore vides & le nombre des pions noir et plus grand de 
         * celle blanc => noir gagne ******************************************/
        
        if (player) // HUMAN                    
        {
            if (Pnum == 0)                                 
                ret = true;
            else if (Bnum == 0 && Hnum > Pnum)
                ret = true;                                  
        }
        
        /***********************************************************************
         * Si aucun pion noir n'est sur le plateau => le blanc a gangé,s'il y a 
         * des cases encore vides & le nombre des pions blanc et plus grand de 
         * celle noir => blanc gagne ******************************************/
        
        else // PROGRAM
        {
           if (Hnum == 0) 
               ret = true;
           else if (Bnum == 0 && Pnum > Hnum) 
               ret = true; 
        }
        return ret;
    }

    //--------- Fonction : positionEvaluation ------------
    @Override
    public float positionEvaluation(Position p, boolean player) {
        int count = 0;
        OthelloPosition pos = (OthelloPosition)p;
        
        //----- on declare un entier pour savoir si le role de Noir ou de blanc
        int tour = -1;  //----- Si le jouer est le blanc => a = -1
        if (player)     //----- Sinon => a = 1
            tour = 1;
        
        for (int i=0; i<64; i++) {
            if (pos.board[i] == tour) 
                count++; //----- Compter le nombre des pions correspond au player
        }
        
        float eval = 0;
        for(int i=0; i<64; i++)
        {
            if(pos.board[i] == 1) 
                eval += force[i];
            else if(pos.board[i] == -1) 
                eval -= force[i];
        }
        
        count = 65 - count;
        if (wonPosition(p, player))  {
            return eval + (1.0f / count);
        }
        if (wonPosition(p, !player))  {
            return -(eval + (1.0f / count));
        }
        return eval;
    }

    
    //--------- Fonction : printPosition ------------
    public void printPosition(Position p) {
        System.out.println("Board position:");
        OthelloPosition pos = (OthelloPosition) p;
        int count = 0;
        int k = 0;
        int h = 0; // HUMAN
        int pr = 0; // PROGRAM
        for (int row = 0; row < 8; row++) 
        {
            System.out.println();
            for (int col = 0; col < 8; col++) 
            {
                switch (pos.board[count]) {
                    case OthelloPosition.HUMAN:
                        //------- Dessiner le pion noir
                        System.out.print("H");
                        Home.board.getBoard()[k].drawPion(Color.black);
                        Home.board.repaint();
                        h++;
                        //System.out.println("Othello 128 ");
                        break;
                    case OthelloPosition.PROGRAM:
                        //------- Dessiner le pion blanc
                        System.out.print("P");
                        //System.out.println("Othello 133 : " + myBoard.getBoard()[k]);
                        Home.board.getBoard()[k].drawPion(Color.white);
                        Home.board.repaint();
                        pr++;
                        break;
                    default:
                        //---- la case est vide
                        System.out.print("o");
                        Home.board.getBoard()[k].eraseCellule();
                        Home.board.repaint();
                        break;
                }
                count++;
                k++;
            }
        }
        System.out.println();
        Home.noirTxt.setText(String.valueOf(h));
        Home.blancTxt.setText(String.valueOf(pr));
        h = 0;
        pr = 0;
    }

    //--------- Fonction : possibleMoves ------------
    @Override
    public Position[] possibleMoves(Position p, boolean player) {
        if (GameSearch.DEBUG) System.out.println("possibleMoves("+p+","+player+")");
        OthelloPosition pos = (OthelloPosition)p;
        
        int tour = -1;
        if (player) 
            tour=1;

        //------ Compter le nombre des cases occuppées
        int count = 0, j=0;
        
        for (int i=0; i<64; i++) 
            if (pos.board[i] != 0) 
                count++;
        int [] occupiedCell = new int[count];
        
        //------ Tables des positions occupées
        for (int i=0; i<64; i++)
        {
            if (pos.board[i] != 0)
            {
                occupiedCell[j] = i;
                j++;                
            }
        }
        
        //------ Identifier les voisins
        ArrayList<Integer> voisins = new ArrayList<Integer>();
        
        for (int i=0; i<count; i++)
        {
            //------ Stocker les 8 voisins de chaque occupiedCell
            int [] temp = new int[8];
            
            /*******************************************************************
             * Pour chaque occupiedPosition, on a 8 voisins, pour les identifier
             * on a besoin de revenir en arrier par 9,8 et 7 au niveau de la 
             * table ou d'avancer par 9, 8 et 7
             ******************************************************************/
            
            temp[0]= occupiedCell[i]-9;
            temp[1]= occupiedCell[i]-8;
            temp[7]= occupiedCell[i]+9;
            temp[6]= occupiedCell[i]+8;
            
            if (occupiedCell[i]%8 != 7)    //----- Les cases situées a droites
            {
                temp[4]= occupiedCell[i]+1;
                temp[2]= occupiedCell[i]-7;
            }
            else 
            {
                temp[4]= -1;
                temp[2]= -1;
            }
            if(occupiedCell[i]%8 != 0)      //----- Les cases situées a gauche
            {
                temp[3]= occupiedCell[i]-1;
                temp[5]= occupiedCell[i]+7;
            }
            else 
            {
                temp[3]= -1;
                temp[5]= -1;
            } 
//          System.out.print("la position occupee N° : " + i + " position: " + occupiedCell[i] + "\n");

            for (int k=0; k<8; k++)
            {
                for (int vois=0; vois<voisins.size(); vois++)
                {
                    //----- Verification si les voisins de occupiedCell[i] 
                    //----- n'existe pas sur adjacent
                    if (temp[k] == voisins.get(vois))
                    {
                        temp[k]= -1;
                    }  
                }
                //----- Verifier si les voisins sont occuppées 
                if(temp[k] >=0 && temp[k] <64)
                    if (pos.board[temp[k]] != 0) 
                        temp[k]=-1; 
            }
            //----- Ajouter les adjacents
            for (int k=0; k<8; k++)
            {
                if(temp[k] >=0 && temp[k] <64)
                    voisins.add(temp[k]);
            }
        }

        ArrayList<Position> rett = new ArrayList<Position>();
        
        for (int i=0; i<voisins.size(); i++)
        {
            int actuel = voisins.get(i);   
            int col = (actuel + 1)% 8;
            
            boolean add = false;
            
            /*******************************************************************
            * Parmi les conditions de jeu Othello on a :
            *  1- il existe au moins une case adjacente de l'aversaire
            *  2- il existe au moins une pion de l'adversaire sur la ligne,
            *  la colonne et le diagonale 
            *******************************************************************/
            
            OthelloPosition pos2 = new  OthelloPosition();
            //------- La ligne a droite
            for (int l=0; l<64; l++) 
            {
                pos2.board[l] = pos.board[l];
                int h = actuel+1;
                
                while(h>=0 && h<64 && h<actuel-col+8)
                {
                    if(pos.board[h] == 0)
                        break;
                    else if(pos.board[h] == tour)
                    {
                        if(h != actuel+1)
                        {
                            int m = h;
                            while ( m>=actuel && m>=0 && m<64)
                            {
                                pos2.board[m]=tour;
                             m--;
                            }
                            add = true; //added: pour verifier si ce possible move est deja ajoute a ret
                        }
                        break;
                    }
                    h++;
                }
            }
            //--------la ligne a gauche
            {
                int h=actuel-1;
                while(h>=0 && h<=63 && h>actuel-col )
                {
                    if(pos.board[h] == 0)
                        break;
                    else if(pos.board[h] == tour)
                    {
                        if(h != actuel-1)
                        {
                            int m = h;
                            while (m<=actuel && m>=0 && m<64)
                            {
                                pos2.board[m]=tour;
                                m++ ;
                            }
                            add = true;
                        }
                        break;
                    }
                    h--;
                }
            }
            //--------la colonne en bas
            {
                int h = actuel+8;
                while(h>=0 && h<64)
                {
                if(pos.board[h] == 0)
                    break;
                else if(pos.board[h] == tour)
                {
                    if(h != actuel+8)
                    {
                        int m=h;
                        while (m>=actuel && m>=0 && m<64)
                        {
                            pos2.board[m]=tour;
                            m-=8;
                        }
                        add = true;
                    }
                    break;
                } 
                h+=8;
                }
            }
            //------- la colonne en haut
            {
            int h = actuel-8;
                while(h>=0 && h<=63)
                {
                if(pos.board[h] == 0)
                    break;
                else if(pos.board[h] == tour)
                {
                    if(h != actuel-8)
                    {
                        int m=h;
                        while(m<=actuel && m>=0 && m<64)
                        {
                            pos2.board[m]=tour;
                            m+=8;
                        }
                    add=true;
                    }
                    break;
                } 
                h-=8;
                }
            }
            //--------- Diagonale a gauche en bas
            { 
                int temp = (actuel+7+1)%8;
                int h=actuel+7;
                
                while(h>=0 && h<64 && temp<col)
                {
                    temp = (h+1)%8;
                    if(pos.board[h] == 0)
                        break;
                    else if(pos.board[h] == tour)
                    {
                        if(h != actuel+7)
                        {
                            int m = h;
                            while(m>=actuel && m>=0 && m<64)
                            {
                                pos2.board[m]=tour;
                                m-=7;
                            }
                            add = true;
                        }
                        break;
                    } 
                    h+=7;
                }
            }
            //-------- Diagonale en haut a droite
            {
                int temp = (actuel-7+1)%8;
                int h = actuel-7;
                while(h >=0 && h<=63 && temp>col)
                { 
                    temp = (h+1)%8;
                    if(pos.board[h] == 0)
                        break;
                    else if(pos.board[h] == tour)
                    {
                        if(h != actuel-7)
                        {
                            int m = h;
                            while(m<=actuel && m>=0 && m<64)
                            {
                                pos2.board[m]=tour;
                            m+=7 ;
                            }
                            add = true;
                        }
                        break;
                    }
                    h-=7;
                }
            }
            //-------- diagonale en bas a droite
            {
                int temp = (actuel+9+1)%8;
                int h = actuel+9;
                
                while(h>=0 && h<=63 && temp>actuel)
                {
                    temp = (h+1)%8;
                    if(pos.board[h] == 0)
                        break;
                    else if(pos.board[h] == tour)
                    {
                        if(h != actuel+9)
                        {
                            int m = h;
                            while(m>=actuel && m>=0 && m<64)
                            {
                                pos2.board[m]=tour;
                                m-=9;
                            }
                                add = true;
                        }
                        break;
                    }
                    h+=9;
                }
            }
            //--------- diagonale en haut a gauche
            {
                int temp = (actuel-9+1)%8;
                int h = actuel-9;
                while(h>=0 && h<=63 && temp<col)
            {
                temp=(h+1)%8;
                if(pos.board[h] == 0)
                    break;
                else if(pos.board[h] == tour)
                {
                    if(h != actuel-9)
                    {
                        int m = h;
                        while(m<=actuel && m>=0 && m<64)
                        {
                            pos2.board[m]=tour;
                        m+=9;
                        }
                        add = true;
                    }
                    break;
                } 
                h-=9;
            }
            }
            if (add)
            {
                rett.add(pos2);
            }
        }
        if (rett.size()==0)
            return null;
        
        Position [] ret = new Position[rett.size()];
        for (int i=0; i<rett.size(); i++)
        {
            ret[i]=rett.get(i);
        }
        return ret;
    }

    
    //--------- Fonction : makeMoves ------------
    @Override
    public Position makeMove(Position p, boolean player, Move move) {
        if (GameSearch.DEBUG) 
            System.out.println("Entered Othello.makeMove");
        
        OthelloMove m = (OthelloMove)move;
        OthelloPosition pos = (OthelloPosition)p;
        OthelloPosition pos2 = new  OthelloPosition();
        
        for (int i = 0; i < 64; i++) 
            pos2.board[i] = pos.board[i];
        int pp;
        if (player) // HUMAN
            pp =  1;
        else // PROGRAM   
            pp = -1;
        if (GameSearch.DEBUG) 
            System.out.println("makeMove: m.moveIndex = " + m.moveIndex);
        System.out.println("move index  : " + m.moveIndex);
        pos2.board[m.moveIndex] = pp;
        
        int a = m.moveIndex-8;
        int b = m.moveIndex+8;
        int c = m.moveIndex-1;
        int d = m.moveIndex+1;
        int e = m.moveIndex-9;
        int f = m.moveIndex+9;
        int g = m.moveIndex-7;
        int h = m.moveIndex+7;
        
        boolean var = false;
        
        ArrayList<Integer> lst = new ArrayList<Integer>();
        
        while(a > 0)        //-------- Les voisins en haut verticalement
        {
            if(pos.board[a] == pp*-1) 
            {
                lst.add(a);
                a-=8;
                var = true;
            } 
            else if(pos.board[a] == pp)
            {
                if(var)
                {
                    for(int j=0 ; j<lst.size();j++) 
                        pos2.board[lst.get(j)]=pp;
                }
                break;
            }
            else break;
        }
        var = false;
        lst.clear();
        
        while(b<64)       //-------- Les voisins en bas verticalement
        {
            if(pos.board[b]==pp*-1) 
            {
                lst.add(b);
                b+=8;
                var = true;
            } 
            else if(pos.board[b]==pp)
            {
                if(var)
                {
                    for(int j=0 ; j<lst.size();j++) 
                        pos2.board[lst.get(j)]=pp;
                }
                break;
            }
            else break;
        }
        var = false;
        lst.clear();
        
        
        int temp = c/8*8;
        while(c>temp-1)          //-------- Les voisins a gauche horizontalement
        {
            if(pos.board[c]==pp*-1) 
            {
                lst.add(c);
                c-=1;
                var = true;
            }
            else if(pos.board[c]==pp)
            {
                if(var )
                {
                 for(int j=0; j<lst.size() ;j++) 
                 pos2.board[lst.get(j)]=pp;
                }
            break;
            }
            else break;
        }
        var = false;
        lst.clear();
        
        
        int temp1 =(d/8*8)+8;   //--------- Les voisins à droite horizontalement
        while(d<temp1)
        {
            if(pos.board[d]==pp*-1) 
            {
                lst.add(d);
                d+=1;
                var = true;
            } 
            else if(pos.board[d]==pp)
            {
                if(var)
                {
                    for(int j=0;j<lst.size();j++) 
                    pos2.board[lst.get(j)]=pp;
                }
            break;
            }
            else break;
        }
        
        var = false;
        lst.clear();
        
        if(m.moveIndex%8!=0 && e>=0) //--------- Les voisins au niveau de diagonales 
        {
            do {
                if(pos.board[e]==pp*-1) 
                {
                    lst.add(e);
                    var = true;
                    e-=9;
                } 
                else if(pos.board[e]==pp)
                {
                    if(var)
                    {
                        for(int j=0;j<lst.size();j++) 
                        pos2.board[lst.get(j)]=pp;
                    }
                    break;
                }
                else 
                    break;
            }while(e%8!=0 && e>=0);
        }
        var = false;
        lst.clear();
       
        if(m.moveIndex%8!=0 && h<64)
        {
            do 
            {
                if(pos.board[h]==pp*-1) 
                {
                    lst.add(h);
                    var = true;
                    h+=7;
                }
                else if(pos.board[h]==pp)
                {
                    if(var)
                    {
                        for(int j=0; j<lst.size() ;j++) 
                            pos2.board[lst.get(j)]=pp;
                    }
                    break;
                }
                else 
                    break;
            }while(h%8!=0 && h<64);
        }
        var = false;
        lst.clear();
        if((m.moveIndex+1)%8!=0 && g>=0)
        {
            do {
                if(pos.board[g]==pp*-1) 
                {
                    lst.add(g);
                    var = true;
                    g-=7;
                } 
                else if(pos.board[g]==pp)
                {
                    if(var)
                    {
                        for(int j=0; j<lst.size() ;j++) 
                            pos2.board[lst.get(j)]=pp;
                    }
                    break;
                }
                else 
                    break;
            }while((g+1)%8!=0 && g >= 0);
         }
         var = false;
         lst.clear();
         
        while(f<64)
        {
         if(pos.board[f]==pp*-1) 
            {
              lst.add(f);
                f+=9;
                var = true;
            } 
            else if(pos.board[f]==pp)
            {
                if(var)
                {
                    for(int j=0; j<lst.size(); j++) 
                    pos2.board[lst.get(j)]=pp;
                }
                break;
            }
            else break;
        }
        return pos2;
    }

    
    //--------- Fonction : reachedMaxDepth ------------
    @Override
    public boolean reachedMaxDepth(Position p, int depth) {
        boolean ret = false;
        if(depth>=5) 
            ret = true;
        else 
        if (wonPosition(p, false)) 
            ret = true;
        else if (wonPosition(p, true))  
            ret = true;
        else if (drawnPosition(p))      
            ret = true;
        if (GameSearch.DEBUG) {
            System.out.println("reachedMaxDepth: pos=" + p.toString() + ", depth="+depth
                               +", ret=" + ret);
        }
        return ret;
    }

    //--------- Fonction : createMove ------------
    @Override
    public Move createMove() {
        OthelloMove mm = new OthelloMove();
        
        state = true;
        Thread t = new Thread() {
            public void run() {     
                for( ; ; ) {
                    if(state == true) {
                            try {
                                sleep(1);

                                if(milliseconds > 1000) {
                                    milliseconds = 0;
                                    seconds++;
                                }
                                if(seconds > 15) {
                                    milliseconds = 0;
                                    seconds = 0;
                                    reset();
                                    System.out.println("Time out !");
                                    break;
                                }                                                     
                                Home.millisecond.setText(" : "+milliseconds);
                                milliseconds++;
                                Home.second.setText("  "+seconds);                                                        
                            } catch(Exception e){ }
                        
                    }
                    else {
                        break;
                    }
                }
                
            }
        };
        t.start();

while(Outils.isClicked == false)
        {
            System.out.print("");
            if(seconds == 15)
            {
                mm = null;
                Outils.gameOver = true;
                //Outils.move = null;
                break;
            }
            if(Outils.isClicked == true)
            {
                //sc.close();
                System.out.print("Your move : " + Outils.moveIndex);
                mm.moveIndex = Outils.moveIndex;
                //Outils.move = mm;
                Outils.isClicked = false;
                Outils.gameOver = false;
                //System.out.println("e != null");
                reset();
                break;
            }
        }
        return mm;
    }
    
    //------ Reinitialiser le timer ---------
    public void reset() {
        state = false;
        milliseconds = 0;
        seconds = 0;

        Home.millisecond.setText("00 : ");
        Home.second.setText("00 : ");
    }
}

