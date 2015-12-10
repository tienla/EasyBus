package com.easybus.gui.Object;

public class TreeNode {

    private Nodes currentNode;
    private int numberOfChange;
    private double weight;
    private TreeNode parent;
    private double previousDistance;
    
    public TreeNode getParent() {
        return parent;
    }

    public double getPreviousDistance() {
        return previousDistance;
    }

    public void setPreviousDistance(double previousDistance) {
        this.previousDistance = previousDistance;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode(Nodes node, int numberOfChange, double previousDistance) {
        this.currentNode = node;
        this.numberOfChange = numberOfChange;
        this.previousDistance = previousDistance;
        weight = numberOfChange*20 + previousDistance;
        
    }

    public TreeNode(Nodes currentNode, TreeNode parent) {
        this.currentNode = currentNode;
        this.parent = parent;
    }
    
    public Nodes getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Nodes currentNode){
        this.currentNode = currentNode;
    }

    public int getNumberOfChange() {
        return numberOfChange;
    }

    public void setNumberOfChange(int numberOfChange) {
        this.numberOfChange = numberOfChange;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
}