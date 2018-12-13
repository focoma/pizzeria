package pizzeria;

import java.util.concurrent.atomic.AtomicLong;

public class PizzaOrder {
    private final long id;
    private final Customer customer;
    private final PizzaType pizzaType;

    private static final AtomicLong idGenerator = new AtomicLong(0);

    public PizzaOrder(Customer customer, PizzaType pizzaType) {
        this.id = idGenerator.incrementAndGet();
        this.customer = customer;
        this.pizzaType = pizzaType;
    }

    public long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    @Override
    public String toString() {
        return pizzaType.getName();
    }
}
