
/**
 * The class models a hand of pair in a Big Two card game.
 * A pair consists of two cards with the same rank. The card
 * with a higher suit in a pair is referred to as the top
 * card of this pair. A pair with a higher rank beats a pair
 * with a lower rank. For pairs with the same rank, the one
 * containing the highest suit beats the other.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class Pair extends Hand {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public Pair(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 2;
    }

    /**
     * a method for checking if this is a pair
     *
     * @return whether this is a pair
     */
    @Override
    public boolean isValid() {
        return this.size() == 2 && this.getCard(0).rank == this.getCard(1).rank;
    }

    /**
     * a method for returning a string specifying the type of this hand
     *
     * @return a string specifying the type of this hand
     */
    @Override
    public String getType() {
        return "Pair";
    }
}
