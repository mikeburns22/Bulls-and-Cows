
package corbos.bullscows.models;


public class Game {
    
    private int id;
    private boolean finished;
    private String finalAnswer;
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public void setAnswer(String answer) {
        this.finalAnswer = answer;
    }
    
    public String getAnswer() {
        return this.finalAnswer;
    }
    
    @Override
    public String toString() {
        return "GAME [" + id + "]: " + finalAnswer + ", Complete: " + finished;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if ((o instanceof Game)) {
            Game game = (Game) o;
            if ((id == game.getId())
                    && (finished == game.isFinished())) {
                if (!finished) {
                    return true;
                } else if (finalAnswer.equals(game.getAnswer())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
}
