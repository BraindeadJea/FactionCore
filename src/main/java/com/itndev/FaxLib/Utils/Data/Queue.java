package com.itndev.FaxLib.Utils.Data;

import java.util.ArrayList;
import java.util.List;

public class Queue {

    private int size = 16;

    private List<Object> QueueList = new ArrayList<>();

    public Queue(int size) {
        this.size = size;
    }

    public Queue() {
        this.size = -1;
    }

    public void add(Object value) {
        QueueList.add(value);
    }

    public Object pop_front() {
        Object o = QueueList.get(0);
        QueueList.remove(0);
        return o;
    }

    public void clear() {
        QueueList.clear();
    }
}
