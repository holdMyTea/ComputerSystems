package com.rise42.node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rise42 on 19/03/17.
 */
public class Node {

    private int moduleIndex;

    private int index;
    private List<Node> connectedNodes;

    public Node(int moduleIndex, int index) {
        this.moduleIndex = moduleIndex;
        this.index = index;
        this.connectedNodes = new ArrayList<>();
    }

    public void addConnection(Node node) {
        this.connectedNodes.add(node);
    }

    public boolean isDirectlyConnected(Node another) {
        return connectedNodes.contains(another);
    }

    public boolean isDirectlyConnected(int index, int moduleIndex) {
        for (Node node : connectedNodes) {
            if (node.getIndex() == index && node.getModuleIndex() == moduleIndex)
                return true;
        }
        return false;
    }

    public void deleteConnectionsToAnotherModule() {
        for (int i = 0; i < connectedNodes.size(); i++)
            if (connectedNodes.get(i).getModuleIndex() != moduleIndex)
                connectedNodes.remove(i);
    }

    public void deleteConnectionsToModule(int module){
        for (int i = 0; i < connectedNodes.size(); i++) {
            int anotherNodesIndex = connectedNodes.get(i).getModuleIndex();
            if (anotherNodesIndex == module && anotherNodesIndex != moduleIndex)
                connectedNodes.remove(i);
        }
    }

    public boolean isConnectedToModule(int anotherModule){
        for (Node node: connectedNodes){
            if(node.getModuleIndex() == anotherModule){
                return true;
            }
        }
        return false;
    }

    /*public void deleteConnectionsToModuleGreaterThan(int greatModule) {
        for (int i = 0; i < connectedNodes.size(); i++) {
            int anotherNodesIndex = connectedNodes.get(i).getModuleIndex();
            if (anotherNodesIndex > greatModule && anotherNodesIndex != moduleIndex)
                connectedNodes.remove(i);
        }
    }

    public void deleteConnectionsToModuleLesserThan(int smallModule) {
        for (int i = 0; i < connectedNodes.size(); i++) {
            int anotherNodesIndex = connectedNodes.get(i).getModuleIndex();
            if (anotherNodesIndex < smallModule && anotherNodesIndex != moduleIndex)
                connectedNodes.remove(i);
        }
    }*/

    public int getModuleIndex() {
        return moduleIndex;
    }

    public int getIndex() {
        return index;
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public void setConnectedNodes(Node... nodes) {
        this.connectedNodes = Arrays.asList(nodes);
    }

    public void setConnectedNodes(List<Node> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    @Override
    public String toString() {
        String s = "In module " + moduleIndex + " node " + index
                + " is directly connected to: ";

        for (Node node : connectedNodes) {
            s = s + node.getModuleIndex() + ":" + node.getIndex() + " ";
        }

        return s;
    }

    public static boolean checkDirectConnectionInList(Node target, List<Node> list) {
        for (Node n : list) {
            if (n.isDirectlyConnected(target)) {
                return true;
            }
        }
        return false;
    }
}
