package pizzeria;

import java.util.List;

public class PizzaType {
    private String name;
    private String cost; //Better to use JavaMoney if there's more time
    private List<String> ingredients;

    public PizzaType(String name, String cost, List<String> ingredients) {
        this.name = name;
        this.cost = cost;
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
