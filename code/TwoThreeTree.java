package main;

import java.util.LinkedList;
import java.util.Queue;

public class TwoThreeTree {
    private int size;
    private TreeNode root;
    private boolean successfulInsertion;
    private boolean successfulDeletion;
    private boolean split;
    private boolean underflow;
    private boolean first;
    private boolean singleNodeUnderflow;
    private static long countInsertIterations;
    private static long countRemoveIterations;
    private static long countSearchIterations;
    public TwoThreeTree() {
        size = 0;
        root = null;
        successfulInsertion = false;
        successfulDeletion = false;
        underflow = false;
        singleNodeUnderflow = false;
        split = false;
        first = false;
        countInsertIterations = 0;
        countRemoveIterations = 0;
        countSearchIterations = 0;
    }
    private void insertKey(int key) {
        Node[] array = new Node[2];
        array = insert(key, root);

        if (array[1] == null) {
            root = (TreeNode) array[0];
        } else {
            TreeNode treeRoot = new TreeNode();
            treeRoot.children[0] = array[0];
            treeRoot.children[1] = array[1];
            updateTree(treeRoot);
            root = treeRoot;
        }
    }
    private Node[] insert(int key, Node n) {
        countInsertIterations++;
        Node array[] = new Node[2];

        Node catchArray[] = new Node[2];

        TreeNode t = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }

        if (root == null && !first) {
            first = true;
            TreeNode newNode = new TreeNode();
            t = newNode;
            t.children[0] = insert(key, t.children[0])[0];
            updateTree(t);

            array[0] = t;
            array[1] = null;

        }
        else if (t != null && !(t.children[0] instanceof LeafNode)) {
            if (key < t.keys[0]) {
                catchArray = insert(key, t.children[0]);
                t.children[0] = catchArray[0];
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = t.children[1];
                        t.children[1] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[1] = catchArray[1];
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
            else if (key >= t.keys[0] && (t.children[2] == null || key < t.keys[1])) {

                catchArray = insert(key, t.children[1]);

                t.children[1] = catchArray[0];

                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
            else if (key >= t.keys[1]) {
                catchArray = insert(key, t.children[2]);
                t.children[2] = catchArray[0];
                if (split) {
                    if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[0];
                        newNode.children[1] = catchArray[1];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
        }
        else if (t != null && t.children[0] instanceof LeafNode) {
            LeafNode l1 = null, l2 = null, l3 = null;
            if (t.children[0] != null && t.children[0] instanceof LeafNode) {
                l1 = (LeafNode) t.children[0];
            }
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            if (t.degree <= 2) {
                if (t.degree == 1 && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = leaf;
                } else if (t.degree == 1 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key < l2.key && key > l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = l2;
                    t.children[1] = leaf;
                } else if (t.degree == 2) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = leaf;
                }
                updateTree(t);
                array[0] = t;
                array[1] = null;
            }
            else if (t.degree > 2) {
                split = true;
                if (key < l1.key) {
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[0] = leaf;
                    t.children[1] = l1;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l1.key && key < l2.key) {
                    LeafNode leaf = new LeafNode(key);
                    TreeNode newNode = new TreeNode();
                    t.children[1] = leaf;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l2.key && key < l3.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = leaf;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key >= l3.key) {
                    LeafNode leaf = new LeafNode(key);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = l3;
                    newNode.children[1] = leaf;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                }
            }
            successfulInsertion = true;
        } else if (n == null) {
            successfulInsertion = true;
            array[0] = new LeafNode(key);
            array[1] = null;
            return array;
        }
        return array;
    }
    private Node remove(int key, Node n) {
        countRemoveIterations++;
        TreeNode tree = null;
        if (n instanceof TreeNode) {
            tree = (TreeNode) n;
        }
        if (n == null) {
            return null;
        }
        if (tree != null && tree.children[0] instanceof TreeNode) {
            if (key < tree.keys[0]) {
                tree.children[0] = remove(key, tree.children[0]);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) tree.children[0];
                    TreeNode rightChild = (TreeNode) tree.children[1];
                    if (rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        tree.children[0] = rightChild;
                        tree.children[1] = tree.children[2];
                        tree.children[2] = null;
                        if (tree.degree == 2) {
                            singleNodeUnderflow = true;
                            tree = (TreeNode) tree.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    }
                    else if (rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = tree.children[0];
                        newNode.children[1] = rightChild.children[0];
                        tree.children[0] = newNode;
                        updateTree(newNode);
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                }
                else if (underflow) {
                    underflow = false;
                    TreeNode child = (TreeNode) tree.children[0];
                    TreeNode rightChild = (TreeNode) tree.children[1];
                    if (rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    }
                    else if (rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        tree.children[0] = rightChild;
                        if (tree.degree == 3) {
                            tree.children[1] = tree.children[2];
                            tree.children[2] = null;
                        }
                        else {
                            Node ref = tree.children[0];
                            tree = (TreeNode) ref;
                            singleNodeUnderflow = true;
                        }
                    }
                }
                updateTree(tree);
            }
            else if (key >= tree.keys[0] && (tree.children[2] == null || key < tree.keys[1])) {
                tree.children[1] = remove(key, tree.children[1]);
                if (singleNodeUnderflow) {
                    TreeNode leftChild = (TreeNode) tree.children[0];
                    TreeNode child = (TreeNode) tree.children[1];
                    TreeNode rightChild = (TreeNode) tree.children[2];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        tree.children[1] = rightChild;
                        tree.children[2] = null;
                        updateTree(leftChild);
                        if (tree.degree == 2) {
                            singleNodeUnderflow = true;
                            tree = (TreeNode) tree.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    }
                    else if (rightChild != null && rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        tree.children[1] = rightChild;
                        tree.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                    else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = child;
                        tree.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                        singleNodeUnderflow = false;
                    }
                    else if (rightChild != null && rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = child;
                        newNode.children[1] = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        tree.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                }
                else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) tree.children[0];
                    TreeNode child = (TreeNode) tree.children[1];
                    TreeNode rightChild = (TreeNode) tree.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    }
                    else if (rightChild != null && rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    }
                    else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        tree.children[1] = null;
                        if (tree.degree == 3) {
                            tree.children[1] = tree.children[2];
                            tree.children[2] = null;
                        }

                        else {
                            singleNodeUnderflow = true;
                            tree = (TreeNode) tree.children[0];
                        }
                    }
                    else if (rightChild != null && rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        tree.children[1] = rightChild;
                        tree.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                }
                updateTree(tree);
            }
            else if (key >= tree.keys[1]) {
                tree.children[2] = remove(key, tree.children[2]);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) tree.children[2];
                    TreeNode leftChild = (TreeNode) tree.children[1];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        tree.children[2] = null;
                        updateTree(leftChild);
                    }
                    else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = tree.children[2];
                        tree.children[2] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                    }

