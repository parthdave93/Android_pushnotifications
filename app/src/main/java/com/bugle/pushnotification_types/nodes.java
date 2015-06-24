package com.bugle.pushnotification_types;

import java.util.ArrayList;

/**
 * Created by parth on 31/5/15.
 */
public class nodes {
    String current_node_name;
    String current_node_type;
    String parent_node_name;
    String parent_node_type;
    ArrayList<nodes> sub_nodes;

    nodes(){
        this.sub_nodes = new ArrayList<>();
    }

    public String getCurrent_node_name() {
        return current_node_name;
    }

    public void setCurrent_node_name(String current_node_name) {
        this.current_node_name = current_node_name;
    }

    public String getCurrent_node_type() {
        return current_node_type;
    }

    public void setCurrent_node_type(String current_node_type) {
        this.current_node_type = current_node_type;
    }

    public ArrayList<nodes> getSub_nodes() {
        return sub_nodes;
    }

    public void setSub_nodes(ArrayList<nodes> sub_nodes) {
        this.sub_nodes = sub_nodes;
    }

    public String getParent_node_name() {
        return parent_node_name;
    }

    public void setParent_node_name(String parent_node_name) {
        this.parent_node_name = parent_node_name;
    }

    public String getParent_node_type() {
        return parent_node_type;
    }

    public void setParent_node_type(String parent_node_type) {
        this.parent_node_type = parent_node_type;
    }

    @Override
    public String toString() {
        return "nodes{" +
                "current_node_name='" + current_node_name + '\'' +
                ", current_node_type='" + current_node_type + '\'' +
                ", parent_node_name='" + parent_node_name + '\'' +
                ", parent_node_type='" + parent_node_type + '\'' +
                ", sub_nodes=" + sub_nodes +
                '}';
    }
}
