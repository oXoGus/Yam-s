package fr.uge.yams;

public class DuoIA implements Game {
    private final Player player ;
    private final AI ai ;

    public DuoIA(){
        System.out.println("Welcome ! Please enter your name : \n");
        player=new Player();
        ai=new AI();
    }
    
    @Override
    public void playRounds(){
        for (int i = 0; i<7; i++) {
            for (int j=0; j<2; j++) {
                if (j==0) {
                    player.playRound();
                } 
                else {
                    ai.playRound();
                }
            }
        }
    }

    @Override 
    public void endScreen(){
        System.out.println("End of the game !\n");
        System.out.println("Here are the results :\n");
    }
}
