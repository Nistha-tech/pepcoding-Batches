import java.util.ArrayList;

public class l001_BT {

    public static class Node {

        int data = 0;
        Node left = null;
        Node right = null;

        public Node() {

        }

        public Node(int data) {
            this.data = data;
        }

        public Node(int data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {

            String str = "";
            str += ((left != null ? left.data : "."));
            str += (" -> " + data + " <- ");
            str += ((right != null ? right.data : "."));
            str += "\n";

            if (left != null)
                str += left.toString();
            if (right != null)
                str += right.toString();

            return str;
        }

    }

    static int idx = 0;

    public static Node createTree(int[] arr) {
        if (idx == arr.length || arr[idx] == -1) {
            idx++;
            return null;
        }

        Node node = new Node(arr[idx]);
        idx++;

        node.left = createTree(arr);
        node.right = createTree(arr);

        return node;
    }

    public static void display(Node node) {
        if (node == null)
            return;

        System.out.print((node.left != null ? node.left.data : "."));
        System.out.print(" -> " + node.data + " <- ");
        System.out.println((node.right != null ? node.right.data : "."));

        display(node.left); // display(2*idx+1);
        display(node.right); // display(2*idx+2);

    }

    // basic.============================

    public static int maximumInTree(Node node) {
        if (node == null)
            return (int) -1e8;

        int lmax = maximumInTree(node.left);
        int rmax = maximumInTree(node.right);
        int oMax = Math.max(lmax, rmax);

        return Math.max(node.data, oMax);
    }

    public static boolean find(Node node, int data) {
        if (node == null)
            return false;

        if (node.data == data)
            return true;
        boolean res = false;
        res = res || find(node.left, data);
        res = res || find(node.right, data);
        return res;

    }

    public static int size(Node node) {
        if (node == null)
            return 0;
        return size(node.left) + size(node.right) + 1;

    }

    public static int height(Node node) {
        if (node == null)
            return -1;
        return Math.max(height(node.left), height(node.right)) + 1;

    }

    // set1.==================================

    public static ArrayList<Node> rootToNodePath(Node node, int data) {
        if (node == null) {
            ArrayList<Node> base = new ArrayList<>();
            return base;
        }

        if (node.data == data) {
            ArrayList<Node> base = new ArrayList<>();
            base.add(node);
            return base;
        }

        ArrayList<Node> left = rootToNodePath(node.left, data);
        if (left.size() != 0) {
            left.add(node);
            return left;
        }
        ArrayList<Node> right = rootToNodePath(node.right, data);
        if (right.size() != 0) {
            right.add(node);
            return right;
        }

        return new ArrayList<>();
    }

    public static int LCA_01(Node node, int data1, int data2) {
        ArrayList<Node> list1 = rootToNodePath(node, data1);
        ArrayList<Node> list2 = rootToNodePath(node, data2);

        int ans = -1;
        int i = list1.size() - 1;
        int j = list2.size() - 1;
        while (i >= 0 && j >= 0) {
            if (list1.get(i) != list2.get(j)) {
                break;
            }

            ans = list1.get(i).data;
            i--;
            j--;
        }

        return ans;

    }

    public static void kDown(Node node, Node pnode, int level) {
        if (node == null || node == pnode) {
            return;
        }

        if (level == 0) {
            System.out.print(node.data + " ");
            return;
        }

        kDown(node.left, pnode, level - 1);
        kDown(node.right, pnode, level - 1);
    }

    public static void KAway_01(Node node, int data, int k) {
        ArrayList<Node> list1 = rootToNodePath(node, data);
        Node pnode = null;

        for (int i = 0; i < list1.size(); i++) {
            kDown(list1.get(i), pnode, k - i);
            pnode = list1.get(i);
        }
    }

