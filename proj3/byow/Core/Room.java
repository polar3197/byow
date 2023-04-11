package byow.Core;

import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
    private int width;
    private int height;
    private int x;
    private int y;
    private ArrayList<Room> neighbors;
    private ArrayList<ArrayList<Integer>> walls;

    public Room(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        neighbors = new ArrayList<>();
        ArrayList<Integer> left_wall = new ArrayList<>(List.of(this.x, this.y, this.x, this.y + this.height - 1));
        ArrayList<Integer> top_wall = new ArrayList<>(List.of(this.x, this.y + this.height - 1, this.x + this.width - 1, this.y + this.height - 1));
        ArrayList<Integer> right_wall = new ArrayList<>(List.of(this.x + this.width - 1, this.y, this.x + this.width - 1, this.y + this.height - 1));
        ArrayList<Integer> bot_wall = new ArrayList<>(List.of(this.x, this.y, this.x + this.width - 1, this.y));
        this.walls.add(left_wall);
        this.walls.add(top_wall);
        this.walls.add(right_wall);
        this.walls.add(bot_wall);
    }

    /**
     * Computes the closest walls of this Room and the given Room and returns the
     * coordinates of their midpoints.
     *
     * @param other other Room to connect to
     * @param random Random object
     * @return an ArrayList of Pairs containing the coordinates of walls to connect
     */
    public ArrayList<Pair<Integer, Integer>> connect(Room other, Random random) {
        int wall1x = 0;
        int wall1y = 0;
        int wall2x = 0;
        int wall2y = 0;
        double min_dist = Float.MAX_VALUE;
        for (ArrayList<Integer> wall : this.walls) {
            for (ArrayList<Integer> other_wall : other.walls) {
                float mid1x = (wall.get(0) + wall.get(2)) / 2;
                float mid1y = (wall.get(1) + wall.get(3)) / 2;
                float mid2x = (other_wall.get(0) + other_wall.get(2)) / 2;
                float mid2y = (other_wall.get(1) + other_wall.get(3)) / 2;
                double cur_dist = Math.sqrt((mid2x - mid1x)*(mid2x - mid1x) + (mid2y - mid1y)*(mid2y - mid1y));
                if (cur_dist < min_dist) {
                    min_dist = cur_dist;
                    wall1x = (int) mid1x;
                    wall1y = (int) mid1y;
                    wall2x = (int) mid2x;
                    wall2y = (int) mid2y;
                }
            }
        }
        return new ArrayList<>(List.of(new Pair(wall1x, wall1y), new Pair(wall2x, wall2y)));
    }
}
