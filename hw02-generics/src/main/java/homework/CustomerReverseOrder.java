package homework;

import java.util.Stack;

public class CustomerReverseOrder {

    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    private final Stack<Customer> customers;

    public CustomerReverseOrder() {
        this.customers = new Stack<>();
    }

    public void add(Customer customer) {
        customers.add(customer);
    }

    public Customer take() {
        return customers.pop();
    }
}
