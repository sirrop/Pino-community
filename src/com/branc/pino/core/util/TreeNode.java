package com.branc.pino.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

class TreeNode {
    private final ObjectTree tree;
    private TreeNode parent;
    private final Disposable disposable;
    private final List<TreeNode> children = new ArrayList<>();

    public TreeNode(ObjectTree tree, TreeNode parent, Disposable disposable) {
        this.tree = tree;
        this.parent = parent;
        this.disposable = disposable;
        Objects.requireNonNull(tree);
        Objects.requireNonNull(disposable);
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public Disposable getDisposable() {
        return disposable;
    }

    public void addChild(TreeNode child) {
        Objects.requireNonNull(child);
        children.add(child);
        child.parent = this;
    }

    public boolean removeChild(TreeNode child) {
        Objects.requireNonNull(child);
        for (TreeNode node: children) {
            if (node.equals(child)) {
                children.remove(child);
                child.parent = null;
                return true;
            }
        }
        return false;
    }

    public void getAndRemoveChildRecursively(List<Disposable> result) {
        Objects.requireNonNull(result);
        getAndRemoveChildRecursively(result, null);
        tree.removeObjectFromTree(this);
        result.add(disposable);
        children.clear();
        parent = null;
    }

    public void getAndRemoveChildRecursively(
            List<Disposable> result,
            Predicate<? super Disposable> predicate
    ) {
        for (int i = children.size(); i > 0; i--) {
            final var childNode = children.get(i - 1);
            if (predicate == null || predicate.test(childNode.disposable)) {
                childNode.getAndRemoveChildRecursively(result);
            }
        }
    }

    public String toString() {
        return String.format("TreeNode: %s", disposable);
    }
}
