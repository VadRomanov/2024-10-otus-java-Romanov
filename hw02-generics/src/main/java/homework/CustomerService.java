package homework;

import java.util.AbstractMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    private final NavigableMap<Customer, String> customerMap;

    public CustomerService() {
        this.customerMap = new TreeMap<>((o1, o2) -> (int) (o1.getScores() - o2.getScores()));
    }

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> smallest = customerMap.firstEntry();
        Customer smallestCustomer = smallest.getKey();
        return new AbstractMap.SimpleEntry<>(new Customer(smallestCustomer.getId(), smallestCustomer.getName(),
                smallestCustomer.getScores()), smallest.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> next = customerMap.higherEntry(customer);
        if (next == null) {
            return null;
        }
        Customer nextCustomer = next.getKey();
        return new AbstractMap.SimpleEntry<>(new Customer(nextCustomer.getId(), nextCustomer.getName(),
                nextCustomer.getScores()), next.getValue());
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
