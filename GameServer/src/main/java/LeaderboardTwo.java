package GameServer.main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardTwo {

    private List<AverageSpeed> topTen;

    public LeaderboardTwo() {
        topTen= new ArrayList<>();
    }

    public List<AverageSpeed> getTopTen() {
        List<AverageSpeed> copyOfTopTen = new ArrayList<>(topTen);
        return copyOfTopTen;
    }

    public void setTopTen(List<AverageSpeed> topTen) {
        this.topTen = topTen;
    }

    public void addAvgSpeed(String name, double avgSpeed) {
        AverageSpeed player = new AverageSpeed(name,avgSpeed);

        if (topTen.size() < 10) {
            topTen.add(player);
            Collections.sort(topTen);
        } else if (player.getAvgSpeed() > topTen.get(topTen.size() - 1).getAvgSpeed()) {
            topTen.remove(topTen.get(topTen.size() - 1));
            topTen.add(player);
            Collections.sort(topTen);
        }

    }
}


