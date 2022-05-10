package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardOne {

    private List<Time> top5;

    public LeaderboardOne() {
        top5 = new ArrayList<>();
    }

    public List<Time> getTop5() {
        List<Time> copyOfTop5 = new ArrayList<>(top5);
        return copyOfTop5;
    }

    public void setTop5(List<Time> top5) {
        this.top5 = top5;
    }

    public void addTime(String name, double time) {
        Time player = new Time(name, time);

        if (top5.size() < 5) {
            top5.add(player);
            Collections.sort(top5);
        } else if (player.getTime() < top5.get(top5.size() - 1).getTime()) {
            top5.remove(top5.get(top5.size() - 1));
            top5.add(player);
            Collections.sort(top5);
        }
    }
}


