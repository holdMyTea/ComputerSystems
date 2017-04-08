package com.rise42.scheme;

import com.google.gson.JsonObject;
import com.rise42.module.Module;
import com.rise42.module.StarModule;
import com.rise42.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rise42 on 08/04/17.
 */
public class StarScheme extends Scheme {

    private StarModule hubModule;
    private List<StarModule> includedModules;

    private int[][] secondMatrix;

    public StarScheme(int moduleCount, JsonObject obj) {
        includedModules = new ArrayList<>();

        for (int i = 0; i < moduleCount; i++) {
            addModule(new StarModule(i, obj));
        }

        secondMatrix = includedModules.get(0).buildSecondMatrix();
    }

    public void addModule(StarModule module) {
        includedModules.add(module);
        if(includedModules.size() == 1){
            hubModule = includedModules.get(0);
        } else {
            hubModule.getHub().addConnection(module.getHub());
            module.getHub().addConnection(hubModule.getHub());
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

        Node iNode;

        for (int i = 1; i < totalSize; i++) {

            iNode = includedModules.get(i / nodeCount).getIncludedNodes().get(i % nodeCount);

            for (int j = 0; j < totalSize; j++) {

                if (m[i][j] == 0)
                    break;

                if (m[i][j] != -1)
                    continue;

                m[i][j] = findDistance(
                        iNode,
                        includedModules.get(j / nodeCount).getIncludedNodes().get(j % nodeCount)
                );
                m[j][i] = m[i][j];
            }

        }

        return m;
    }

    public int findDistance(Node a, Node b){
        int aToHub = secondMatrix[hubModule.getHub().getIndex()][a.getIndex()];
        int bToHub = secondMatrix[hubModule.getHub().getIndex()][b.getIndex()];

        if(includedModules.get(a.getModuleIndex()) == hubModule || includedModules.get(b.getModuleIndex()) == hubModule){
            return aToHub + bToHub + 1;
        } else return aToHub + bToHub + 2;
    }
}
