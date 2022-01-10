import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The BigTwoClient class is used to model a Big Two card game
 * that supports 4 players playing over the internet.
 *
 * @author Lu Meng
 * @version 1.0
 * @see CardGame
 * @see NetworkGame
 */
public class BigTwoClient implements CardGame, NetworkGame {
    /**
     * a constructor for creating a Big Two client.
     */
    public BigTwoClient() {
        for (int i = 0; i < 4; i++) {
            playerList.add(new CardGamePlayer());
        }
        table = new BigTwoTable(this);
        table.disable();
        makeConnection();
    }

    public BigTwoClient(String name, String ip, int port) {
        for (int i = 0; i < 4; i++) {
            playerList.add(new CardGamePlayer());
        }
        playerName = name;
        serverIP = ip;
        serverPort = port;
        table = new BigTwoTable(this);
        table.disable();
        makeConnection();
    }

    private int numOfPlayers = 0;
    private Deck deck;
    private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>();
    private ArrayList<Hand> handsOnTable = new ArrayList<Hand>();
    private int playerID;
    private String playerName = "Anonymous";
    private String serverIP = "127.0.0.1";
    private int serverPort = 2396;
    private Socket sock;
    private ObjectOutputStream oos;
    private int currentIdx;
    private BigTwoTable table;
    boolean connected = false;

    private int lastIdx;
    private final Color warning = new Color(250, 233, 81);
    private final Color highlight = new Color(236, 242, 247);

    /**
     * Returns the number of players in this card game.
     *
     * @return the number of players in this card game
     */
    @Override
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Returns the deck of cards being used in this card game.
     *
     * @return the deck of cards being used in this card game
     */
    @Override
    public Deck getDeck() {
        return deck;
    }

    /**
     * Returns the list of players in this card game.
     *
     * @return the list of players in this card game
     */
    @Override
    public ArrayList<CardGamePlayer> getPlayerList() {
        return playerList;
    }

    /**
     * Returns the list of hands played on the table.
     *
     * @return the list of hands played on the table
     */
    @Override
    public ArrayList<Hand> getHandsOnTable() {
        return handsOnTable;
    }

    /**
     * Returns the index of the current player.
     *
     * @return the index of the current player
     */
    @Override
    public int getCurrentIdx() {
        return currentIdx;
    }

    /**
     * Starts the card game.
     *
     * @param deck the deck of (shuffled) cards to be used in this game
     */
    @Override
    public void start(Deck deck) {
        this.deck = deck;
        for (int i = 0; i < 4; i++) { // remove all the cards from the players
            playerList.get(i).removeAllCards();
        }
        handsOnTable.clear();
        for (int i = 0; i < 4; i++) { // give out cards
            for (int j = 13 * i; j < 13 * i + 13; j++) {
                playerList.get(i).addCard(this.deck.getCard(j));
                if (this.deck.getCard(j).rank == 2 && this.deck.getCard(j).suit == 0) {
                    currentIdx = i; // mark the first player
                }
            }
            this.playerList.get(i).sortCardsInHand();
        }
        table.reset();
        lastIdx = currentIdx;
        table.printMsg("\n" + playerList.get(currentIdx).getName() + "'s turn:\n");
        table.disable();
        if (currentIdx == playerID) {
            table.enable();
        }
        table.repaint();
    }