    public static int kAway_02(Node node, int data, int k) {
        if (node == null)
            return -1;

        if (node.data == data) {
            kDown(node, null, k);
            return 1;
        }
        int ld = kAway_02(node.left, data, k);
        if (ld != -1) {
            // if(k-ld==0) System.out.print(node.data + " ");
            // else if(node.right!=null){
            // kDown(node.right, node.left, k - ld-1);
            // }

            kDown(node, node.left, k - ld);
            return ld + 1;
        }
        int rd = kAway_02(node.right, data, k);
        if (rd != -1) {
            kDown(node, node.right, k - rd);
            return rd + 1;
        }

        return -1;
    }

    int diameter_01(Node node) {
        if (node == null)
            return 0;

        int ld = diameter_01(node.left);
        int rd = diameter_01(node.right);

        int lh = height(node.left);
        int rh = height(node.right);

        return Math.max(Math.max(ld, rd), lh + rh + 2);
    }

    int[] diameter_02(Node node) {
        if (node == null)
            return new int[] { 0, -1 };

        int[] ld = diameter_02(node.left);
        int[] rd = diameter_02(node.right);

        int[] myAns = new int[2];
        myAns[0] = Math.max(Math.max(ld[0], rd[0]), ld[1] + rd[1] + 2);
        myAns[1] = Math.max(ld[1], rd[1]) + 1;

        return myAns;
    }

    static int maxDia = 0;

    int diameter_03(Node node) {
        if (node == null)
            return -1;

        int lh = diameter_03(node.left);
        int rh = diameter_03(node.right);

        maxDia = Math.max(maxDia, lh + rh + 2);
        return Math.max(lh, rh) + 1;

    }

    static int MaxSum = Integer.MIN_VALUE;

    public static int leafToLeafSum(Node node) {
        if (node == null)
            return Integer.MIN_VALUE;

        if (node.left == null && node.right == null) { // leaf
            return node.data;
        }

        int lmaxSum = leafToLeafSum(node.left);
        int rmaxSum = leafToLeafSum(node.right);

        if (node.left != null && node.right != null)
            MaxSum = Math.max(MaxSum, lmaxSum + rmaxSum + node.data);

        return Math.max(lmaxSum, rmaxSum) + node.data;
    }

    static int MaxSum1 = Integer.MIN_VALUE;

    public static int nodeToNodeSum(Node node) {
        if (node == null)
            return 0;

        int lmaxSum = nodeToNodeSum(node.left);
        int rmaxSum = nodeToNodeSum(node.right);

        int maxbranch = Math.max(lmaxSum, rmaxSum);

        MaxSum1 = Math.max(Math.max(MaxSum1, node.data),
                Math.max(maxbranch + node.data, lmaxSum + rmaxSum + node.data));

        return Math.max(maxbranch + node.data, node.data);
    }

    // -1 : i need a camera.
    // 0 : i already covered.
    // 1 : im a camera
    static int cameras = 0;

    public static int minCameras_(Node node) {
        if (node == null)
            return 0;

        int left = minCameras_(node.left);
        int right = minCameras_(node.right);

        if (left == -1 || right == -1) {
            camera++;
            return 1;
        }

        if (left == 1 || right == 1) {
            return 0;
        }

        return -1;
    }

    public static int minCameras(Node node) {
        int val = minCameras_(node);
        if (val == -1)
            camera++;
        return camera;
    }

    public static void set1(Node node) {
        // System.out.println(LCA_01(node, 40, 70));
        // KAway_01(node, 60, 2);
        leafToLeafSum(node);
        System.out.println(MaxSum);
    }

    public static void solve() {
        int[] arr = { 10, 20, 30, 40, -1, -1, 50, -1, -1, 60, -1, 70, -1, -1, 80, 90, 100, 120, -1, -1, 130, -1, -1,
                110, -1, -1, 140, -1, -1 };

        Node root = createTree(arr);
        System.out.println(root);
        set1(root);
    }

    public static void main(String[] args) {
        solve();
    }

}