
/**
 * The class models a hand of straight in a Big Two card game.
 * A straight consists of five cards with consecutive ranks.
 * For the sake of simplicity, 2 and A can only form a straight
 * with K but not with 3. The card with the highest rank in a
 * straight is referred to as the top card of this straight.
 * A straight having a top card with a higher rank beats a
 * straight having a top card with a lower rank. For straights
 * having top cards with the same rank, the one having a top
 * card with a higher suit beats the one having a top card with
 * a lower suit.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class Straight extends Hand {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public Straight(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 4;
    }

    /**
     * a method for checking if this is a straight
     *
     * @return whether this is a straight
     */
    @Override
    public boolean isValid() {
        if (this.size() != 5) {
            return false;
        }
        int card0FixedRank = (this.getCard(0).rank == 0 || this.getCard(0).rank == 1) ? this.getCard(0).rank + 11 : this.getCard(0).rank - 2;
        for (int i = 1; i < 5; i++) {
            int cardIFixedRank = (this.getCard(i).rank == 0 || this.getCard(i).rank == 1) ? this.getCard(i).rank + 11 : this.getCard(i).rank - 2;
            if (cardIFixedRank - card0FixedRank != i) {
                return false;
            }
        }
        return true;
    }

    /**
     * a method for returning a string specifying the type of this hand
     *
     * @return a string specifying the type of this hand
     */
    @Override
    public String getType() {
        return "Straight";
    }
}
