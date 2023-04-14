package byow.Core;


import com.github.javaparser.utils.Pair;
import java.util.Random;

public class RoomTree {
    private int[] parent;
    private int[] rank; // sizes of trees
    private int size;

    // Creates n sets with single item in each
    public void makeSet() {
        for (int i = 0; i < size; i++) {
            parent[i] = -1;
        }
    }

    // @GeeksforGeeks https://www.geeksforgeeks.org/disjoint-set-data-structures/
    public RoomTree(int n) {
        rank = new int[n];
        parent = new int[n];
        this.size = n;
        makeSet();
    }

    public void printParent() {
        System.out.println(parent.toString());
    }

    public void printRank() {
        System.out.println(rank.toString());
    }

    public Pair<Integer, Integer> pickDisjointRooms(Random random) {
        int rmOne = random.nextInt(0, size);
        int rmTwo = random.nextInt(0, size);
        while (find(rmOne) == find(rmTwo)) {
            System.out.println("HERE");
            rmTwo = random.nextInt(0, size);
        }
        return new Pair<>(rmOne, rmTwo);
    }

    // checks if disjoint set is fully connected
    public boolean fullyConnected() {
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == (-1 * size)) {
                return true;
            }
        }
        return false;
    }

    private void setParent(int val, int root) {
        this.parent[val] = root;
    }

    // Returns representative of x's set
    public int find(int val) {
        int p = this.parent[val];
        if (p < 0) {
            return val;
        } else {
            int root = find(p);
            //setParent(val, root);
            return root;
        }
    }

    // Unites the set that includes x and the set that includes y
    public void union(int x, int y) {
        // subtract parent[yRoot] from parent[xRoot]

        // Find representatives of two sets
        int xRoot = find(x), yRoot = find(y);
        if (xRoot == yRoot) {
            return;
        }
        if (rank[xRoot] < rank[yRoot]) {
            parent[yRoot] += parent[xRoot];
            parent[xRoot] = yRoot;
            parent[xRoot] = yRoot;
        } else if (rank[yRoot] < rank[xRoot]) { // Else if y's rank is less than x's rank
            parent[xRoot] += parent[yRoot];
            parent[yRoot] = xRoot;
            parent[yRoot] = xRoot;
        } else { // if ranks are the same
            parent[xRoot] += parent[yRoot];
            parent[yRoot] = xRoot;

            rank[xRoot]++;
        }
    }
}
