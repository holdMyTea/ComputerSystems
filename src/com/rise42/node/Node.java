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

    public void deleteAllConnections() {
        connectedNodes.clear();
    }

    public void deleteConnectionsToAnotherModule() {
        for (int i = 0; i < connectedNodes.size(); i++)
            if (connectedNodes.get(i).getModuleIndex() != moduleIndex)
                connectedNodes.remove(i);
    }

    public void deleteConnectionsToModuleGreaterThan(int greatModule) {
        for (int i = 0; i < connectedNodes.size(); i++)
            if (connectedNodes.get(i).getModuleIndex() > greatModule)
                connectedNodes.remove(i);
    }

    public void deleteConnectionsToModuleLesserThan(int smallModule) {
        for (int i = 0; i < connectedNodes.size(); i++)
            if (connectedNodes.get(i).getModuleIndex() < smallModule)
                connectedNodes.remove(i);
    }

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
        String s = "Node " + index + " in module " + moduleIndex
                + " is directly connected to: ";

        for (Node node : connectedNodes) {
            s = s + node.getIndex() + " ";
        }

        return s + "\n\r";
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
