package GameServer.main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardOne {

    private List<Time> top10;

    public LeaderboardOne() {
        top10 = new ArrayList<>();
    }

    public List<Time> getTop10() {
        List<Time> copyOfTop10 = new ArrayList<>(top10);
        return copyOfTop10;
    }

    public void setTop10(List<Time> top10) {
        this.top10 = top10;
    }

    public void addTime(String name, double time) {
        Time player = new Time(name, time);

        if (top10.size() < 10) {
            top10.add(player);
            Collections.sort(top10);
        } else if (player.getTime() < top10.get(top10.size() - 1).getTime()) {
            top10.remove(top10.get(top10.size() - 1));
            top10.add(player);
            Collections.sort(top10);
        }

    }
}


