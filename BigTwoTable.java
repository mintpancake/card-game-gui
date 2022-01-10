import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.*;

/**
 * The BigTwoTable class is used to build a GUI for the Big Two card game and handle all user actions.
 *
 * @author Lu Meng
 * @version 1.0
 * @see CardGameTable
 */
public class BigTwoTable implements CardGameTable {
    /**
     * a constructor for creating a BigTwoTable.
     *
     * @param game a card game associates with this table
     */
    public BigTwoTable(CardGame game) {
        this.game = game;
        selected = new boolean[13];
        focus = new boolean[13];
        activePlayer = -1;
        frame = new JFrame();
        bigTwoPanel = new BigTwoPanel();
        bigTwoPanel.addMouseListener(new BigTwoPanel());
        bigTwoPanel.addMouseMotionListener(new BigTwoPanel());
        playButton = new JButton("Play");
        playButton.addActionListener(new PlayButtonListener());
        passButton = new JButton("Pass");
        passButton.addActionListener(new PassButtonListener());
//        msgArea = new JTextArea();
        msgArea = new JTextPane();
        chatBox = new JTextPane();
        label = new JLabel();
        textField = new JTextField();
        textField.addActionListener(new TextFieldListener());
        cardImages = new Image[4][13];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                cardImages[i][j] = new ImageIcon("src/" + i + "_" + j + ".png").getImage();
            }
        }
        cardBackImage = new ImageIcon("src/blue_back.png").getImage();
        faces = new Image[4];
        for (int i = 0; i < 4; i++) {
            faces[i] = new ImageIcon("src/player_" + i + ".png").getImage();
        }
        avatars = new Image[4];
        System.arraycopy(faces, 0, avatars, 0, 4);
        emotions = new Image[9];
        emotions[0] = new ImageIcon("src/wow.png").getImage();
        emotions[1] = new ImageIcon("src/grimacing.png").getImage();
        emotions[2] = new ImageIcon("src/dizzy.png").getImage();
        emotions[3] = new ImageIcon("src/angry.png").getImage();
        emotions[4] = new ImageIcon("src/grinning.png").getImage();
        emotions[5] = new ImageIcon("src/crying.png").getImage();
        emotions[6] = new ImageIcon("src/triumph.png").getImage();
        emotions[7] = new ImageIcon("src/persevering.png").getImage();
        emotions[8] = new ImageIcon("src/sleep.png").getImage();

        theme = new Color[6];
        theme[0] = new Color(34, 51, 70);
        theme[1] = new Color(67, 101, 139);
        theme[2] = new Color(78, 137, 174);
        theme[3] = new Color(50, 76, 105);
        theme[4] = new Color(182, 205, 224);
        theme[5] = new Color(236, 242, 247);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(null);
        menuBar.setBackground(theme[3]);
        JMenu menu = new JMenu("Menu");
        menu.setBackground(theme[3]);
        menu.setForeground(theme[4]);
        menu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        JMenuItem connect = new JMenuItem("Connect");
        connect.addActionListener(new ConnectMenuItemListener());
        connect.setBorder(null);
        connect.setBackground(theme[3]);
        connect.setForeground(theme[4]);
        connect.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new QuitMenuItemListener());
        quit.setBorder(null);
        quit.setBackground(theme[3]);
        quit.setForeground(theme[4]);
        quit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        menu.add(connect);
        menu.add(quit);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        bigTwoPanel.setBackground(theme[1]);
        bigTwoPanel.setLayout(new GridBagLayout());
        frame.add(bigTwoPanel);

        playButton.setBackground(theme[3]);
        playButton.setForeground(theme[5]);
        playButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        playButton.setBorderPainted(false);
        playButton.setFocusable(false);
        passButton.setBackground(theme[3]);
        passButton.setForeground(theme[5]);
        passButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        passButton.setBorderPainted(false);
        passButton.setFocusable(false);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(745, 0, 0, 15);
        bigTwoPanel.add(playButton, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_END;
        c.insets = new Insets(745, 15, 0, 0);
        bigTwoPanel.add(passButton, c);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(theme[1]);
        rightPanel.setBorder(null);
        rightPanel.setPreferredSize(new Dimension(450, 828)); //828
        rightPanel.setLayout(new GridBagLayout());
        frame.add(rightPanel, BorderLayout.EAST);

        msgArea.setEditable(false);
        msgArea.setBackground(theme[2]);
        //msgArea.setLineWrap(true);
        //msgArea.setForeground(theme[5]);
        //msgArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        StyledDocument doc = msgArea.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, theme[5]);
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontFamily(set, Font.SANS_SERIF);
        doc.setParagraphAttributes(0, doc.getLength(), set, false);
        StyleConstants.setFontSize(set, 32);
        StyleConstants.setBold(set, true);
        try {
            doc.insertString(doc.getLength(), "Console\n\n", set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        JScrollPane scroller = new JScrollPane(msgArea);
        scroller.setBorder(null);
        scroller.setPreferredSize(new Dimension(445, 383));//450,414  411
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getVerticalScrollBar().setBackground(theme[2]);
        scroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = theme[1];
            }
        });
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 3, 5);
        c.gridwidth = 2;
        rightPanel.add(scroller, c);

        chatBox.setEditable(false);
        chatBox.setBackground(theme[2]);
        //chatBox.setLineWrap(true);
        //chatBox.setForeground(theme[5]);
        //chatBox.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        doc = chatBox.getStyledDocument();
        set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, theme[5]);
        StyleConstants.setFontFamily(set, Font.SANS_SERIF);
        doc.setCharacterAttributes(0, doc.getLength(), set, false);
        StyleConstants.setFontSize(set, 32);
        StyleConstants.setBold(set, true);
        try {
            doc.insertString(doc.getLength(), "                Chat Box\n\n", set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        JScrollPane chatBoxScroller = new JScrollPane(chatBox);
        chatBoxScroller.setBorder(null);
        chatBoxScroller.setPreferredSize(new Dimension(445, 383)); //450,389
        chatBoxScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatBoxScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        chatBoxScroller.getVerticalScrollBar().setBackground(theme[2]);
        chatBoxScroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = theme[1];
            }
        });
        c.gridy = 1;
        c.insets = new Insets(2, 0, 0, 5);
        c.gridwidth = 2;
        rightPanel.add(chatBoxScroller, c);

        label.setText("Message:");
        label.setForeground(theme[5]);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        c = new GridBagConstraints();
        c.gridy = 2;
        rightPanel.add(label, c);

        textField.setColumns(25);
        textField.setBorder(null);
        textField.setBackground(theme[4]);
        textField.setForeground(theme[3]);
        textField.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        c.gridx = 1;
        c.insets = new Insets(12, 0, 10, 0);
        rightPanel.add(textField, c);

        frame.setIconImage(cardBackImage);
        frame.setTitle("Big Two (by Lu Meng)");
        frame.setSize(1200, 900);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CardGame game;
    private boolean[] selected;
    private boolean[] focus;
    private int activePlayer;
    private JFrame frame;
    private JPanel bigTwoPanel;
    private JButton playButton;
    private JButton passButton;
    //    private JTextArea msgArea;
    private JTextPane msgArea;
    private JTextPane chatBox;
    private JLabel label;
    private JTextField textField;
    private Image[][] cardImages;
    private Image cardBackImage;
    private Image[] avatars;
    private Image[] faces;
    private Image[] emotions;
    private Color[] theme;
    private boolean disabled;

    /**
     * Sets the index of the active player (i.e., the current player).
     *
     * @param activePlayer an int value representing the index of the active player
     */
    @Override
    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    /**
     * Returns an array of indices of the cards selected.
     *
     * @return an array of indices of the cards selected
     */
    @Override
    public int[] getSelected() {
        int n = 0;
        for (boolean s : selected) {
            if (s) {
                n++;
            }
        }
        int[] cardIdx = new int[n];
        int idx = 0;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                cardIdx[idx] = i;
                idx++;
            }
        }

        return cardIdx;
    }

    /**
     * Resets the list of selected cards to an empty list.
     */
    @Override
    public void resetSelected() {
        for (int i = 0; i < 13; i++) {
            selected[i] = false;
        }
    }

    /**
     * Repaints the GUI.
     */
    @Override
    public void repaint() {
        frame.repaint();
    }

    /**
     * Prints the specified string to the message area of the card game table.
     *
     * @param msg the string to be printed to the message area of the card game
     *            table
     */
    @Override
    public void printMsg(String msg) {
//        msgArea.setForeground(theme[4]);
//        msgArea.append(msg);
//        msgArea.setCaretPosition(msgArea.getText().length());
        StyledDocument doc = msgArea.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, theme[4]);
        StyleConstants.setFontSize(set, 20);
        try {
            doc.insertString(doc.getLength(), msg, set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        msgArea.setCaretPosition(doc.getLength());
    }

    /**
     * Prints the specified string to the message area of the card game table with
     * a specific color.
     *
     * @param msg   the string to be printed to the message area of the card game table
     * @param color the color with which the string to be printed
     */
    public void printMsg(String msg, Color color) {
//        msgArea.setForeground(theme[4]);
//        msgArea.append(msg);
//        msgArea.setCaretPosition(msgArea.getText().length());
        StyledDocument doc = msgArea.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, color);
        StyleConstants.setFontSize(set, 20);
        try {
            doc.insertString(doc.getLength(), msg, set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        msgArea.setCaretPosition(doc.getLength());
    }

    /**
     * Prints the specified string to the chat box of the card game table.
     *
     * @param msg the string to be printed to the chat box of the card game table
     */
    public void printChatMsg(String msg) {
        StyledDocument doc = chatBox.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setForeground(set, theme[5]);
        StyleConstants.setFontSize(set, 20);
        try {
            doc.insertString(doc.getLength(), msg + "\n", set);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        chatBox.setCaretPosition(doc.getLength());
    }

    /**
     * Clears the message area of the card game table.
     */
    @Override
    public void clearMsgArea() {
//        msgArea.setForeground(theme[0]);
//        msgArea.setText("");
//        msgArea.setCaretPosition(msgArea.getText().length());
        msgArea.setText(null);
        StyledDocument doc = msgArea.getStyledDocument();
        SimpleAttributeSet set = new SimpleAttributeSet();
        StyleConstants.setFontSize(set, 32);
        StyleConstants.setBold(set, true);
        try {
            doc.insertString(doc.getLength(), "Console\n\n", set);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        msgArea.setCaretPosition(doc.getLength());
    }

    /**
     * Resets the GUI.
     */
    @Override
    public void reset() {
        System.arraycopy(faces, 0, avatars, 0, 4);
        this.resetSelected();
        //this.clearMsgArea();
        this.enable();
    }

    /**
     * Enables user interactions.
     */
    @Override
    public void enable() {
        playButton.setEnabled(true);
        passButton.setEnabled(true);
        disabled = false;
//        if (bigTwoPanel.getMouseListeners().length == 0) {
//            bigTwoPanel.addMouseListener(new BigTwoPanel());
//        }
//        if (bigTwoPanel.getMouseMotionListeners().length == 0) {
//            bigTwoPanel.addMouseMotionListener(new BigTwoPanel());
//        }
    }

    /**
     * Disables user interactions.
     */
    @Override
    public void disable() {
        playButton.setEnabled(false);
        passButton.setEnabled(false);
        disabled = true;
//        if (bigTwoPanel.getMouseListeners().length != 0) {
//            for (MouseListener ml : bigTwoPanel.getMouseListeners()) {
//                bigTwoPanel.removeMouseListener(ml);
//            }
//        }
//        if (bigTwoPanel.getMouseMotionListeners().length != 0) {
//            for (MouseMotionListener mml : bigTwoPanel.getMouseMotionListeners()) {
//                bigTwoPanel.removeMouseMotionListener(mml);
//            }
//        }
    }

    /**
     * The BigTwoPanel class is used to model a table and handle all the mouse events.
     */
    class BigTwoPanel extends JPanel implements MouseListener, MouseMotionListener {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            g2.setColor(theme[2]);
            g2.setStroke(new BasicStroke(5));
            for (int i = 0; i < 4; i++) {
                g2.drawLine(0, 150 + 150 * i, 1000, 150 + 150 * i);
            }

            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            g2.setColor(theme[4]);
            for (int i = 0; i < 4; i++) {
                if (i == activePlayer) {
                    g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    g2.setColor(theme[5]);
                } else {
                    g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                    g2.setColor(theme[4]);
                }
                g2.drawString(game.getPlayerList().get(i).getName(), 10, 23 + 150 * i);

                if (game.endOfGame()) {
                    int idx = 5;
                    for (int j = 0; j < 4; j++) {
                        if (game.getPlayerList().get(j).getNumOfCards() == 0) {
                            avatars[j] = emotions[4];
                            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 70));
                            g2.setColor(theme[5]);
                            g2.drawString("WINNER!", 150, 110 + j * 150);
                            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
                        } else {
                            avatars[j] = emotions[idx++];
                        }
                    }
                } else {
                    for (int j = 0; j < 4; j++) {
                        if (Objects.equals(game.getPlayerList().get(j).getName(), "waiting...")) {
                            avatars[j] = emotions[8];
                        }
                    }
                }
                g2.drawImage(avatars[i], 10, 35 + 150 * i, this);

                for (int j = 0; j < game.getPlayerList().get(i).getNumOfCards(); j++) {
                    Image cardImage;
                    int offset;
                    if (game.endOfGame()) {
                        Card card = game.getPlayerList().get(i).getCardsInHand().getCard(j);
                        cardImage = cardImages[card.getSuit()][card.getRank()];
                        offset = 0;
                    } else {
                        if (i == activePlayer) {
                            Card card = game.getPlayerList().get(i).getCardsInHand().getCard(j);
                            cardImage = cardImages[card.getSuit()][card.getRank()];
                            offset = selected[j] ? -22 : (focus[j] ? -7 : 0);
                        } else {
                            cardImage = cardBackImage;
                            offset = 0;
                        }
                    }
                    g2.drawImage(cardImage, 140 + 40 * j, 32 + 150 * i + offset, this);
                }
            }

            g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
            g2.setColor(theme[4]);
            if (game.getHandsOnTable().size() == 0) {
                g2.drawString("Hand on Table (Empty)", 10, 23 + 150 * 4);
            } else {
                Hand lastHand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
                g2.drawString("Hand on Table (Played by " + lastHand.getPlayer().getName() + ")", 10, 23 + 150 * 4);
                for (int j = 0; j < lastHand.size(); j++) {
                    Card card = lastHand.getCard(j);
                    g2.drawImage(cardImages[card.getSuit()][card.getRank()], 20 + 40 * j, 42 + 150 * 4, this);
                }
            }
        }

        /**
         * Invoked when the mouse button has been clicked (pressed
         * and released) on a component.
         *
         * @param e
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (!disabled && activePlayer != -1) {
                int x = e.getX();
                int y = e.getY();
                int n = game.getPlayerList().get(activePlayer).getNumOfCards();
                for (int i = 0; i < n; i++) {
                    int width = i == n - 1 ? 69 : 40;
                    int offset = -22;
                    if (i == n - 1) {
                        if (selected[i]
                                && (x >= 140 + 40 * i
                                && x < 140 + 40 * i + width
                                && y >= 32 + 150 * activePlayer + offset
                                && y <= 32 + 150 * activePlayer + 106 + offset)) {
                            selected[i] = !selected[i];
                        } else if (!selected[i]
                                && (x >= 140 + 40 * i
                                && x < 140 + 40 * i + width
                                && y >= 32 + 150 * activePlayer
                                && y <= 32 + 150 * activePlayer + 106)) {
                            selected[i] = !selected[i];
                        }
                    } else {
                        if (selected[i] && selected[i + 1]
                                && (x >= 140 + 40 * i
                                && x < 140 + 40 * i + width
                                && y >= 32 + 150 * activePlayer + offset
                                && y <= 32 + 150 * activePlayer + 106 + offset)) {
                            selected[i] = !selected[i];
                        } else if (selected[i] && !selected[i + 1]
                                && ((x >= 140 + 40 * i
                                && x < 140 + 40 * i + width
                                && y >= 32 + 150 * activePlayer + offset
                                && y <= 32 + 150 * activePlayer + 106 + offset)
                                || (x >= 140 + 40 * i + 40
                                && x <= 140 + 40 * i + width + 29
                                && y >= 32 + 150 * activePlayer + offset
                                && y < 32 + 150 * activePlayer))) {
                            selected[i] = !selected[i];
                        } else if (!selected[i] && !selected[i + 1]
                                && (x >= 140 + 40 * i
                                && x < 140 + 40 * i + width
                                && y >= 32 + 150 * activePlayer
                                && y <= 32 + 150 * activePlayer + 106)) {
                            selected[i] = !selected[i];
                        } else if (!selected[i] && selected[i + 1]
                                && ((x >= 140 + 40 * i
                                && x < 140 + 40 * i + width
                                && y >= 32 + 150 * activePlayer
                                && y <= 32 + 150 * activePlayer + 106)
                                || (x >= 140 + 40 * i + 40
                                && x <= 140 + 40 * i + width + 29
                                && y > 32 + 150 * activePlayer + 106 + offset
                                && y <= 32 + 150 * activePlayer + 106))) {
                            selected[i] = !selected[i];
                        }
                    }
                }
            }

            Point[] centers = new Point[4];
            for (int i = 0; i < 4; i++) {
                centers[i] = new Point(60, 85 + 150 * i);
            }
            for (int i = 0; i < 4; i++) {
                if (centers[i].distance(e.getPoint()) <= 50) {
                    avatars[i] = emotions[new Random().nextInt(2) + 2];
                }
            }
            BigTwoTable.this.repaint();
        }

        /**
         * Invoked when a mouse button has been pressed on a component.
         *
         * @param e
         */
        @Override
        public void mousePressed(MouseEvent e) {
        }

        /**
         * Invoked when a mouse button has been released on a component.
         *
         * @param e
         */
        @Override
        public void mouseReleased(MouseEvent e) {
        }

        /**
         * Invoked when the mouse enters a component.
         *
         * @param e
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * Invoked when the mouse exits a component.
         *
         * @param e
         */
        @Override
        public void mouseExited(MouseEvent e) {
        }

        /**
         * Invoked when a mouse button is pressed on a component and then
         * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
         * delivered to the component where the drag originated until the
         * mouse button is released (regardless of whether the mouse position
         * is within the bounds of the component).
         * <p>
         * Due to platform-dependent Drag&amp;Drop implementations,
         * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
         * Drag&amp;Drop operation.
         *
         * @param e
         */
        @Override
        public void mouseDragged(MouseEvent e) {
        }

        /**
         * Invoked when the mouse cursor has been moved onto a component
         * but no buttons have been pushed.
         *
         * @param e
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            if (!disabled && activePlayer != -1) {
                int x = e.getX();
                int y = e.getY();
                int n = 0;
                if (activePlayer != -1) {
                    n = game.getPlayerList().get(activePlayer).getNumOfCards();
                }
                for (int i = 0; i < n; i++) {
                    if (!selected[i]) {
                        int width = i == n - 1 ? 69 : 40;
                        int selectedOffset = -22;
                        int focusOffset = -7;
                        if (i == n - 1) {
                            if (!focus[i]
                                    && x >= 140 + 40 * i
                                    && x <= 140 + 40 * i + width
                                    && y >= 32 + 150 * activePlayer
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset) {
                                focus[i] = !focus[i];
                                BigTwoTable.this.repaint();
                            } else if (focus[i]
                                    && !(x >= 140 + 40 * i
                                    && x <= 140 + 40 * i + width
                                    && y >= 32 + 150 * activePlayer
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset)) {
                                focus[i] = !focus[i];
                                BigTwoTable.this.repaint();
                            }
                        } else {
                            if (!focus[i] && !selected[i + 1]
                                    && (x >= 140 + 40 * i
                                    && x < 140 + 40 * i + width
                                    && y >= 32 + 150 * activePlayer
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset)) {
                                focus[i] = !focus[i];
                                BigTwoTable.this.repaint();
                            } else if (!focus[i] && selected[i + 1]
                                    && ((x >= 140 + 40 * i
                                    && x < 140 + 40 * i + width
                                    && y >= 32 + 150 * activePlayer
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset)
                                    || (x >= 140 + 40 * i + 40
                                    && x <= 140 + 40 * i + width + 29
                                    && y > 32 + 150 * activePlayer + 106 + selectedOffset
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset))) {
                                focus[i] = !focus[i];
                                BigTwoTable.this.repaint();
                            } else if (focus[i] && !selected[i + 1]
                                    && !(x >= 140 + 40 * i
                                    && x <= 140 + 40 * i + width
                                    && y >= 32 + 150 * activePlayer
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset)) {
                                focus[i] = !focus[i];
                                BigTwoTable.this.repaint();
                            } else if (focus[i] && selected[i + 1]
                                    && !((x >= 140 + 40 * i
                                    && x < 140 + 40 * i + width
                                    && y >= 32 + 150 * activePlayer
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset)
                                    || (x >= 140 + 40 * i + 40
                                    && x <= 140 + 40 * i + width + 29
                                    && y > 32 + 150 * activePlayer + 106 + selectedOffset
                                    && y <= 32 + 150 * activePlayer + 106 + focusOffset))) {
                                focus[i] = !focus[i];
                                BigTwoTable.this.repaint();
                            }
                        }
                    } else {
                        focus[i] = false;
                    }
                }
            }

            Point[] centers = new Point[4];
            for (int i = 0; i < 4; i++) {
                centers[i] = new Point(60, 85 + 150 * i);
            }
            for (int i = 0; i < 4; i++) {
                if (centers[i].distance(e.getPoint()) <= 50) {
                    if (avatars[i].equals(faces[i])) {
                        avatars[i] = emotions[new Random().nextInt(2)];
                        BigTwoTable.this.repaint();
                    }
                } else {
                    if (!avatars[i].equals(faces[i])) {
                        avatars[i] = faces[i];
                        BigTwoTable.this.repaint();
                    }
                }
            }
        }
    }

    /**
     * The PlayButtonListener class is used to handle the click event of the play button.
     */
    class PlayButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getSelected().length != 0) {
                game.makeMove(activePlayer, getSelected());
            } else {
                printMsg("INVALID!\nSELECT A HAND!\n", new Color(250, 233, 81));
            }
            BigTwoTable.this.repaint();
        }
    }

    /**
     * The PassButtonListener class is used to handle the click event of the pass button.
     */
    class PassButtonListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeMove(activePlayer, new int[0]);
            BigTwoTable.this.repaint();
        }
    }

    /**
     * The PassButtonListener class is used to handle the click event of the pass button.
     */
    class TextFieldListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getClass().getSimpleName().equals("BigTwoClient")) {
                BigTwoClient onlineGame = (BigTwoClient) game;
                if (textField.getText() != null && !Objects.equals(textField.getText(), "")) {
                    onlineGame.sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, textField.getText()));
                    textField.setText("");
                }
            }
        }
    }

    /**
     * The ConnectMenuItemListener class is used to handle the click event of the menu item connect.
     */
    class ConnectMenuItemListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getClass().getSimpleName().equals("BigTwoClient")) {
                BigTwoClient onlineGame = (BigTwoClient) game;
                onlineGame.makeConnection();
            }
        }
    }

    /**
     * The QuitMenuItemListener class is used to handle the click event of the menu item quit.
     */
    class QuitMenuItemListener implements ActionListener {
        /**
         * Invoked when an action occurs.
         *
         * @param e
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
