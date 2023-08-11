// Definition of a binary tree node
class TreeNode {
    int val; // Value of the node
    TreeNode left; // Reference to the left child
    TreeNode right; // Reference to the right child

    TreeNode(int val) {
        this.val = val;
    }
}

// Class to determine if two nodes in a binary tree are siblings
public class Q4b {
    private TreeNode parentX = null; // Parent of node X
    private TreeNode parentY = null; // Parent of node Y
    private int depthX = -1; // Depth of node X
    private int depthY = -1; // Depth of node Y

    // Main method to check if two nodes are siblings
    public boolean areSiblings(TreeNode root, int x, int y) {
        // Traverse the tree and find the nodes and their depths
        findNodes(root, null, x, y, 0);

        // Return true if the nodes have different parents and are at the same depth,
        // indicating they are siblings
        return parentX != parentY && depthX == depthY;
    }

    // Recursive method to find nodes and their depths
    private void findNodes(TreeNode node, TreeNode parent, int x, int y, int depth) {
        // Base case: If the current node is null, return
        if (node == null) {
            return;
        }

        // Check if the current node is node X
        if (node.val == x) {
            parentX = parent; // Set parent of node X
            depthX = depth; // Set depth of node X
        }
        // Check if the current node is node Y
        else if (node.val == y) {
            parentY = parent; // Set parent of node Y
            depthY = depth; // Set depth of node Y
        }

        // Recursively search the left and right subtrees
        findNodes(node.left, node, x, y, depth + 1); // Search left subtree
        findNodes(node.right, node, x, y, depth + 1); // Search right subtree
    }

    // Main method to test the Q4b
    public static void main(String[] args) {
        // Create a sample binary tree
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);

        int x = 4; // Node value to check for X
        int y = 3; // Node value to check for Y

        // Create an instance of Q4b
        Q4b checker = new Q4b();

        // Check if nodes with values x and y are siblings
        boolean result = checker.areSiblings(root, x, y);

        // Print the result
        System.out.println(result); // Output: false
    }
}
