package com.branc.pino.core.util;

import java.util.*;

class ObjectTree {
    private final Object treeLock = new Object();

    private final Set<Disposable> rootObjects = new HashSet<>();
    private final Map<Disposable, TreeNode> nodeMap = new HashMap<>();
    private final Map<Disposable, Object> disposedObjects = new WeakHashMap<>(100, 0.5f);

    private TreeNode getNode(Disposable disposable) {
        return nodeMap.get(disposable);
    }

    private void putNode(Disposable disposable, TreeNode node) {
        if (node == null) {
            nodeMap.remove(disposable);
        } else {
            nodeMap.put(disposable, node);
        }
    }

    public void register(Disposable parent, Disposable child) {
        Objects.requireNonNull(parent);
        Objects.requireNonNull(child);
        if (parent == child) {
            throw new IllegalArgumentException("parent == child");
        }

        synchronized (treeLock) {
            if (disposedObjects.get(parent) != null) {
                throw new IllegalArgumentException(String.format(
                        "parent: %s has already been disposed, so the child: %s will never be disposed.",
                        parent,
                        child
                ));
            }

            disposedObjects.remove(child);

            var parentNode = getNode(parent);
            if (parentNode == null) {
                parentNode = createNode(parent, null);
            }

            var childNode = getNode(child);
            if (childNode == null) {
                childNode = createNode(child, parentNode);
            } else {
                childNode.getParent().removeChild(childNode);
            }
            rootObjects.remove(child);
            parentNode.addChild(childNode);
        }
    }

    private TreeNode createNode(Disposable obj, TreeNode parent) {
        Objects.requireNonNull(obj, "obj is null.");

        var newNode = new TreeNode(this, parent, obj);

        if (parent == null) {
            rootObjects.add(obj);
        }
        putNode(obj, newNode);
        return newNode;
    }

    public boolean isDisposed(Disposable obj) {
        synchronized (treeLock) {
            return disposedObjects.get(obj) != null;
        }
    }

    public void executeAll(Disposable disposable) {
        final var disposables = new ArrayList<Disposable>();
        var node = getNode(disposable);

        if (node == null) {
            disposables.add(disposable);
        } else {
            node.getAndRemoveChildRecursively(disposables);
        }

        for (Disposable d: disposables) {
            d.dispose();
        }
    }

    public void removeObjectFromTree(TreeNode node) {
        synchronized (treeLock) {
            final var obj = node.getDisposable();
            putNode(obj, null);
            final var parent = node.getParent();
            if (parent == null) {
                rootObjects.remove(obj);
            } else {
                parent.removeChild(node);
            }
            node.setParent(null);
        }
    }
}
