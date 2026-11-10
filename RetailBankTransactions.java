import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class RetailBankTransactions {

    // =========================================
    // TRANSACTION CLASS
    // =========================================

    static class Transaction {

        int transactionId;
        String accountId;
        double amount;
        String merchant;
        LocalDateTime postingDate;

        Transaction(int id, String account, double amount,
                    String merchant, String date) {

            this.transactionId = id;
            this.accountId = account;
            this.amount = amount;
            this.merchant = merchant;

            this.postingDate = LocalDateTime.parse(
                date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            );
        }

        public String toString() {

            return transactionId + " " + accountId + " "
                    + amount + " " + merchant + " "
                    + postingDate;
        }
    }

    // =========================================
    // TASK 1 - LINEAR SEARCH AND BINARY SEARCH
    // =========================================

    static int linearSearch(int a[], int key) {

        int comparisons = 0;

        for (int i = 0; i < a.length; i++) {

            comparisons++;

            if (a[i] == key) {
                return comparisons;
            }
        }

        return comparisons;
    }

    static int binarySearch(int a[], int key) {

        int low = 0;
        int high = a.length - 1;
        int comparisons = 0;

        while (low <= high) {

            int mid = low + (high - low) / 2;

            comparisons++;

            if (a[mid] == key) {
                return comparisons;
            }

            else if (a[mid] < key) {
                low = mid + 1;
            }

            else {
                high = mid - 1;
            }
        }

        return comparisons;
    }

    static void task1() {

        System.out.println("\n===== TASK 1: SEARCHING =====");

        int n = 100000;

        int a[] = new int[n];

        // Generate sorted transaction IDs
        for (int i = 0; i < n; i++) {
            a[i] = 100001 + i;
        }

        System.out.println("Total Transaction IDs: " + n);

        // Best case
        int linearBest = linearSearch(a, a[0]);
        int binaryBest = binarySearch(a, a[n / 2]);

        // Calculate actual average and worst cases
        long binaryTotal = 0;
        int binaryWorst = 0;

        for (int id : a) {

            int count = binarySearch(a, id);

            binaryTotal += count;

            if (count > binaryWorst) {
                binaryWorst = count;
            }
        }

        double binaryAverage = (double) binaryTotal / n;

        double linearAverage = (n + 1) / 2.0;

        System.out.println("\nLINEAR SEARCH");
        System.out.println("Best Case: " + linearBest);
        System.out.println("Average Case: " + linearAverage);
        System.out.println("Worst Case: " + n);

        System.out.println("\nBINARY SEARCH");
        System.out.println("Best Case: 1");
        System.out.println("Average Case: " + binaryAverage);
        System.out.println("Worst Case: " + binaryWorst);

        // Example search
        int searchId = 150050;

        System.out.println("\nSearching Transaction ID: " + searchId);

        System.out.println("Linear Search Comparisons: "
                + linearSearch(a, searchId));

        System.out.println("Binary Search Comparisons: "
                + binarySearch(a, searchId));
    }

    // =========================================
    // TASK 2 - INSERTION SORT AND MERGE SORT
    // =========================================

    static void insertionSort(ArrayList<Transaction> list) {

        for (int i = 1; i < list.size(); i++) {

            Transaction key = list.get(i);

            int j = i - 1;

            while (j >= 0 &&
                   list.get(j).postingDate.compareTo(
                       key.postingDate) > 0) {

                list.set(j + 1, list.get(j));

                j--;
            }

            list.set(j + 1, key);
        }
    }

    static void mergeSort(ArrayList<Transaction> list,
                          int low, int high) {

        if (low < high) {

            int mid = low + (high - low) / 2;

            mergeSort(list, low, mid);

            mergeSort(list, mid + 1, high);

            merge(list, low, mid, high);
        }
    }

    static void merge(ArrayList<Transaction> list,
                      int low, int mid, int high) {

        ArrayList<Transaction> temp = new ArrayList<>();

        int i = low;
        int j = mid + 1;

        while (i <= mid && j <= high) {

            // <= maintains stability
            if (list.get(i).postingDate.compareTo(
                    list.get(j).postingDate) <= 0) {

                temp.add(list.get(i));
                i++;
            }

            else {

                temp.add(list.get(j));
                j++;
            }
        }

        while (i <= mid) {
            temp.add(list.get(i));
            i++;
        }

        while (j <= high) {
            temp.add(list.get(j));
            j++;
        }

        for (int k = 0; k < temp.size(); k++) {
            list.set(low + k, temp.get(k));
        }
    }

    static ArrayList<Transaction> getStatement() {

    ArrayList<Transaction> list = new ArrayList<>();

    list.add(new Transaction(1004, "A101", 800,
            "Amazon", "2026-11-10 10:15:00"));

    list.add(new Transaction(1001, "A101", 2500,
            "Amazon", "2026-11-10 09:00:00"));

    list.add(new Transaction(1002, "A101", 1500,
            "Flipkart", "2026-11-10 09:05:00"));

    list.add(new Transaction(1003, "A101", 1200,
            "Myntra", "2026-11-10 09:00:00"));

    list.add(new Transaction(1005, "A101", 3000,
            "Walmart", "2026-11-10 10:30:00"));

    return list;
    }

    static void displayTransactions(ArrayList<Transaction> list) {

        for (Transaction t : list) {
            System.out.println(t);
        }
    }

    static void task2() {

        System.out.println("\n===== TASK 2: SORTING =====");

        ArrayList<Transaction> insertionList = getStatement();

        ArrayList<Transaction> mergeList = getStatement();

        System.out.println("\nBefore Sorting:");
        displayTransactions(insertionList);

        insertionSort(insertionList);

        System.out.println("\nAfter Insertion Sort:");
        displayTransactions(insertionList);

        mergeSort(mergeList, 0, mergeList.size() - 1);

        System.out.println("\nAfter Merge Sort:");
        displayTransactions(mergeList);
    }

    // =========================================
    // TASK 3 - DUPLICATE DETECTION USING HASHMAP
    // =========================================

    static class TransactionKey {

        String accountId;
        long amountInCents;
        String merchant;

        TransactionKey(Transaction t) {

            accountId = t.accountId;
            amountInCents = Math.round(t.amount * 100);
            merchant = t.merchant;
        }

        public boolean equals(Object obj) {

            if (this == obj) return true;

            if (!(obj instanceof TransactionKey)) return false;

            TransactionKey other = (TransactionKey) obj;

            return accountId.equals(other.accountId)
                    && amountInCents == other.amountInCents
                    && merchant.equalsIgnoreCase(other.merchant);
        }

        public int hashCode() {

            return Objects.hash(
                accountId,
                amountInCents,
                merchant.toLowerCase(Locale.ROOT)
            );
        }
    }

    static void task3() {

        System.out.println("\n===== TASK 3: DUPLICATE DETECTION =====");

        ArrayList<Transaction> list = new ArrayList<>();

        list.add(new Transaction(1001, "A101", 2500,
                "Amazon", "2026-11-10 09:00:00"));

        list.add(new Transaction(1002, "A102", 1500,
                "Flipkart", "2026-11-10 09:05:00"));

        list.add(new Transaction(1003, "A101", 2500,
                "Amazon", "2026-11-10 09:00:45"));

        list.add(new Transaction(1004, "A101", 2500,
                "Amazon", "2026-11-10 09:03:00"));

        // Sort by transaction time
        list.sort(Comparator.comparing(t -> t.postingDate));

        HashMap<TransactionKey, Transaction> map =
                new HashMap<>();

        boolean found = false;

        for (Transaction current : list) {

            TransactionKey key = new TransactionKey(current);

            Transaction previous = map.get(key);

            if (previous != null) {

                long seconds = Duration.between(
                    previous.postingDate,
                    current.postingDate
                ).getSeconds();

                if (seconds <= 60) {

                    System.out.println(
                        "Possible Duplicate Found: "
                        + previous.transactionId
                        + " and "
                        + current.transactionId
                    );

                    found = true;
                }
            }

            // Update latest transaction for this key
            map.put(key, current);
        }

        if (!found) {
            System.out.println("No Duplicate Transactions!");
        }
    }

    // =========================================
    // TASK 4 - CUSTOM QUEUE
    // =========================================

    static class CustomQueue {

        Transaction a[];
        int front;
        int rear;
        int size;

        CustomQueue(int capacity) {

            a = new Transaction[capacity];

            front = 0;
            rear = -1;
            size = 0;
        }

        void enqueue(Transaction t) {

            if (size == a.length) {

                System.out.println("Queue is Full!");
                return;
            }

            rear = (rear + 1) % a.length;

            a[rear] = t;

            size++;

            System.out.println("Added to Review Queue: "
                    + t.transactionId);
        }

        Transaction dequeue() {

            if (size == 0) {

                System.out.println("Queue is Empty!");
                return null;
            }

            Transaction t = a[front];

            a[front] = null;

            front = (front + 1) % a.length;

            size--;

            return t;
        }

        boolean isEmpty() {
            return size == 0;
        }
    }

    // =========================================
    // TASK 4 - CUSTOM STACK
    // =========================================

    static class Adjustment {

        int transactionId;
        double previousAmount;
        double updatedAmount;

        Adjustment(int id, double oldAmount, double newAmount) {

            transactionId = id;
            previousAmount = oldAmount;
            updatedAmount = newAmount;
        }
    }

    static class CustomStack {

        Adjustment a[];
        int top;

        CustomStack(int capacity) {

            a = new Adjustment[capacity];

            top = -1;
        }

        void push(Adjustment adjustment) {

            if (top == a.length - 1) {

                System.out.println("Stack is Full!");
                return;
            }

            a[++top] = adjustment;

            System.out.println("Adjustment Saved: "
                    + adjustment.transactionId);
        }

        Adjustment pop() {

            if (top == -1) {

                System.out.println("Stack is Empty!");
                return null;
            }

            return a[top--];
        }

        boolean isEmpty() {
            return top == -1;
        }
        
    }

    static void task4() {

        System.out.println("\n===== TASK 4: STACK AND QUEUE =====");

        // Queue demonstration
        CustomQueue q = new CustomQueue(10);

        Transaction t1 = new Transaction(1001, "A101", 2500,
                "Amazon", "2026-11-10 09:00:00");

        Transaction t2 = new Transaction(1002, "A102", 1500,
                "Flipkart", "2026-11-10 09:05:00");

        Transaction t3 = new Transaction(1003, "A103", 3500,
                "Walmart", "2026-11-10 09:10:00");

        System.out.println("\nManual Review Queue:");

        q.enqueue(t1);
        q.enqueue(t2);
        q.enqueue(t3);

        System.out.println("\nProcessing Review Queue:");

        while (!q.isEmpty()) {

            Transaction t = q.dequeue();

            System.out.println("Reviewing Transaction: "
                    + t.transactionId);
        }

        // Stack demonstration
        CustomStack stack = new CustomStack(10);

        double currentAmount = 2500;

        System.out.println("\nAdjustment Stack:");

        Adjustment adjustment1 =
                new Adjustment(1001, 2500, 2000);

        currentAmount = adjustment1.updatedAmount;

        stack.push(adjustment1);

        Adjustment adjustment2 =
                new Adjustment(1001, 2000, 1500);

        currentAmount = adjustment2.updatedAmount;

        stack.push(adjustment2);

        System.out.println("Current Amount: " + currentAmount);

        System.out.println("\nUndo Last Adjustment:");

        Adjustment last = stack.pop();

        if (last != null) {

            currentAmount = last.previousAmount;

            System.out.println("Restored Amount: " + currentAmount);
        }
    }

    // =========================================
    // TASK 5 - BRUTE FORCE MINIMUM AND MAXIMUM
    // =========================================

    static class MinMax {

        int min;
        int max;

        MinMax(int min, int max) {

            this.min = min;
            this.max = max;
        }
    }

    static long bruteComparisons = 0;
    static long divideComparisons = 0;

    static MinMax bruteForce(int a[]) {

        int min = a[0];
        int max = a[0];

        for (int i = 1; i < a.length; i++) {

            bruteComparisons++;

            if (a[i] < min) {
                min = a[i];
            }
        }

        for (int i = 1; i < a.length; i++) {

            bruteComparisons++;

            if (a[i] > max) {
                max = a[i];
            }
        }

        return new MinMax(min, max);
    }

    static MinMax divideAndConquer(int a[],
                                   int low, int high) {

        // One element
        if (low == high) {

            return new MinMax(a[low], a[low]);
        }

        // Two elements
        if (high == low + 1) {

            divideComparisons++;

            if (a[low] < a[high]) {

                return new MinMax(a[low], a[high]);

            } else {

                return new MinMax(a[high], a[low]);
            }
        }

        int mid = low + (high - low) / 2;

        MinMax left = divideAndConquer(a, low, mid);

        MinMax right = divideAndConquer(a, mid + 1, high);

        divideComparisons++;

        int min = Math.min(left.min, right.min);

        divideComparisons++;

        int max = Math.max(left.max, right.max);

        return new MinMax(min, max);
    }

    static void task5() {

        System.out.println("\n===== TASK 5: MINIMUM AND MAXIMUM =====");

        int amounts[] = {2500, 1500, 5000, 800, 3000, 1200};

        System.out.println("Transaction Amounts: "
                + Arrays.toString(amounts));

        bruteComparisons = 0;

        MinMax brute = bruteForce(amounts);

        System.out.println("\nBrute Force:");

        System.out.println("Minimum Amount: " + brute.min);

        System.out.println("Maximum Amount: " + brute.max);

        System.out.println("Comparisons: " + bruteComparisons);

        divideComparisons = 0;

        MinMax result = divideAndConquer(
                amounts, 0, amounts.length - 1);

        System.out.println("\nDivide and Conquer:");

        System.out.println("Minimum Amount: " + result.min);

        System.out.println("Maximum Amount: " + result.max);

        System.out.println("Comparisons: " + divideComparisons);
    }

    // =========================================
    // MAIN METHOD
    // =========================================

    public static void main(String[] args) {

        System.out.println("===== RETAIL BANK TRANSACTIONS =====");

        task1();

        task2();

        task3();

        task4();

        task5();

        System.out.println("\n===== ALL TASKS COMPLETED =====");
    }
}
