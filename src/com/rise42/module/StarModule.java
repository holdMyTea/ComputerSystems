package com.rise42.module;

import com.google.gson.JsonObject;
import com.rise42.node.Node;

/**
 * Created by rise42 on 08/04/17.
 */
public class StarModule extends Module {

    private Node hub;

    public StarModule(int moduleIndex, JsonObject moduleJson) {
        super(moduleIndex, moduleJson);

        hub = includedNodes.get(moduleJson.get("hub").getAsInt());
    }

    public Node getHub() {
        return hub;
    }
}
