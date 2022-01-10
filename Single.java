
/**
 * The class models a hand of single in a Big Two card game.
 * A single consists of only one single card. The only card
 * in a single is referred to as the top card of this single.
 * A single with a higher rank beats a single with a lower rank.
 * For singles with the same rank, the one with a higher suit
 * beats the one with a lower suit.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Hand
 */
public class Single extends Hand {
    /**
     * a constructor for building a hand with the specified player and list of cards
     *
     * @param player the specified player
     * @param cards  the list of cards
     */
    public Single(CardGamePlayer player, CardList cards) {
        super(player, cards);
        this.type = 1;
    }

    /**
     * a method for checking if this is a single
     *
     * @return whether this is a valid hand
     */
    @Override
    public boolean isValid() {
        return this.size() == 1;
    }

    /**
     * a method for returning a string specifying the type of this hand
     *
     * @return a string specifying the type of this hand
     */
    @Override
    public String getType() {
        return "Single";
    }
}
