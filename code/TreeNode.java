package main;
public class TreeNode extends Node {
    int keys[];
    Node children[];
    int degree;

    public TreeNode() {
        keys = new int[2];
        children = new Node[3];
        degree = 0;
    }
    public void print() {
        if (degree == 1) {
            System.out.print("(-,-)");
        } else if (degree == 2) {
            System.out.print("(" + keys[0] + ",-) ");
        } else {
            System.out.print("(" + keys[0] + "," + keys[1] + ") ");
        }
    }
}