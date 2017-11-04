package com.cd.voyager.email;


import java.util.LinkedList;
import java.util.List;

public class Queue {

	private static Queue queue = new Queue();

	private List list;

	public static final int SIZE = 10;

	public Queue() {
		list = new LinkedList();
	}

	public static Queue getInstance() {
		return queue;

	}

	public boolean isFull() {
		return list.size() == SIZE;
	}

	public void add(Object obj) {
		list.add(obj);
	}

	public Object poll() {
		return (Object) list.remove(0);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public void clear() {
		list.clear();
	}

}
