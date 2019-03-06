import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);

        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            for (String aStrArr : str.split(" ")) {
                queue.enqueue(aStrArr);
            }
        }

        int count = k;
        while (count > queue.size()) {
            System.out.println(queue.sample());
            count--;
        }
        for (int i = 0; i < queue.size(); i++) {
            System.out.println(queue.dequeue());
            if (--count == 0) {
                return;
            }
        }
    }
}
