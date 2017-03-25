package com.rise42.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rise42.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rise42 on 19/03/17.
 */

public class Module {

    private int moduleIndex;

    private List<Node> includedNodes;

    private List<Node> inputNodes;
    private List<Node> outputNodes;

    public Module(int moduleIndex, JsonObject moduleJson) {

        int nodeCount = moduleJson.get("nodeCount").getAsInt();

        this.moduleIndex = moduleIndex;
        this.includedNodes = new ArrayList<>(nodeCount);

        for (int i = 0; i < nodeCount; i++) {
            this.includedNodes.add(
                    new Node(this.moduleIndex, i)
            );
        }

        for (int i = 0; i < nodeCount; i++) {
            final int index = i;
            moduleJson.get(Integer.toString(i)).getAsJsonArray().forEach(
                    (id) -> {
                        includedNodes.get(index).addConnection(includedNodes.get(id.getAsInt()));
                    }
            );
        }

        JsonArray inputs = moduleJson.get("input").getAsJsonArray();
        inputNodes = new ArrayList<>(inputs.size());
        for (int i = 0; i < inputs.size(); i++) {
            inputNodes.add(
                    includedNodes.get(
                            inputs.get(i).getAsInt()
                    )
            );
        }

        JsonArray output = moduleJson.get("output").getAsJsonArray();
        outputNodes = new ArrayList<>(inputs.size());
        for (int i = 0; i < output.size(); i++) {
            outputNodes.add(
                    includedNodes.get(
                            output.get(i).getAsInt()
                    )
            );
        }

    }

    public int[][] buildFirstMatrix() {
        int size = includedNodes.size();
        int[][] m = new int[size][size];

        for (int i = 1; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    m[i][j] = 0;
                    break;
                }
                m[i][j] = includedNodes.get(i).isDirectlyConnected(
                        includedNodes.get(j)
                ) ? 1 : 0;
                m[j][i] = m[i][j];
            }
        }

        return m;
    }

    public int[][] buildSecondMatrix() {
        int size = includedNodes.size();
        int[][] matrix = new int[size][size];

        Node targetNode;

        for (int i = 1; i < size; i++) {

            for (int j = 0; j < size; j++) {

                targetNode = includedNodes.get(j);

                if (i == j){
                    matrix[i][j] = 0;
                    break;
                }

                else if (includedNodes.get(i).isDirectlyConnected(targetNode)){
                    matrix[i][j] = 1;
                }

                else {

                    boolean keepUp = true;

                    int result = 2;
                    List<Node> checkedNodes = includedNodes.get(i).getConnectedNodes();

                    while(keepUp){

                        for(Node node: checkedNodes){
                            if(node.isDirectlyConnected(targetNode)) {
                                matrix[i][j] = result;
                                keepUp = false;
                                break;
                            }
                        }

                        List<Node> buffer = new ArrayList<>();
                        for(Node node: checkedNodes){
                            buffer.addAll(node.getConnectedNodes());
                        }

                        checkedNodes = buffer;
                        result++;
                    }

                }

                matrix[j][i] = matrix[i][j];
            }

        }

        return matrix;
    }

    public List<Node> getIncludedNodes() {
        return includedNodes;
    }

    public List<Node> getInputNodes() {
        return inputNodes;
    }

    public List<Node> getOutputNodes() {
        return outputNodes;
    }

    @Override
    public String toString() {
        String s = "Module " + moduleIndex + " has following nodes:\n\r";
        for (Node node : includedNodes) {
            s = s + node;
        }
        return s;
    }

}
