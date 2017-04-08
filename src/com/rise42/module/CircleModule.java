package com.rise42.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rise42.node.Node;
import com.rise42.scheme.CircleScheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rise42 on 08/04/17.
 */
public class CircleModule extends Module {

    private List<Node> inputNodes;
    private List<Node> outputNodes;

    public CircleModule(int moduleIndex, JsonObject moduleJson){
        super(moduleIndex, moduleJson);

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

    public List<Node> getInputNodes() {
        return inputNodes;
    }

    public List<Node> getOutputNodes() {
        return outputNodes;
    }
}
