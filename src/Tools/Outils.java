/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;


public class Outils {
    
    public static boolean isClicked = false; //---- un boolean qui retourne true 
                                            //si le joueur clique sur le board pour dessiner un pion
    
    public static int moveIndex = 0;       //----- la position de pion à dessiner
    public static Position position;
    public static int depth = 0;           //----- la profendeur
    public static boolean gameOver = false;         //----- Si le joueur depasse le timer, on va retourner True
    public static OthelloPosition lastPosition = new OthelloPosition();
}

