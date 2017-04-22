package com.rise42.module;

import com.google.gson.JsonObject;
import com.rise42.node.Node;

/**
 * Created by rise42 on 08/04/17.
 */
public class TreeModule extends Module {

    Node parent, leftChild, rightChild;

    public TreeModule(int moduleIndex, JsonObject moduleJson) {
        super(moduleIndex, moduleJson);

        parent = includedNodes.get(moduleJson.get("parent").getAsInt());
        leftChild = includedNodes.get(moduleJson.get("left").getAsInt());
        rightChild = includedNodes.get(moduleJson.get("right").getAsInt());
    }

    public Node getParent() {
        return parent;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }
}
