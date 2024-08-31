package application;

public class CLinkedList<T1 extends Comparable<T1>, T2> {

	private Node<T1, T2> head;

	public Node getHead() {
		return head;
	}

	public void addAtHead(T1 label, T2 data) {
		Node n = new Node(label, data);
		if (head == null) {
			head = n;
			n.setNext(head);
		} else {
			n.setNext(head);
			Node<T1, T2> curr = head;
			while (curr.getNext() != head) {
				curr = curr.getNext();
			}
			head = n;
			curr.setNext(head);
		}
	}

	public void traverse() {
		Node<T1, T2> curr = head;
		if (curr != null) {
			do {
				System.out.println(curr);
				curr = curr.getNext();
			} while (curr != head);
		}
	}

	public Node<T1, T2> search(T1 label) {
		Node curr = head;
		if (curr != null)
			do {
				if (curr.getLabel().compareTo(label) == 0) {
					return curr;
				} else {
					curr = curr.getNext();
				}
			} while (curr != head);

		return null;
	}

//	public Node<T> searchR(T data) {
//		return searchR(data, head);
//	}

//	public Node<T> searchR(T data, Node<T> curr) {
//		if (curr == null) {
//			return null;
//		}
//		if (curr.getData().compareTo(data) == 0) {
//			return curr;
//		} else {
//			if (curr.getNext() != head) {
//				return searchR(data, curr.getNext());
//			} else {
//				return null;
//			}
//		}
//	}

//	public Node<T> delete(T data) {
//		Node n = head;
//		if (n != null) {
//
//			// if the list contains one item only
//			if (n == n.getNext()) {
//				if (n.getData().compareTo(data) == 0) {
//					head = null;
//					return n;
//				}
//
//				// if the list contains more than one item
//			} else {
//
//				// if data are stored at head
//				if (head.getData().compareTo(data) == 0) {
//					Node temp = head; 
//					Node curr = head.getNext();
//					while (curr.getNext() != head) {
//						curr = curr.getNext();
//					}
//					curr.setNext(head.getNext());
//					head = head.getNext();
//					return temp;
//
//					// if data are not stored at head
//				} else {
//					n = head.getNext();
//					Node prev = head;
//					do {
//						if (n.getData().compareTo(data) == 0) {
//							prev.setNext(n.getNext());
//
//							return n;
//						}
//						prev = n;
//						n = n.getNext();
//					} while (n != head.getNext());
//					return null;
//				}
//			}
//		}
//		return null;
//	}

	public void insertSorted(T1 label, T2 data) {
		Node neww = new Node(label, data);
		if (head == null) {
			head = neww;
			head.setNext(neww);
		} else {
			Node prev = head;
			Node curr = head.getNext();
			if (head.getLabel().compareTo(label) < 0) {
				neww.setNext(head);
				do {
					curr = curr.getNext();
				} while (curr.getNext() != head);
				curr.setNext(neww);
				head = neww;
			} else {
				do {
					if (curr.getLabel().compareTo(label) < 0) {
						neww.setNext(curr);
						prev.setNext(neww);
						break;
					}
					prev = curr;
					curr = curr.getNext();
				} while (curr != head);
				if (curr == head) {
					prev.setNext(neww);
					neww.setNext(head);
				}
			}
		}
	}

//	public void reverse() {
//		if (head != null) {
//			Node curr = head;
//			Node next = curr.getNext();
//			Node temp;
//			do {
//				temp = next.getNext();
//				next.setNext(curr);
//				curr = next;
//				next = temp;
//			} while (next != head);
//			head = curr;
//			next.setNext(head);
//		}
//	}

	public Node<T1, T2> searchSorted(T1 label) {
		Node curr = head;
		if (curr != null)
			do {
				if (curr.getLabel().compareTo(label) == 0) {
					return curr;
				} else if (curr.getLabel().compareTo(label) < 0) {
					return null;
				} else {
					curr = curr.getNext();
				}
			} while (curr != head);

		return null;
	}

	public Node<T1, T2> deleteSorted(T1 label) {
		Node curr = head;
		Node prev = head;
		if (curr != null) {
			do {
				// since the linkedList is sorted in descending order, then the first node will
				// be the greatest one, any greater value will not be in the list
				if (curr.getLabel().compareTo(label) < 0)
					return null;
				// if the node is found:
				if (curr.getLabel().compareTo(label) == 0) {
					if (curr == head) {
						head = null;
						return prev;
					}
					prev.setNext(curr.getNext());
					return curr;
				}
				prev = curr;
				curr = curr.getNext();
			} while (curr != head);
		}
		return null;
	}
}
