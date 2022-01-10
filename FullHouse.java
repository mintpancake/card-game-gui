/**
 * The class models a hand of full house in a Big Two card game.
 * A full house consists of five cards, with two having the same
 * rank and three having another same rank. The card in the triplet
 * with the highest suit in a full house is referred to as the top
 * card of this full house. A full house always beats any straights
 * and flushes. A full house having a top card with a higher rank
 * beats a full house having a top card with a lower rank.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class FullHouse extends Hand {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public FullHouse(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 6;
    }

    /**
     * a method for retrieving the top card of this full house
     *
     * @return the top card of this full house
     */
    @Override
    public Card getTopCard() {
        if (this.getCard(0).rank == this.getCard(2).rank) {
            return this.getCard(2);
        } else {
            return this.getCard(4);
        }
    }

    /**
     * a method for checking if this is a full house
     *
     * @return whether this is a full house
     */
    @Override
    public boolean isValid() {
        if (this.size() != 5) {
            return false;
        }
        if (this.getCard(0).rank == this.getCard(2).rank) {
            return this.getCard(0).rank == this.getCard(1).rank && this.getCard(3).rank == this.getCard(4).rank;
        } else if (this.getCard(2).rank == this.getCard(4).rank) {
            return this.getCard(2).rank == this.getCard(3).rank && this.getCard(0).rank == this.getCard(1).rank;
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
        return "FullHouse";
    }
}