    /**
     * Makes a move by the player.
     *
     * @param playerID the playerID of the player who makes the move
     * @param cardIdx  the list of indices
     */
    @Override
    public void makeMove(int playerID, int[] cardIdx) {
        CardGameMessage moveMsg = new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx);
        sendMessage(moveMsg);
    }

    /**
     * Checks the move made by the player.
     *
     * @param playerID the playerID of the player who makes the move
     * @param cardIdx  the list of indices
     */
    @Override
    public void checkMove(int playerID, int[] cardIdx) {
        boolean valid = false; // whether this is a valid move
        boolean pass = false; // whether the player passes
        Hand hand = null;
        CardList cards = playerList.get(playerID).play(cardIdx);
        if (cards == null) { // if no 1card is selected
            if (currentIdx == lastIdx) { // if the player is the last one who played (or the player is the first one)
                //table.printMsg("{Pass}\n");
                table.printMsg("INVALID!\nDO NOT PASS!\n", warning);
            } else {
                table.printMsg("{Pass}\n");
                pass = true;
                valid = true;
            }
        } else {
            hand = composeHand(playerList.get(currentIdx), cards);
            if (hand == null) { // if not a legal combination
                //table.printMsg(cards.toString() + "\n");
                table.printMsg("INVALID!\nSELECT A LEGAL HAND!\n", warning);
            } else if (lastIdx == currentIdx) { // if the player is the last one who played (or the player is the first one)
                if (handsOnTable.size() == 0) { // if this is the first round
                    boolean have = false;
                    for (int i = 0; i < hand.size(); i++) { // check whether the Three of Diamonds is in the hand
                        if (hand.getCard(i).rank == 2 && hand.getCard(i).suit == 0) {
                            have = true;
                            break;
                        }
                    }
                    if (have) { // if the Three of Diamonds is in the hand
                        table.printMsg("{" + hand.getType() + "} " + hand.toString() + "\n");
                        valid = true;
                    } else {
                        //table.printMsg("{" + hand.getType() + "} " + hand.toString() + "\n");
                        table.printMsg("INVALID!\nSELECT \u26663!\n", warning);
                    }
                } else {
                    table.printMsg("{" + hand.getType() + "} " + hand.toString() + "\n");
                    valid = true;
                }
            } else if (hand.beats(handsOnTable.get(handsOnTable.size() - 1))) { // if the hand beats the last hand
                table.printMsg("{" + hand.getType() + "} " + hand.toString() + "\n");
                valid = true;
            } else {
                //table.printMsg("{" + hand.getType() + "} " + hand.toString() + "\n");
                table.printMsg("INVALID!\nSELECT A GREATER HAND!\n", warning);
            }
        }
        if (valid) {
            if (!pass) {
                playerList.get(currentIdx).removeCards(hand);
                handsOnTable.add(hand);
                lastIdx = currentIdx;
            }
            if (endOfGame()) {
                StringBuilder result = new StringBuilder("GAME ENDS!\n");
                currentIdx = -1;
                table.disable();
                table.printMsg("\nGAME ENDS!\n", highlight);
                for (int i = 0; i < 4; i++) {
                    int n = playerList.get(i).getNumOfCards();
                    if (n != 0) {
                        String msg = playerList.get(i).getName() + " has " + n + " cards in hand.\n";
                        table.printMsg(msg, highlight);
                        result.append(msg);
                    } else {
                        String msg = playerList.get(i).getName() + " wins the game.\n";
                        table.printMsg(msg, highlight);
                        result.append(msg);
                    }
                }
                table.printMsg("\n", highlight);
                JOptionPane.showMessageDialog(null, result, "Game Result", JOptionPane.INFORMATION_MESSAGE);
                sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
            } else {
                currentIdx = (currentIdx + 1) % 4;
                if (this.playerID == currentIdx) {
                    table.enable();
                } else {
                    table.disable();
                }
                table.printMsg("\n" + playerList.get(currentIdx).getName() + "'s turn:\n");
            }
        }
        table.resetSelected();
        table.repaint();
    }

    /**
     * Checks for end of game.
     *
     * @return true if the game ends; false otherwise
     */
    @Override
    public boolean endOfGame() {
        int num = 0;
        for (int i = 0; i < 4; i++) {
            if (playerList.get(i).getNumOfCards() == 0) {
                num++;
            }
        }
        if (num == 1) {
            return true;
        }
        return false;
    }

    /**
     * Returns the playerID (index) of the local player.
     *
     * @return the playerID (index) of the local player
     */
    @Override
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Sets the playerID (index) of the local player.
     *
     * @param playerID the playerID (index) of the local player.
     */
    @Override
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * Returns the name of the local player.
     *
     * @return the name of the local player
     */
    @Override
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Sets the name of the local player.
     *
     * @param playerName the name of the local player
     */
    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Returns the IP address of the server.
     *
     * @return the IP address of the server
     */
    @Override
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Sets the IP address of the server.
     *
     * @param serverIP the IP address of the server
     */
    @Override
    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    /**
     * Returns the TCP port of the server.
     *
     * @return the TCP port of the server
     */
    @Override
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Sets the TCP port of the server
     *
     * @param serverPort the TCP port of the server
     */
    @Override
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Makes a network connection to the server.
     */
    @Override
    public synchronized void makeConnection() {
        if (!connected) {
            try {
                sock = new Socket(serverIP, serverPort);
                connected = true;
                oos = new ObjectOutputStream(sock.getOutputStream());
                Thread receiver = new Thread(new ServerHandler());
                receiver.start();
                //sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
                //sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
            } catch (IOException e) {
                table.printMsg("CONNECTION FAILED!\nSERVER NOT FOUND!\n\n", warning);
                //e.printStackTrace();
            } finally {
                for (int i = 0; i < 4; i++) { // remove all the cards from the players
                    playerList.get(i).removeAllCards();
                }
                handsOnTable.clear();
                table.repaint();
            }
        }
    }

    /**
     * Parses the specified message received from the server.
     *
     * @param message the specified message received from the server
     */
    @Override
    public synchronized void parseMessage(GameMessage message) {
        switch (message.getType()) {
            case CardGameMessage.PLAYER_LIST:
                playerID = message.getPlayerID();
                table.setActivePlayer(playerID);
                if (message.getData() != null && message.getData().getClass().getSimpleName().equals("String[]")) {
                    String[] nameList = (String[]) message.getData();
                    for (int i = 0; i < nameList.length; i++) {
                        if (nameList[i] != null) {
                            playerList.get(i).setName(nameList[i]);
                            numOfPlayers++;
                        } else {
                            playerList.get(i).setName("waiting...");
                        }
                    }
                }
                sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, playerName));
                table.repaint();
                break;
            case CardGameMessage.JOIN:
                if (message.getData() != null && message.getData().getClass().getSimpleName().equals("String")) {
                    int id = message.getPlayerID();
                    String name = (String) message.getData();
                    playerList.get(id).setName(name);
                    if (id == playerID) {
                        sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                    }
                }
                numOfPlayers++;
                table.repaint();
                break;
            case CardGameMessage.FULL:
                table.printMsg("CONNECTION FAILED!\nTHE SERVER IS FULL!\n\n", warning);
                connected = false;
                break;
            case CardGameMessage.QUIT:
                int quitId = message.getPlayerID();
                if (message.getData() != null && message.getData().getClass().getSimpleName().equals("String")) {
                    String address = (String) message.getData();
                    table.printMsg("\n" + address + " LOST CONNECTION!\n", warning);
                }
                table.printMsg(playerList.get(quitId).getName() + " left the game!\n\n", warning);
                playerList.get(quitId).setName("waiting...");
                if (!endOfGame()) {
                    table.disable();
                    for (int i = 0; i < 4; i++) { // remove all the cards from the players
                        playerList.get(i).removeAllCards();
                    }
                    handsOnTable.clear();
                    sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
                }
                table.repaint();
                break;
            case CardGameMessage.READY:
                int readyId = message.getPlayerID();
                table.printMsg(playerList.get(readyId).getName() + " is ready!\n", highlight);
                break;
            case CardGameMessage.START:
                if (message.getData() != null && message.getData().getClass().getSimpleName().equals("BigTwoDeck")) {
                    BigTwoDeck deck = (BigTwoDeck) message.getData();
                    start(deck);
                }
                break;
            case CardGameMessage.MOVE:
                if (message.getData() != null && message.getData().getClass().getSimpleName().equals("int[]")) {
                    checkMove(message.getPlayerID(), (int[]) message.getData());
                }
                break;
            case CardGameMessage.MSG:
                if (message.getData() != null && message.getData().getClass().getSimpleName().equals("String")) {
                    table.printChatMsg((String) message.getData());
                }
                break;
        }
    }

    /**
     * Sends the specified message to the server.
     *
     * @param message the specified message to be sent the server
     */
    @Override
    public synchronized void sendMessage(GameMessage message) {
        if (connected) {
            try {
                oos.writeObject(message);
                oos.flush();
                //oos.close();
            } catch (IOException e) {
                e.printStackTrace();
                connected = false;
                table.printMsg("\nCONNECTION LOST!\nTRY RECONNECT IN MENU!\n\n", warning);
                for (int i = 0; i < 4; i++) { // remove all the cards from the players
                    playerList.get(i).removeAllCards();
                }
                handsOnTable.clear();
                table.repaint();
            }
        }
    }

    /**
     * The class is used to handle messages from the server.
     */
    class ServerHandler implements Runnable {
        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            receive();
        }

        /**
         * receive messages
         */
        public synchronized void receive() {
            ObjectInputStream ois = null;
            if (connected) {
                try {
                    ois = new ObjectInputStream(sock.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        if (ois == null) {
                            connected = false;
                            table.printMsg("\nOIS ERROR!\nTRY RECONNECT IN MENU!\n\n", warning);
                            for (int i = 0; i < 4; i++) { // remove all the cards from the players
                                playerList.get(i).removeAllCards();
                            }
                            handsOnTable.clear();
                            table.repaint();
                            break;
                        }
                        Object obj = ois.readObject();
                        CardGameMessage message = (CardGameMessage) obj;
                        parseMessage(message);
                    } catch (IOException | ClassNotFoundException e) {
                        connected = false;
                        table.printMsg("\nCONNECTION LOST!\nTRY RECONNECT IN MENU!\n\n", warning);
                        for (int i = 0; i < 4; i++) { // remove all the cards from the players
                            playerList.get(i).removeAllCards();
                        }
                        handsOnTable.clear();
                        table.repaint();
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    /**
     * a method for creating an instance of BigTwoClient
     *
     * @param args not being used
     */
    public static void main(String[] args) {
        BigTwoClient client;
        String name = JOptionPane.showInputDialog("Enter Your Name:", "Anonymous");
        String ip = JOptionPane.showInputDialog("Enter Server IP:", "127.0.0.1");
        String portString = JOptionPane.showInputDialog("Enter Server Port:", 2396);
        if (name == null) {
            name = "Anonymous";
        }
        if (ip == null || portString == null) {
            client = new BigTwoClient();
        } else {
            int port = Integer.parseInt(portString);
            client = new BigTwoClient(name, ip, port);
        }
    }

    /**
     * a method for returning a valid hand from the specified list of cards of the player
     *
     * @param player the player
     * @param cards  the specified list of cards
     * @return a valid hand
     */
    public static Hand composeHand(CardGamePlayer player, CardList cards) {
        if (!(cards.size() == 1 || cards.size() == 2 || cards.size() == 3 || cards.size() == 5)) {
            return null;
        }
        if (new Single(player, cards).isValid()) {
            return new Single(player, cards);
        } else if (new Pair(player, cards).isValid()) {
            return new Pair(player, cards);
        } else if (new Triple(player, cards).isValid()) {
            return new Triple(player, cards);
        } else if (new StraightFlush(player, cards).isValid()) {
            return new StraightFlush(player, cards);
        } else if (new Straight(player, cards).isValid()) {
            return new Straight(player, cards);
        } else if (new Flush(player, cards).isValid()) {
            return new Flush(player, cards);
        } else if (new FullHouse(player, cards).isValid()) {
            return new FullHouse(player, cards);
        } else if (new Quad(player, cards).isValid()) {
            return new Quad(player, cards);
        } else {
            return null;
        }
    }
}
