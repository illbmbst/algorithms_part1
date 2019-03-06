import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private final static int INIT_SIZE = 50;
    private Object[] array;
    private int size = 0;

    public RandomizedQueue() {
        array = new Object[INIT_SIZE];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        array[size++] = item;
        if (size == array.length) {
            resize(array.length * 2);
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(0, size);
        Item item = (Item) array[index];
        array[index] = array[--size];
        array[size] = null;
        if (size > 0 && size == array.length / 4) {
            resize(array.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int index;
        index = StdRandom.uniform(0, size);
        return (Item) array[index];
    }

    private void resize(int capacity) {
        array = java.util.Arrays.copyOf(array, capacity);
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {

            int[] orderArray = new int[RandomizedQueue.this.size];
            int current = 0;

            {
                for (int i = 0; i < size; i++) {
                    orderArray[i]= i;
                }
                StdRandom.shuffle(orderArray);
            }

            @Override
            public boolean hasNext() {
                return !isEmpty() && current < orderArray.length;
            }

            @Override
            public Item next() {
                if (isEmpty() || current == orderArray.length) {
                    throw new NoSuchElementException();
                }
                return (Item) array[orderArray[current++]];
            }
        };
    }
}
