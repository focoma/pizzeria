package pizzeria;

public class PizzeriaParameters {
    private int numberOfPizzaiolo;
    private int numberOfPlaceInPizzeria;
    private int numberOfOrderForMargherita;
    private int numberOfOrderForCapriciosa;
    private int numberOfOrderForQuatroStagioni;

    public PizzeriaParameters(int numberOfPizzaiolo, int numberOfPlaceInPizzeria, int numberOfOrderForMargherita, int numberOfOrderForCapriciosa, int numberOfOrderForQuatroStagioni) {
        this.numberOfPizzaiolo = numberOfPizzaiolo;
        this.numberOfPlaceInPizzeria = numberOfPlaceInPizzeria;
        this.numberOfOrderForMargherita = numberOfOrderForMargherita;
        this.numberOfOrderForCapriciosa = numberOfOrderForCapriciosa;
        this.numberOfOrderForQuatroStagioni = numberOfOrderForQuatroStagioni;
    }

    public int getNumberOfPizzaiolo() {
        return numberOfPizzaiolo;
    }

    public int getNumberOfPlaceInPizzeria() {
        return numberOfPlaceInPizzeria;
    }

    public int getNumberOfOrderForMargherita() {
        return numberOfOrderForMargherita;
    }

    public int getNumberOfOrderForCapriciosa() {
        return numberOfOrderForCapriciosa;
    }

    public int getNumberOfOrderForQuatroStagioni() {
        return numberOfOrderForQuatroStagioni;
    }
}
