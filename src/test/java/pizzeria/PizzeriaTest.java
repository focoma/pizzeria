package pizzeria;

import org.junit.Test;

public class PizzeriaTest {
    @Test
    public void test1() {
        Pizzeria pizzeria = new Pizzeria();
        PizzeriaParameters pizzeriaParameters = new PizzeriaParameters(3, 50, 25, 25, 25);
        pizzeria.open(pizzeriaParameters);
    }

    @Test
    public void test2() {
        Pizzeria pizzeria = new Pizzeria();
        PizzeriaParameters pizzeriaParameters = new PizzeriaParameters(30, 50, 25, 25, 25);
        pizzeria.open(pizzeriaParameters);
    }
}
