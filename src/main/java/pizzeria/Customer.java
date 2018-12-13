package pizzeria;

import java.util.concurrent.atomic.AtomicLong;

public class Customer implements Runnable {
    private static final long PIZZA_EATING_TIME_IN_MILLIS = 1000;
    private static final AtomicLong idGenerator = new AtomicLong(0);
    private final long id;
    private final CustomerEventHandler customerEventHandler;

    private transient PizzaOrder pizzaOrder;

    public Customer(CustomerEventHandler customerEventHandler) {
        this.id = idGenerator.incrementAndGet();
        this.customerEventHandler = customerEventHandler;
    }

    public long getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            eat();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void orderPizza(PizzaType pizzaType) {
        this.pizzaOrder = new PizzaOrder(this, pizzaType);
        customerEventHandler.orderSubmitted(pizzaOrder);
    }

    public void receive(PizzaOrder pizzaOrder) {
        if (!this.pizzaOrder.equals(pizzaOrder)) {
            throw new IllegalArgumentException("Wrong pizza order received");
        }
        customerEventHandler.orderReceived(this, pizzaOrder);
        Thread customerThread = new Thread(this);
        customerThread.start();
    }

    private void eat() throws InterruptedException {
        Thread.sleep(PIZZA_EATING_TIME_IN_MILLIS);
        customerEventHandler.orderConsumed(this, pizzaOrder);
    }

    public interface CustomerEventHandler {
        void orderSubmitted(PizzaOrder pizzaOrder);
        void orderReceived(Customer customer, PizzaOrder pizzaOrder);
        void orderConsumed(Customer customer, PizzaOrder pizzaOrder);
    }
}
