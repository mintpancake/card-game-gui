
/**
 * The class is used to model a card used in a Big Two card game.
 *
 * @author Lu Meng
 * @version 1.0
 * @see Card
 */
public class BigTwoCard extends Card {
    /**
     * a constructor for building a card with the specified suit and rank
     *
     * @param suit an integer between 0 and 3
     * @param rank an integer between 0 and 12
     */
    public BigTwoCard(int suit, int rank) {
        super(suit, rank);
    }

    /**
     * a method for comparing the order of this card with the specified card
     *
     * @param card the card to be compared
     * @return a negative integer, zero, or a positive integer as this card is
     * less than, equal to, or greater than the specified card
     */
    @Override
    public int compareTo(Card card) {
        int thisFixedRank = (this.rank == 0 || this.rank == 1) ? this.rank + 11 : this.rank - 2;
        int cardFixedRank = (card.rank == 0 || card.rank == 1) ? card.rank + 11 : card.rank - 2;
        if (thisFixedRank > cardFixedRank) {
            return 1;
        } else if (thisFixedRank < cardFixedRank) {
            return -1;
        } else return Integer.compare(this.suit, card.suit);
    }
}
