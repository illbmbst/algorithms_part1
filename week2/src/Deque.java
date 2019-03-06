import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first = null;
    private Node<Item> last = null;
    private int size = 0;

    public Deque() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldFirst = this.first;
        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.next = oldFirst;
        this.first = newFirst;
        if (oldFirst != null) {
            oldFirst.prev = newFirst;
        }
        size++;
        if (last == null || size == 1) {
            last = newFirst;
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node oldLast = this.last;
        Node newLast = new Node();
        newLast.item = item;
        newLast.prev = oldLast;
        this.last = newLast;
        if (oldLast != null) {
            oldLast.next = newLast;
        }
        size++;
        if (first == null || size == 1) {
            first = newLast;
        }
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> first = this.first;
        this.first.prev = null;
        this.first = first.next;
        if (--size == 1) {
            this.last = first.next;
            this.last.prev = null;
        } else if (size == 0) {
            this.last = null;
            this.first = null;
        }
        return first.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> last = this.last;
        this.last.next = null;
        this.last = last.prev;
        if (--size == 1) {
            this.first = last.prev;
            this.first.next = null;
        } else if (size == 0) {
            this.first = null;
            this.last = null;
        }
        return last.item;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            Node<Item> next = Deque.this.first;

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Item next() {
                if (next == null) {
                    throw new NoSuchElementException();
                }
                Item item = next.item;
                next = next.next;
                return item;
            }
        };
    }

    private static class Node<Item> {
        Item item;
        Node<Item> prev;
        Node<Item> next;
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addLast(1);
        deque.addFirst(2);
        deque.addLast(3);
        deque.addFirst(4);
        deque.addLast(5);
        deque.addFirst(6);
        deque.removeFirst();
        deque.addLast(8);
        deque.removeLast();

        for (Integer integer : deque) {
            System.out.println(integer);
        }
    }
}
