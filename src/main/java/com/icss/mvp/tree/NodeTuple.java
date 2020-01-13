package com.icss.mvp.tree;

import java.util.Map;

public class NodeTuple {
    private Node node;
    private Map<String, Node> map;

    public NodeTuple(Node node, Map<String, Node> map) {
        this.node = node;
        this.map = map;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Map<String, Node> getMap() {
        return map;
    }

    public void setMap(Map<String, Node> map) {
        this.map = map;
    }
}
