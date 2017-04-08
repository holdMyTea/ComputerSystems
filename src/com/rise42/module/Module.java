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

    private int[][] firstMatrix, secondMatrix;

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
                    (id) -> includedNodes.get(index).addConnection(includedNodes.get(id.getAsInt()))
            );
        }

        JsonArray inputs = moduleJson.get("input").getAsJsonArray();
        inputNodes = new ArrayList<>(inputs.size());
        for (int i = 0; i < inputs.size(); i++) {
            inputNodes.add(i,
                    includedNodes.get(
                            inputs.get(i).getAsInt()
                    )
            );
        }

        JsonArray output = moduleJson.get("output").getAsJsonArray();
        outputNodes = new ArrayList<>(inputs.size());
        for (int i = 0; i < output.size(); i++) {
            outputNodes.add(i,
                    includedNodes.get(
                            output.get(i).getAsInt()
                    )
            );
        }

    }

    public int[][] buildFirstMatrix() {
        if(firstMatrix != null)
            return firstMatrix;

        int size = includedNodes.size();
        firstMatrix = new int[size][size];

        for (int i = 1; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    firstMatrix[i][j] = 0;
                    break;
                }
                firstMatrix[i][j] = includedNodes.get(i).isDirectlyConnected(
                        includedNodes.get(j)
                ) ? 1 : 0;
                firstMatrix[j][i] = firstMatrix[i][j];
            }
        }

        return firstMatrix;
    }

    public int[][] buildSecondMatrix() {
        if(secondMatrix != null)
            return secondMatrix;

        int size = includedNodes.size();
        secondMatrix = new int[size][size];

        Node currentNode, targetNode;

        for (int i = 1; i < size; i++) {
            currentNode = includedNodes.get(i);

            for (int j = 0; j < size; j++) {
                targetNode = includedNodes.get(j);

                if (i == j){
                    secondMatrix[i][j] = 0;
                    break;
                }

                else if (currentNode.isDirectlyConnected(targetNode)){
                    secondMatrix[i][j] = 1;
                }

                else {

                    if((i == 7 || i == 4)&&(j == 7 || j == 4)){
                        //System.out.println("BANG-BANG-BANG: i="+i+", j="+j);
                    }

                    boolean keepUp = true;

                    int result = 2;
                    List<Node> checkedNodes = currentNode.getConnectedNodes();

                    while(keepUp){

                        for(Node node: checkedNodes){
                            if(node.isDirectlyConnected(targetNode)) {
                                secondMatrix[i][j] = result;
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

                secondMatrix[j][i] = secondMatrix[i][j];
            }

        }

        return secondMatrix;
    }

    public boolean isConnectedToModule(int anotherModule){
        for(Node input: inputNodes)
            if(input.isConnectedToModule(anotherModule))
                return true;

        for(Node output: outputNodes)
            if(output.isConnectedToModule(anotherModule))
                return true;

        return false;
    }

    public boolean areInputsConnectedToModule(int anotherModule){
        for(Node input: inputNodes)
            if(input.isConnectedToModule(anotherModule))
                return true;

        return false;
    }

    public boolean areOutputsConnectedToModule(int anotherModule){
        for(Node output: outputNodes)
            if(output.isConnectedToModule(anotherModule))
                return true;

        return false;
    }

    public int getMaxDistanceInModule() {
        int max = -1;
        for(int[] row: buildFirstMatrix())
            for(int i: row)
                if(i > max)
                    max = i;
        return max;
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

    public int getModuleIndex() {
        return moduleIndex;
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
