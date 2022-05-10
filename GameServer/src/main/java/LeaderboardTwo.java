package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardTwo {

    private List<AverageSpeed> topFive;

    public LeaderboardTwo() {
        topFive= new ArrayList<>();
    }

    public List<AverageSpeed> getTopFive() {
        List<AverageSpeed> copyOfTopFive = new ArrayList<>(topFive);
        return copyOfTopFive;
    }

    public void setTopFive(List<AverageSpeed> topFive) {
        this.topFive = topFive;
    }

    public void addAvgSpeed(String name, double avgSpeed) {
        AverageSpeed player = new AverageSpeed(name,avgSpeed);

        if (topFive.size() < 5) {
            topFive.add(player);
            Collections.sort(topFive);
        } else if (player.getAvgSpeed() > topFive.get(topFive.size() - 1).getAvgSpeed()) {
            topFive.remove(topFive.get(topFive.size() - 1));
            topFive.add(player);
            Collections.sort(topFive);
        }

    }
}


