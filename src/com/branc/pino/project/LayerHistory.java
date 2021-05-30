package com.branc.pino.project;

import com.branc.pino.core.history.History;
import com.branc.pino.core.history.Memento;
import com.branc.pino.paint.layer.LayerObject;

import java.util.LinkedList;
import java.util.Optional;

public class LayerHistory implements History<Memento<LayerObject>> {
    private final int capacity;
    private final LinkedList<Memento<LayerObject>> undoStack;
    private final LinkedList<Memento<LayerObject>> redoStack;

    public LayerHistory(int capacity) {
        this.capacity = capacity;
        undoStack = new LinkedList<>();
        redoStack = new LinkedList<>();
    }

    @Override
    public void add(Memento<LayerObject> instance) {
        if (instance == null) throw new NullPointerException();
        redoStack.clear();
        undoStack.add(instance);
        if (size() > capacity) {
            undoStack.removeFirst();
        }
    }

    @Override
    public Memento<LayerObject> current() {
        return undoStack.getLast();
    }

    @Override
    public Memento<LayerObject> undo() {
        if (canUndo()) {
            redoStack.add(undoStack.removeLast());
            return current();
        }
        throw new IllegalStateException("Can't undo");
    }

    @Override
    public Memento<LayerObject> redo() {
        if (canRedo()) {
            undoStack.add(redoStack.removeLast());
            return current();
        }
        throw new IllegalStateException("Can't redo");
    }

    @Override
    public boolean canUndo() {
        return undoStack.size() > 1;
    }

    @Override
    public boolean canRedo() {
        return redoStack.size() > 1;
    }

    @Override
    public Optional<Memento<LayerObject>> undoIfCan() {
        if (canUndo()) {
            return Optional.of(undo());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Memento<LayerObject>> redoIfCan() {
        if (canRedo()) {
            return Optional.of(redo());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public int size() {
        return undoStack.size() + redoStack.size();
    }

    @Override
    public int capacity() {
        return capacity;
    }
}
