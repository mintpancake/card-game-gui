
/**
 * The class models a hand of straight flush in a Big Two card game.
 * This hand consists of five cards with consecutive ranks and the
 * same suit. For the sake of simplicity, 2 and A can only form a
 * straight flush with K but not with 3. The card with the highest
 * rank in a straight flush is referred to as the top card of this
 * straight flush. A straight flush always beats any straights,
 * flushes, full houses and quads. A straight flush having a top
 * card with a higher rank beats a straight flush having a top card
 * with a lower rank. For straight flushes having top cards with the
 * same rank, the one having a top card with a higher suit beats one
 * having a top card with a lower suit.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class StraightFlush extends Hand {

    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public StraightFlush(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 8;
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
        for (int i = 1; i < 5; i++) {
            if (this.getCard(i).suit != this.getCard(0).suit) {
                return false;
            }
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
        return "StraightFlush";
    }
}
