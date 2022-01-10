
/**
 * The class models a hand of triple in a Big Two card game.
 * A triple consists of three cards with the same rank. The
 * card with the highest suit in a triple is referred to as
 * the top card of this triple. A triple with a higher rank
 * beats a triple with a lower rank.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class Triple extends Hand {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public Triple(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 3;
    }

    /**
     * a method for checking if this is a triple
     *
     * @return whether this is a triple
     */
    @Override
    public boolean isValid() {
        return this.size() == 3 && this.getCard(0).rank == this.getCard(1).rank && this.getCard(1).rank == this.getCard(2).rank;
    }

    /**
     * a method for returning a string specifying the type of this hand
     *
     * @return a string specifying the type of this hand
     */
    @Override
    public String getType() {
        return "Triple";
    }
}
