package byow.Core;

import byow.TileEngine.TETile;
import com.github.javaparser.utils.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Chunk {
    private int size;
    private int x;
    private int y;
    private TETile chunk[][];
    private Room roomTree;

    public Chunk(int size, int x, int y) {
        this.size = size;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the walls of the rooms to connect
     *
     * @param other chunk to connect this Chunk to
     * @param random Random object
     * @return an ArrayList of Pairs containing the coordinates of walls to connect
     */
    public ArrayList<Pair<Integer, Integer>> connect(Chunk other, Random random) {
        return this.roomTree.connect(other.roomTree, random);
    }
}
