package com.rise42.scheme;

import com.google.gson.JsonObject;
import com.rise42.module.CircleModule;
import com.rise42.node.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rise42 on 24/03/17.
 */
public class CircleScheme extends Scheme {

    // left -- outputs, right -- inputs
    private int[][] interModuleMatrix;

    private List<CircleModule> includedModules;

    public CircleScheme(int moduleCount, JsonObject obj) {
        includedModules = new ArrayList<>();

        for (int i = 0; i < moduleCount; i++) {
            addModule(new CircleModule(i, obj));
        }

        buildIntermoduleMatrix();
    }


    public void addModule(CircleModule module) {

        if (includedModules.size() == 0) {
            includedModules.add(module);
        } else {
            CircleModule last = includedModules.get(includedModules.size() - 1);
            CircleModule first = includedModules.get(0);


            last.getOutputNodes().forEach(
                    (node) -> node.deleteConnectionsToModule(0)
            );

            first.getInputNodes().forEach(
                    (node) -> node.deleteConnectionsToModule(last.getModuleIndex())
            );

            for (int i = 0; i < module.getInputNodes().size(); i++) {
                module.getInputNodes().get(i).addConnection(last.getOutputNodes().get(i));
                last.getOutputNodes().get(i).addConnection(module.getInputNodes().get(i));
            }

            for (int i = 0; i < module.getInputNodes().size(); i++) {
                module.getOutputNodes().get(i).addConnection(first.getInputNodes().get(i));
                first.getInputNodes().get(i).addConnection(module.getOutputNodes().get(i));
            }

            includedModules.add(module);
        }
    }

    @Override
    public int[][] buildSecondMatrix() {
        int nodeCount = includedModules.get(0).getIncludedNodes().size();

        int totalSize = nodeCount * includedModules.size();
        int[][] m = new int[totalSize][totalSize];

        for (int i = 0; i < totalSize; i++)
            for (int j = 0; j < totalSize; j++)
                m[i][j] = -1;

        int[][] inner = includedModules.get(0).buildSecondMatrix();

        for (int eight = 0; eight < totalSize; eight += nodeCount)
            for (int i = 0; i < nodeCount; i++)
                for (int j = 0; j < nodeCount; j++)
                    m[eight + i][eight + j] = inner[i][j];

        Node iNode, jNode;

        for (int i = 1; i < totalSize; i++) {

            iNode = includedModules.get(i / nodeCount).getIncludedNodes().get(i % nodeCount);

            for (int j = 0; j < totalSize; j++) {

                if (m[i][j] == 0)
                    break;

                if (m[i][j] != -1)
                    continue;

                jNode = includedModules.get(j / nodeCount).getIncludedNodes().get(j % nodeCount);

                m[i][j] = findDistance(jNode, iNode);
                m[j][i] = m[i][j];
            }

        }

        return m;
    }

    private int findDistance(Node low, Node high) {
        CircleModule sampleModule = includedModules.get(1);
        int[][] secondMatrix = sampleModule.buildSecondMatrix();

        boolean reversed = false;
        int moduleDistance = high.getModuleIndex() - low.getModuleIndex();
        if (includedModules.size() - high.getModuleIndex() + low.getModuleIndex() < moduleDistance) {
            moduleDistance = includedModules.size() - high.getModuleIndex() + low.getModuleIndex();
            reversed = true;
        }

        int minDistance = Integer.MAX_VALUE;

        if (reversed) {
            HashMap<Node, Integer> lowInput = new HashMap<>(sampleModule.getOutputNodes().size());
            for (Node input : sampleModule.getInputNodes()) {
                lowInput.put(input, secondMatrix[low.getIndex()][input.getIndex()]);
            }

            HashMap<Node, Integer> highOutput = new HashMap<>(sampleModule.getOutputNodes().size());
            for (Node output : sampleModule.getOutputNodes())
                highOutput.put(output, secondMatrix[high.getIndex()][output.getIndex()]);

            for (int i = 0; i < sampleModule.getOutputNodes().size(); i++) {
                for (int j = 0; j < sampleModule.getInputNodes().size(); j++) {
                    int d;
                    d = highOutput.get(sampleModule.getOutputNodes().get(j));
                    d += interModuleMatrix[i][j] * (moduleDistance);
                    d += secondMatrix[sampleModule.getOutputNodes().get(i).getIndex()][sampleModule.getInputNodes().get(j).getIndex()] * (moduleDistance - 1);
                    d += lowInput.get(sampleModule.getInputNodes().get(i));

                    if (d < minDistance)
                        minDistance = d;
                }
            }
        } else {

            // distance from low Node to its module outputs
            HashMap<Node, Integer> lowOutput = new HashMap<>(sampleModule.getOutputNodes().size());
            for (Node output : sampleModule.getOutputNodes())
                lowOutput.put(output, secondMatrix[low.getIndex()][output.getIndex()]);

            // distance from high Node to its nodule inputs
            HashMap<Node, Integer> highInput = new HashMap<>(sampleModule.getInputNodes().size());
            for (Node input : sampleModule.getInputNodes())
                highInput.put(input, secondMatrix[high.getIndex()][input.getIndex()]);

            for (int i = 0; i < sampleModule.getOutputNodes().size(); i++) {
                for (int j = 0; j < sampleModule.getInputNodes().size(); j++) {
                    int d;
                    d = lowOutput.get(sampleModule.getOutputNodes().get(i));
                    d += interModuleMatrix[i][j] * (moduleDistance);
                    d += secondMatrix[sampleModule.getOutputNodes().get(i).getIndex()][sampleModule.getInputNodes().get(j).getIndex()] * (moduleDistance - 1);
                    d += highInput.get(sampleModule.getInputNodes().get(j));


                    if (d < minDistance)
                        minDistance = d;
                }
            }
        }


        return minDistance;
    }

    private void buildIntermoduleMatrix() {
        CircleModule sampleModule = includedModules.get(0);

        interModuleMatrix = new int[sampleModule.getOutputNodes().size()][sampleModule.getInputNodes().size()];

        int[][] secondMatrix = sampleModule.buildSecondMatrix();

        CircleModule nextSample = includedModules.get(1);

        for (int i = 0; i < sampleModule.getOutputNodes().size(); i++)
            for (int j = 0; j < nextSample.getInputNodes().size(); j++) {
                if (sampleModule.getOutputNodes().get(i).isDirectlyConnected(nextSample.getInputNodes().get(j))) {
                    interModuleMatrix[i][j] = 1;
                } else interModuleMatrix[i][j] = -1;
            }


        for (int i = 0; i < sampleModule.getOutputNodes().size(); i++)
            for (int j = 0; j < nextSample.getInputNodes().size(); j++)
                if (interModuleMatrix[i][j] == -1) {
                    int min = Integer.MAX_VALUE;
                    int value;
                    for (int k = 0; k < sampleModule.getOutputNodes().size(); k++) {
                        value = secondMatrix[sampleModule.getOutputNodes().get(k).getIndex()][nextSample.getInputNodes().get(j).getIndex()];
                        if (value < min) min = value;
                    }

                    for (int k = 0; k < sampleModule.getInputNodes().size(); k++) {
                        value = secondMatrix[sampleModule.getOutputNodes().get(i).getIndex()][nextSample.getInputNodes().get(k).getIndex()];
                        if (value < min) min = value;
                    }

                    interModuleMatrix[i][j] = min + 1;
                }

    }

}
