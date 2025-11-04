package model;

import java.io.Serializable;

public class Playing implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int point;
    private String result;
    private Player player;

    public Playing() {}

    public Playing(int point, String result, Player player) {
        this.point = point;
        this.result = result;
        this.player = player;
    }

    public Playing(int id, int point, String result, Player player) {
        this.id = id;
        this.point = point;
        this.result = result;
        this.player = player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "Playing{" +
                "id=" + id +
                ", point=" + point +
                ", result='" + result + '\'' +
                ", player=" + player +
                '}';
    }
}
