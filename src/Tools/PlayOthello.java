/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Algorithm.Othello;

public class PlayOthello  implements Runnable {

    @Override
    public void run() {
        Othello ttt = new Othello();
        ttt.playGame(Outils.position, true);
    }
    
}
