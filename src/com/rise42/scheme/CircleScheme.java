package com.rise42.scheme;

import com.rise42.module.Module;
import com.rise42.node.Node;

/**
 * Created by rise42 on 24/03/17.
 */
public class CircleScheme extends Scheme {


    @Override
    public void addModule(Module module) {

        Module last, first;

        if(includedModules.size() == 0){
            first = module;
            last = module;
        } else if(includedModules.size() == 1){
            first = includedModules.get(0);
            last = first;
        } else{
            last = includedModules.get(includedModules.size()-1);
            first = includedModules.get(0);
        }

        last.getOutputNodes().forEach(
                (node) -> node.deleteConnectionsToModuleLesserThan(1)
        );

        for (int i = 0; i < module.getInputNodes().size(); i++) {
            module.getIncludedNodes().forEach(
                    Node::deleteConnectionsToAnotherModule
            );
            module.getInputNodes().get(i).addConnection(last.getOutputNodes().get(i));
            last.getOutputNodes().get(i).addConnection(module.getInputNodes().get(i));
        }

        first.getInputNodes().forEach(
                (node) -> node.deleteConnectionsToModuleGreaterThan(1)
        );

        for (int i = 0; i < module.getInputNodes().size(); i++) {
            module.getOutputNodes().get(i).addConnection(first.getInputNodes().get(i));
            first.getInputNodes().get(i).addConnection(module.getOutputNodes().get(i));
        }

        includedModules.add(module);
    }

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
}
