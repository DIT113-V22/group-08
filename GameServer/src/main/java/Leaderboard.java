import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard {

    private List<Time> top10;
    private List<AverageSpeed> topTen;

    public Leaderboard() {
        top10 = new ArrayList<>();
        topTen= new ArrayList<>();
    }

    public List<Time> getTop10() {
        List<Time> copyOfTop10 = new ArrayList<>(top10);
        return copyOfTop10;
    }

    public void setTop10(List<Time> top10) {
        this.top10 = top10;
    }

    public List<AverageSpeed> getTopTen() {
        List<AverageSpeed> copyOfTopTen = new ArrayList<>(topTen);
        return copyOfTopTen;
    }

    public void setTopTen(List<AverageSpeed> topTen) {
        this.topTen = topTen;
    }

    public void addTime(String name, double time) {
        Time player = new Time(name,time);

        if (top10.size() < 10) {
            top10.add(player);
            Collections.sort(top10);
        } else if (player.getTime() < top10.get(top10.size() - 1).getTime()) {
            top10.remove(top10.get(top10.size() - 1));
            top10.add(player);
            Collections.sort(top10);
        }

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

