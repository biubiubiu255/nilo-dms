package com.nilo.dms.service.order.third;

import java.util.Map;


public interface TreeNode<T> {

    void add(TreeNode<T> child);

    TreeNode<T> add(Node entry, T ext);

    Map<Node, TreeNode<T>> getChildren();

    void setChildren(Map<Node, TreeNode<T>> children);

    boolean isLeaf();

    TreeNode<T> getParent();

    String getName();

    void sort();
}
