package com.rise42.scheme;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rise42.Main;
import com.rise42.module.Module;
import com.rise42.module.StarModule;
import com.rise42.node.Node;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by rise42 on 16/12/18.
 */
public class GridScheme extends Scheme {

    private StarModule sampleModule;
    private List<StarModule> includedModules;

    private int[][] secondMatrix;

    public GridScheme(int moduleCount, JsonObject obj) {
        includedModules = new ArrayList<>();

        for (int i = 0; i < moduleCount; i++) {
            addModule(new StarModule(i, obj));
        }

        secondMatrix = includedModules.get(0).buildSecondMatrix();
    }

    public void addModule(StarModule module) {
        includedModules.add(module);
        switch (includedModules.size()) {
            case 1:
                this.sampleModule = module;
            case 2:
            case 3:
                includedModules.get(0).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(0).getHub());
                break;
            case 4:
                includedModules.get(1).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(1).getHub());
                includedModules.get(2).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(2).getHub());
                break;
            case 5:
                includedModules.get(1).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(1).getHub());
                break;
            case 6:
                includedModules.get(3).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(3).getHub());
                includedModules.get(4).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(4).getHub());
                break;
            case 7:
                includedModules.get(2).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(2).getHub());
                break;
            case 8:
                includedModules.get(3).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(3).getHub());
                includedModules.get(6).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(6).getHub());
                break;
            case 9:
                includedModules.get(5).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(5).getHub());
                includedModules.get(7).getHub().addConnection(module.getHub());
                module.getHub().addConnection(includedModules.get(7).getHub());
                break;
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

    private int findDistance(Node a, Node b){
        int aToHub = secondMatrix[sampleModule.getHub().getIndex()][a.getIndex()];
        int bToHub = secondMatrix[sampleModule.getHub().getIndex()][b.getIndex()];

        return aToHub + bToHub + buildInterModuleMatrix()[a.getModuleIndex()][b.getModuleIndex()];
    }

    private int[][] buildInterModuleMatrix() {
        StringBuilder builder = new StringBuilder();
        try {
            Files.lines(
                    Paths.get(
                            "res/GridSample" +
                                    ".json", ""
                    )
            ).forEach(builder::append);
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject file = new JsonParser().parse(builder.toString()).getAsJsonObject();
        JsonObject module = file.get("module").getAsJsonObject();

        return new Module(0, module).buildSecondMatrix();
    }
}