                    singleNodeUnderflow = false;
                }
                else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) tree.children[1];
                    TreeNode child = (TreeNode) tree.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    }
                    else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        tree.children[2] = null;
                    }
                }
                updateTree(tree);
            }
        }
        else if (tree != null && tree.children[0] instanceof LeafNode) {
            LeafNode l1 = null, l2 = null, l3 = null;
            if (tree.children[0] != null && tree.children[0] instanceof LeafNode) {
                l1 = (LeafNode) tree.children[0];
            }
            if (tree.children[1] != null && tree.children[1] instanceof LeafNode) {
                l2 = (LeafNode) tree.children[1];
            }
            if (tree.children[2] != null && tree.children[2] instanceof LeafNode) {
                l3 = (LeafNode) tree.children[2];
            }
            if (tree.degree == 3) {
                if (key == l1.key) {
                    tree.children[0] = l2;
                    tree.children[1] = l3;
                    tree.children[2] = null;
                } else if (key == l2.key) {
                    tree.children[1] = l3;
                    tree.children[2] = null;
                } else if (key == l3.key) {
                    tree.children[2] = null;
                }
                updateTree(tree);
            }
            else if (tree.degree == 2) {

                underflow = true;


                if (l1.key == key) {
                    tree.children[0] = l2;
                    tree.children[1] = null;
                } else if (l2.key == key) {
                    tree.children[1] = null;
                }
            }
            else if (tree.degree == 1) {

                if (l1.key == key) {
                    tree.children[0] = null;
                }
            }
            successfulDeletion = true;
        }
        return tree;
    }
    private void updateTree(TreeNode tree) {
        if (tree != null) {
            if (tree.children[2] != null && tree.children[1] != null && tree.children[0] != null) {
                tree.degree = 3;
                tree.keys[0] = getValueForKey(tree, Nodes.LEFT);
                tree.keys[1] = getValueForKey(tree, Nodes.RIGHT);
            } else if (tree.children[1] != null && tree.children[0] != null) {
                tree.degree = 2;
                tree.keys[0] = getValueForKey(tree, Nodes.LEFT);
                tree.keys[1] = 0;
            } else if (tree.children[0] != null) {
                tree.degree = 1;
                tree.keys[1] = tree.keys[0] = 0;
            }
        }
    }
    private int getValueForKey(Node n, Nodes whichVal) {
        int key = -1;
        TreeNode tree = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            tree = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (l != null) {
            key = l.key;
        }
        if (tree != null) {
            if (null != whichVal) {
                switch (whichVal) {
                    case LEFT:
                        key = getValueForKey(tree.children[1], Nodes.DUMMY);
                        break;
                    case RIGHT:
                        key = getValueForKey(tree.children[2], Nodes.DUMMY);
                        break;
                    case DUMMY:
                        key = getValueForKey(tree.children[0], Nodes.DUMMY);
                        break;
                    default:
                        break;
                }
            }
        }
        return key;
    }
    private boolean search(int key, Node n) {
        countRemoveIterations++;
        countInsertIterations++;
        countSearchIterations++;
        boolean found = false;
        TreeNode tree = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            tree = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (tree != null) {
            if (tree.degree == 1) {
                found = search(key, tree.children[0]);
            }
            else if (tree.degree == 2 && key < tree.keys[0]) {
                found = search(key, tree.children[0]);
            }
            else if (tree.degree == 2 && key >= tree.keys[0]) {
                found = search(key, tree.children[1]);
            }
            else if (tree.degree == 3 && key < tree.keys[0]) {
                found = search(key, tree.children[0]);
            }
            else if (tree.degree == 3 && key >= tree.keys[0] && key < tree.keys[1]) {
                found = search(key, tree.children[1]);
            }
            else if (tree.degree == 3 && key >= tree.keys[1]) {
                found = search(key, tree.children[2]);
            }
        }
        else if (l != null && key == l.key) {
            return true;
        }
        return found;
    }
    private void keyOrderList(Node n) {
        TreeNode tree = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            tree = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (tree != null) {
            if (tree.children[0] != null) {
                keyOrderList(tree.children[0]);
            }
            if (tree.children[1] != null) {
                keyOrderList(tree.children[1]);
            }
            if (tree.children[2] != null) {
                keyOrderList(tree.children[2]);
            }
        }
        else if (l != null) {
            System.out.print(l.key + " ");
        }
    }
    private void bfsList(Node n) {
        Queue<Node> queueOne = new LinkedList<>();
        Queue<Node> queueTwo = new LinkedList<>();
        if (n == null) {
            return;
        }
        queueOne.add(n);
        Node first = null;
        TreeNode tree = null;
        while (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
            while (!queueOne.isEmpty()) {
                first = queueOne.poll();
                if (first instanceof TreeNode) {
                    tree = (TreeNode) first;
                    tree.print();
                }
                if (tree.children[0] != null && !(tree.children[0] instanceof LeafNode)) {
                    queueTwo.add(tree.children[0]);
                }
                if (tree.children[1] != null && !(tree.children[1] instanceof LeafNode)) {
                    queueTwo.add(tree.children[1]);
                }
                if (tree.children[2] != null && !(tree.children[2] instanceof LeafNode)) {
                    queueTwo.add(tree.children[2]);
                }
            }
            if (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
                System.out.println();
            }
            while (!queueTwo.isEmpty()) {
                first = queueTwo.poll();
                if (first instanceof TreeNode) {
                    tree = (TreeNode) first;
                    tree.print();
                }
                if (tree.children[0] != null && !(tree.children[0] instanceof LeafNode)) {
                    queueOne.add(tree.children[0]);
                }
                if (tree.children[1] != null && !(tree.children[1] instanceof LeafNode)) {
                    queueOne.add(tree.children[1]);
                }
                if (tree.children[2] != null && !(tree.children[2] instanceof LeafNode)) {
                    queueOne.add(tree.children[2]);
                }
            }
            if (!queueOne.isEmpty() || !queueTwo.isEmpty()) {
                System.out.println();
            }
        }
        System.out.println();
        keyOrderList(root);
        System.out.println();
    }
    private int height(Node n) {
        TreeNode tree = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            tree = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (tree != null) {
            return 1 + height(tree.children[0]);
        }
        return 0;
    }
    public boolean insert(int key) {
        countInsertIterations = 0;
        boolean insert = false;
        split = false;
        if (!search(key)) {
            insertKey(key);
        }
        if (successfulInsertion) {
            size++;
            insert = successfulInsertion;
            successfulInsertion = false;
        }
        return insert;
    }
    public boolean search(int key) {
        countSearchIterations = 0;
        return search(key, root);
    }
    public boolean remove(int key) {
        countRemoveIterations = 0;
        boolean delete = false;
        singleNodeUnderflow = false;
        underflow = false;
        if (search(key)) {
            root = (TreeNode) remove(key, root);
            if (root.degree == 1 && root.children[0] instanceof TreeNode) {
                root = (TreeNode) root.children[0];
            }
        }
        if (successfulDeletion) {
            size--;
            delete = successfulDeletion;
            successfulDeletion = false;
        }
        return delete;
    }
    public void keyOrderList() {
        System.out.println("Keys");
        keyOrderList(root);
        System.out.println();
    }
    public void bfsList() {
        System.out.println("Tree");
        bfsList(root);
    }
    public int numberOfNodes() {
        return size;
    }
    public int height() {
        return height(root);
    }
    public long getCountInsertIterations() {
        return countInsertIterations;
    }

    public long getCountRemoveIterations() {
        return countRemoveIterations;
    }

    public long getCountSearchIterations() {
        return countSearchIterations;
    }
}