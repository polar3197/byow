package byow.Core;


public class RoomTree {
    private int[] parent;
    private int[] rank; // sizes of trees
    private int size;

    // @GeeksforGeeks https://www.geeksforgeeks.org/disjoint-set-data-structures/
    public RoomTree(int n) {
        rank = new int[n];
        parent = new int[n];
        this.size = n;
        makeSet();
    }

    // Creates n sets with single item in each
    void makeSet() {
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    // Returns representative of x's set
    public int find(int val) {
        int p = this.parent[val];
        if (p == val) {
            return val;
        } else {
            int root = find(p);
            return root;
        }
    }

    private void setParent(int val, int root) {
        this.parent[val] = root;
    }

    // Unites the set that includes x and the set that includes y
    public void union(int x, int y) {
        // Find representatives of two sets
        int xRoot = find(x), yRoot = find(y);
        if (xRoot == yRoot) {
            return;
        }
        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot;
        } else if (rank[yRoot] < rank[xRoot]) { // Else if y's rank is less than x's rank
            parent[yRoot] = xRoot;
        } else { // if ranks are the same
            parent[yRoot] = xRoot;
            rank[xRoot]++;
        }
    }
}
