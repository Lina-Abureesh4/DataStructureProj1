package application;

public class Node<T1 extends Comparable<T1>, T2> {

	private T1 label;
	private T2 data;
	private Node<T1, T2> next;

	public Node(T1 label, T2 data) {
		super();
		this.label = label;
		this.data = data;
	}

	public T1 getLabel() {
		return label;
	}

	public void setLabel(T1 label) {
		this.label = label;
	}

	public T2 getData() {
		return data;
	}

	public void setData(T2 data) {
		this.data = data;
	}

	public Node<T1, T2> getNext() {
		return next;
	}

	public void setNext(Node<T1, T2> next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return "Node= " + label + ", data: " + data;
	}

}