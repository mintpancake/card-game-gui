/**
 * The class is used to model a hand of cards.
 *
 * @author Lu Meng
 * @version 1.0
 * @see CardList
 */
public abstract class Hand extends CardList {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public Hand(CardGamePlayer player, CardList cards) {
        this.player = player;
        for (int i = 0; i < cards.size(); i++) {
            this.addCard(cards.getCard(i));
        }
        this.sort();
    }

    private CardGamePlayer player;
    protected int type;

    /**
     * a method for retrieving the player of this hand
     *
     * @return the player of this hand
     */
    public CardGamePlayer getPlayer() {
        return player;
    }

    /**
     * a method for retrieving the top card of this hand
     * (it only works for single, pair, triple, straight,
     * flush and straight flush but not for full house and
     * quad)
     *
     * @return the top card of this hand
     */
    public Card getTopCard() {
        return this.getCard(this.size() - 1);
    }

    /**
     * a method for checking if this hand beats a specified hand
     * (it works only for single, pair, triple, straight, full house,
     * quad and straight flush but not for flush)
     *
     * @param hand a specified hand
     * @return whether this hand beats a specified hand
     */
    public boolean beats(Hand hand) {
        if (!this.isValid() || !hand.isValid()) {
            return false;
        }
        if (this.size() == hand.size()) {
            if (this.type > hand.type) {
                return true;
            } else if (this.type < hand.type) {
                return false;
            } else {
                return this.getTopCard().compareTo(hand.getTopCard()) > 0;
            }
        }
        return false;
    }

    /**
     * a method for checking if this is a valid hand
     *
     * @return whether this is a valid hand
     */
    public abstract boolean isValid();

    /**
     * a method for returning a string specifying the type of this hand
     *
     * @return a string specifying the type of this hand
     */
    public abstract String getType();
}
