package pizzeria;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;

public class Pizzeria implements Customer.CustomerEventHandler, Pizzaiolo.PiazzoloEventHandler {
    private Map<String, PizzaType> pizzaTypes;
    private List<Pizzaiolo> pizzaiolos;
    private BlockingQueue<PizzaOrder> orderQueue;
    private BlockingQueue<Customer> customers;
    private CountDownLatch countDownLatch;

    public void open(PizzeriaParameters pizzeriaParameters) {
        buildPizzeria(pizzeriaParameters);
        try {
            processOrders("Margherita", pizzeriaParameters.getNumberOfOrderForMargherita());
            processOrders("Capriciosa", pizzeriaParameters.getNumberOfOrderForCapriciosa());
            processOrders("Quatro Stagioni", pizzeriaParameters.getNumberOfOrderForQuatroStagioni());
            waitForAllOrdersThenClose(pizzeriaParameters);
        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
        }
    }

    private void waitForAllOrdersThenClose(PizzeriaParameters pizzeriaParameters) throws InterruptedException {
        countDownLatch.await();
        close();
    }

    private int countTotalNumberOfOrders(PizzeriaParameters pizzeriaParameters) {
        return pizzeriaParameters.getNumberOfOrderForMargherita()
                + pizzeriaParameters.getNumberOfOrderForCapriciosa()
                + pizzeriaParameters.getNumberOfOrderForQuatroStagioni();
    }

    private void processOrders(String pizzaType, int numberOfOrder) throws InterruptedException {
        Thread orderThread = new Thread(() -> {
            try {
                for (int i = 0; i < numberOfOrder; i++) {
                    Customer customer = getNewCustomer();
                    customer.orderPizza(pizzaTypes.get(pizzaType));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                close();
            }
        });
        orderThread.start();
    }

    private Customer getNewCustomer() throws InterruptedException {
        Customer customer = new Customer(this);
        if (!customers.offer(customer)) {
            System.out.println("!!! The Pizzeria is full");
            customers.put(customer);
        }
        return customer;
    }

    private void close() {
        for (Pizzaiolo pizzaiolo : pizzaiolos) {
            pizzaiolo.stop();
        }
    }

    private void buildPizzeria(PizzeriaParameters pizzeriaParameters) {
        orderQueue = new LinkedBlockingDeque<>();
        customers = new LinkedBlockingDeque<>(pizzeriaParameters.getNumberOfPlaceInPizzeria());
        buildPizzaiolos(pizzeriaParameters);
        buildPizzaTypes();
        int totalNumberOfOrders = countTotalNumberOfOrders(pizzeriaParameters);
        countDownLatch = new CountDownLatch(totalNumberOfOrders);
    }

    private void buildPizzaTypes() {
        if (pizzaTypes == null || pizzaTypes.isEmpty()) {
            buildDefaultPizzaTypes();
        }
    }

    public void setPizzaTypes(Map<String, PizzaType> pizzaTypes) {
        this.pizzaTypes = pizzaTypes;
    }

    private void buildPizzaiolos(PizzeriaParameters pizzeriaParameters) {
        int numberOfPizzaiolo = pizzeriaParameters.getNumberOfPizzaiolo();
        pizzaiolos = new ArrayList<>();
        for (int i = 1; i <= numberOfPizzaiolo; i++) {
            Pizzaiolo pizzaiolo = new Pizzaiolo(orderQueue, this);
            pizzaiolo.work();

            pizzaiolos.add(pizzaiolo);
        }
    }

    private void buildDefaultPizzaTypes() {
        pizzaTypes = new HashMap<>();
        pizzaTypes.put("Margherita", new PizzaType("Margherita", "8,50€", Arrays.asList("tomato", "mozzarella")));
        pizzaTypes.put("Capriciosa", new PizzaType("Capriciosa", "11,50€", Arrays.asList("tomato", "mozzarella", "ham", "artichokes")));
        pizzaTypes.put("Quatro Stagioni", new PizzaType("Quatro Stagioni", "11,50€", Arrays.asList("tomato", "mozzarella", "ham", "mushrooms",
                "artichokes", "anchovies")));
    }

    @Override
    public void orderSubmitted(PizzaOrder pizzaOrder) {
        try {
            orderQueue.put(pizzaOrder);
        } catch (InterruptedException e) {
            e.printStackTrace();
            close();
        }
    }

    @Override
    public void orderReceived(Customer customer, PizzaOrder pizzaOrder) {
        System.out.println("- " + customer.getId() + " starts to eat the pizza " + pizzaOrder.getId());
    }

    @Override
    public void orderConsumed(Customer customer, PizzaOrder pizzaOrder) {
        System.out.println("- " + customer.getId() + " finished to eat the pizza " + pizzaOrder.getId());
        customers.remove(customer);
        countDownLatch.countDown();
    }

    @Override
    public void orderFinished(Pizzaiolo pizzaiolo, PizzaOrder pizzaOrder) {
        System.out.println("# " + pizzaOrder.getId() + " - " + pizzaOrder.getPizzaType().getName() + " prepared by " + pizzaiolo.getId() + " is ready");
        Customer customer = pizzaOrder.getCustomer();
        customer.receive(pizzaOrder);
    }
}
