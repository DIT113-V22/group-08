import static java.lang.Double.compare;

public class AverageSpeed implements Comparable<AverageSpeed> {

    private String playerName;
    private double avgSpeed;


    public AverageSpeed(String playerName, double avgSpeed) {
        this.playerName = playerName;
        this.avgSpeed = avgSpeed;
    }

    //Default Constructor
    public AverageSpeed() {
        playerName = "";
        avgSpeed = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    @Override
    public int compareTo(AverageSpeed otherAvgSpeed) {
        return compare( otherAvgSpeed.getAvgSpeed(),getAvgSpeed());
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Average Speed: %f", playerName, avgSpeed);
    }

}

