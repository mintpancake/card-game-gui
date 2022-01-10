
/**
 * The class is used to model a deck of cards used in a Big Two card game.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Deck
 */
public class BigTwoDeck extends Deck {
    /**
     * a method for initializing a deck of Big Two cards
     */
    @Override
    public void initialize() {
        removeAllCards();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                Card card = new BigTwoCard(i, j);
                addCard(card);
            }
        }
    }
}
