
/**
 * The class models a hand of flush in a Big Two card game.
 * A flush consists of five cards with the same suit. The
 * card with the highest rank in a flush is referred to as
 * the top card of this flush. A flush always beats any
 * straights. A flush with a higher suit beats a flush with
 * a lower suit. For flushes with the same suit, the one
 * having a top card with a higher rank beats the one having
 * a top card with a lower rank.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class Flush extends Hand {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public Flush(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 5;
    }

    /**
     * a method for checking if this flush beats a specified hand
     *
     * @param hand a specified hand
     * @return whether this hand beats a specified hand
     */
    @Override
    public boolean beats(Hand hand) {
        if (!this.isValid() || !hand.isValid()) {
            return false;
        }
        if (hand.type == 4) {
            return true;
        } else if (hand.type > 5) {
            return false;
        } else {
            if (this.getTopCard().suit > hand.getTopCard().suit) {
                return true;
            } else if (this.getTopCard().suit < hand.getTopCard().suit) {
                return false;
            } else {
                int thisTopCardFixedRank = (this.getTopCard().rank == 0 || this.getTopCard().rank == 1) ? this.getTopCard().rank + 11 : this.getTopCard().rank - 2;
                int handTopCardFixedRank = (hand.getTopCard().rank == 0 || hand.getTopCard().rank == 1) ? hand.getTopCard().rank + 11 : hand.getTopCard().rank - 2;
                return thisTopCardFixedRank > handTopCardFixedRank;
            }
        }
    }

    /**
     * a method for checking if this is a flush
     *
     * @return whether this is a flush
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
        return true;
    }

    /**
     * a method for returning a string specifying the type of this hand
     *
     * @return a string specifying the type of this hand
     */
    @Override
    public String getType() {
        return "Flush";
    }
}
