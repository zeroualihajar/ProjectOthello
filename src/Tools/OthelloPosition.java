/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

public class OthelloPosition extends Position {
    final static public int BLANK = 0;      //----- une case vide prend la valeur 0
    final static public int HUMAN = 1;      //----- la valeur remplie par le joueur prend la valeur 1
    final static public int PROGRAM = -1;   //----- la valeur remplie par l'adversaire prend la valeur -1
    public int [] board = new int[64];      //----- Le plateau de 64 cases (8x8)
    
    //---------- Constructeur --------------
    public OthelloPosition() {
        
        //----- les pions de départ
        board[27] = -1;         
        board[28] = 1;
        board[35] = 1;
        board[36] = -1;
        
        //----- Si une case n'est pas remplie par 1 ou -1, il va etre vide => 0
        for(int i = 0 ; i < 64; i ++)
        {
            if(board[i] != -1 && board[i] != 1)
                board[i] = 0;
        }
    }
    
    //---------- Getters & Setters --------------
    public int[] getBoard() {
        return this.board;
    }
    
    public void setBoard(int[] board) {
        this.board = board;
    }
    
    
    //----- un boolean qui decrit l'etat d'une partie joué, 
    //----- s'il est terminer etat =1, sinon etat = 0
    public boolean etat() {
        for(int i = 0; i < 64; i++)
            if(this.board[i] == 0)
                return false;
        return true;
    }
    
    //---------- toString()-----------------
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (int i=0; i<64; i++) {
            sb.append(""+board[i]+",");
        }
        sb.append("]");
        return sb.toString();
    }
}




