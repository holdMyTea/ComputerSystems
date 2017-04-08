package com.rise42.scheme;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rise42.module.Module;
import com.rise42.node.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rise42 on 24/03/17.
 */
public abstract class Scheme {

    protected List<Module> includedModules;

    public Scheme(){
        includedModules = new ArrayList<Module>();
    }

    public Scheme(int moduleCount, JsonObject obj){
        includedModules = new ArrayList<Module>(moduleCount);

        for (int i = 0; i < moduleCount; i++) {
            addModule(new Module(i,obj));
        }

    }

    public abstract void addModule(Module module);

    public int[][] buildFirstMatrix(){

        int nodeCount = includedModules.get(0).getIncludedNodes().size();

        int totalSize = nodeCount * includedModules.size();
        int[][] m = new int[totalSize][totalSize];

        for (int i = 1; i < totalSize; i++) {
            for (int j = 0; j < totalSize; j++) {
                if (i == j) {
                    m[i][j] = 0;
                    break;
                }

                m[i][j] = includedModules.get(i/nodeCount).getIncludedNodes().get(i%nodeCount).isDirectlyConnected(
                        includedModules.get(j/nodeCount).getIncludedNodes().get(j%nodeCount)
                ) ? 1 : 0;

                m[j][i] = m[i][j];
            }
        }

        return m;
    }

    public abstract int[][] buildSecondMatrix();
}
