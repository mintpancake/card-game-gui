
/**
 * The class models a hand of quad in a Big Two card game.
 * A quad consists of five cards, with four having the same
 * rank. The card in the quadruplet with the highest suit
 * in a quad is referred to as the top card of this quad.
 * A quad always beats any straights, flushes and full houses.
 * A quad having a top card with a higher rank beats a quad
 * having a top card with a lower rank.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class Quad extends Hand {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public Quad(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 7;
    }

    /**
     * a method for retrieving the top card of this quad
     *
     * @return the top card of this quad
     */
    @Override
    public Card getTopCard() {
        if (this.getCard(0).rank == this.getCard(2).rank) {
            return this.getCard(3);
        } else {
            return this.getCard(4);
        }
    }

    /**
     * a method for checking if this is a valid hand
     *
     * @return whether this is a valid hand
     */
    @Override
    public boolean isValid() {
        if (this.size() != 5) {
            return false;
        }
        if (this.getCard(0).rank == this.getCard(2).rank) {
            return this.getCard(0).rank == this.getCard(1).rank && this.getCard(2).rank == this.getCard(3).rank;
        } else if (this.getCard(2).rank == this.getCard(4).rank) {
            return this.getCard(1).rank == this.getCard(2).rank && this.getCard(3).rank == this.getCard(4).rank;
        } else {
            return false;
        }
    }

    /**
     * a method for returning a string specifying the type of this hand
     *
     * @return a string specifying the type of this hand
     */
    @Override
    public String getType() {
        return "Quad";
    }
}
