package pizzeria;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class Pizzaiolo implements Runnable {
    private static final long PIZZA_PREP_TIME_IN_MILLIS = 500;
    private static final AtomicLong idGenerator = new AtomicLong(0);

    private final BlockingQueue<PizzaOrder> orderQueue;
    private final long id;
    private final PiazzoloEventHandler piazzoloEventHandler;

    public Pizzaiolo(BlockingQueue<PizzaOrder> orderQueue, PiazzoloEventHandler piazzoloEventHandler) {
        this.id = idGenerator.incrementAndGet();
        this.orderQueue = orderQueue;
        this.piazzoloEventHandler = piazzoloEventHandler;
    }

    public long getId() {
        return id;
    }

    public void work() {
        Thread pizziaoloThread = new Thread(this);
        pizziaoloThread.start();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                PizzaOrder pizzaOrder = orderQueue.take();
                makePizza(pizzaOrder);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void makePizza(PizzaOrder pizzaOrder) throws InterruptedException {
        Thread.sleep(PIZZA_PREP_TIME_IN_MILLIS);
        synchronized (orderQueue) {
            piazzoloEventHandler.orderFinished(this, pizzaOrder);
        }
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }

    public interface PiazzoloEventHandler {
        void orderFinished(Pizzaiolo pizzaiolo, PizzaOrder pizzaOrder);
    }
}
