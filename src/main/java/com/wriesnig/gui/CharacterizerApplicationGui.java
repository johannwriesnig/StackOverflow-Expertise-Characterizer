package com.wriesnig.gui;

import com.wriesnig.CharacterizerApplication;
import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.expertise.Expertise;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.utils.Logger;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class CharacterizerApplicationGui extends JFrame implements Observer {
    private static final int HEIGHT = 550;
    private static final int WIDTH = 900;

    private final JPanel pane;
    private JPanel welcomeScreen;
    private JPanel waitScreen;
    private JScrollPane usersScrollPane;

    private final Color backGroundColor = Color.decode("#fcfcfc");
    private final Color borderColor = Color.decode("#DADCE0");
    private final Color linkColor = Color.decode("#4287f5");
    private final Color noLinkColor = Color.decode("#d41313");

    private JButton appStartBtn;
    private JButton backToStartBtn;
    private JPanel hoverExpertisePanel;
    private JLabel stackInfo;
    private JLabel stackInfoValue;
    private JLabel gitInfo;
    private JLabel gitInfoValue;
    private JTextField idsInput;

    public CharacterizerApplicationGui(){
        super("Characterizer");
        ImageIcon image = new ImageIcon("src/main/resources/src/gui/StackIcon.png");
        this.setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setBackground(Color.cyan);

        initWelcomeScreen();
        initWaitScreen();

        pane.add(welcomeScreen);

        this.getContentPane().add(pane);
        this.setVisible(true);
    }

    public void initWelcomeScreen(){
        welcomeScreen = new JPanel();
        welcomeScreen.setLayout(new GridBagLayout());
        welcomeScreen.setBackground(backGroundColor);
        GridBagConstraints constraints = new GridBagConstraints();

        JLabel headLbl = new JLabel("Welcome");
        headLbl.setFont(new Font(headLbl.getFont().getName(), Font.PLAIN , 45));
        constraints.gridy=0;
        constraints.weighty=0.3;
        welcomeScreen.add(headLbl, constraints);

        JLabel infoLbl = new JLabel("Please provide some stackoverflow-ids separated by commas");
        infoLbl.setFont(new Font(infoLbl.getFont().getName(), Font.PLAIN , 16));
        constraints.gridy=1;
        constraints.weighty=0;
        constraints.insets = new Insets(0,0,10,0);
        welcomeScreen.add(infoLbl, constraints);


        idsInput = new JTextField();
        idsInput.setFont(new Font(idsInput.getFont().getName(), Font.PLAIN , 13));
        constraints.gridy=2;
        idsInput.setPreferredSize(new Dimension(250,22));
        idsInput.setMaximumSize(idsInput.getPreferredSize());
        idsInput.setMinimumSize(idsInput.getPreferredSize());
        idsInput.getDocument().addDocumentListener(getUserInputDocumentListener());
        welcomeScreen.add(idsInput, constraints);

        appStartBtn = new JButton("Start");
        appStartBtn.setEnabled(false);
        appStartBtn.addActionListener(e -> startButtonPressed());
        constraints.ipadx = 0;
        constraints.gridy=3;
        welcomeScreen.add(appStartBtn, constraints);

        JLabel footerLbl = new JLabel("By Johann Wriesnig");
        footerLbl.setFont(new Font(footerLbl.getFont().getName(), Font.PLAIN , 11));
        constraints.gridy=4;
        constraints.weighty=1;
        constraints.weightx = 1;
        constraints.insets = new Insets(0,0,10,10);
        constraints.anchor=GridBagConstraints.LAST_LINE_END;
        welcomeScreen.add(footerLbl, constraints);
    }

    //listens to users input
    public DocumentListener getUserInputDocumentListener(){
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStartBtn();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStartBtn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStartBtn();
            }

            public void updateStartBtn(){
                //disables start button if input does not match numbers delimited by commas
                boolean isInputCorrect = idsInput.getText().matches("\\d+(,\\d+)*");
                if(!isInputCorrect){
                    appStartBtn.setEnabled(false);
                    return;
                }
                String[] ids = idsInput.getText().split(",");
                String lastId = ids[ids.length-1];
                boolean isInteger=true;
                //disables button if value to big for integer
                try{
                    Integer.parseInt(lastId);
                } catch(NumberFormatException e){
                    isInteger=false;
                }
                appStartBtn.setEnabled(isInteger);
            }
        };
    }

    public void startButtonPressed(){
        changeViewAfterStartButtonPressed();
        revalidate();
        repaint();
        startApp();
    }

    public void changeViewAfterStartButtonPressed(){
        appStartBtn.setEnabled(false);
        pane.removeAll();
        waitScreen.setVisible(true);
        pane.add(waitScreen);
    }

    public void startApp(){
        ArrayList<Integer> ids = getIdsFromUserInput();

        Thread thread = new Thread(() -> {
            CharacterizerApplication characterizerApplication = new CharacterizerApplication(ids);
            characterizerApplication.addObserver(this);
            characterizerApplication.run();
        });
        thread.start();
    }

    public ArrayList<Integer> getIdsFromUserInput(){
        String[] inputIdsSplit = idsInput.getText().split(",");
        ArrayList<Integer> ids = new ArrayList<>();
        for(String id: inputIdsSplit)
            ids.add(Integer.parseInt(id));
        return ids;
    }


    public void initWaitScreen(){
        waitScreen = new JPanel();
        waitScreen.setBackground(backGroundColor);

        ImageIcon spinner = new ImageIcon(new ImageIcon("src/main/resources/src/gui/Spinner.gif").getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT));
        JLabel infoLbl = new JLabel("this could take some time...", spinner,JLabel.CENTER);
        infoLbl.setFont(new Font(infoLbl.getFont().getName(), Font.PLAIN , 17));
        waitScreen.add(infoLbl);
    }

    public void setExpertiseHoverInfo(String stackExpertise, String gitExpertise){
        gitInfoValue.setText(gitExpertise);
        stackInfoValue.setText(stackExpertise);
    }


    @Override
    public void notifyUpdate(ArrayList<User> users) {
        JPanel usersPanel = getUsersPanel(users);
        usersScrollPane = getUsersScrollPane(usersPanel);
        JPanel backBtnPanel = getBackBtnPanel();
        hoverExpertisePanel = getHoverExpertisePanel();
        JLayeredPane layeredPane = getLayeredPane(usersScrollPane, hoverExpertisePanel);
        pane.removeAll();
        pane.add(layeredPane);
        pane.add(backBtnPanel);
        revalidate();
        repaint();
    }

    public JPanel getUsersPanel(ArrayList<User> users){
        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        usersPanel.setBackground(backGroundColor);

        for(User user: users)
            usersPanel.add(getUserPanel(user));

        return usersPanel;
    }

    //Creates ScrollPane from the usersPanel
    public JScrollPane getUsersScrollPane(JPanel panel){
        JScrollPane scrollPane = new JScrollPane(panel,  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVisible(true);
        return scrollPane;
    }

    public JPanel getBackBtnPanel(){
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridBagLayout());
        btnPanel.setBackground(backGroundColor);
        backToStartBtn = new JButton("Back to start");
        backToStartBtn.addActionListener(e->backBtnPressed());
        backToStartBtn.setMaximumSize(backToStartBtn.getPreferredSize());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10,0,10,0);
        btnPanel.add(backToStartBtn, constraints);
        btnPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) btnPanel.getPreferredSize().getHeight()));
        return btnPanel;
    }

    public JPanel getHoverExpertisePanel(){
        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#e6d4d3"));
        panel.setVisible(false);
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        stackInfo = new JLabel("Stack-Expertise:");
        stackInfo.setFont(new Font(stackInfo.getFont().getName(), Font.PLAIN , 13));
        stackInfo.setSize(stackInfo.getPreferredSize());
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(3,3,0,0);
        panel.add(stackInfo, constraints);

        stackInfoValue = new JLabel("Placeholder");
        stackInfoValue.setFont(new Font(stackInfoValue.getFont().getName(), Font.PLAIN , 13));
        stackInfoValue.setHorizontalAlignment(SwingConstants.LEFT);
        constraints.gridx = 1;
        constraints.anchor=GridBagConstraints.LINE_START;
        constraints.insets = new Insets(3,3,0,3);
        panel.add(stackInfoValue, constraints);

        gitInfo = new JLabel("Git-Expertise:");
        gitInfo.setFont(new Font(gitInfo.getFont().getName(), Font.PLAIN , 13));
        gitInfo.setHorizontalAlignment(SwingConstants.RIGHT);
        constraints.anchor=GridBagConstraints.LINE_END;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(3,3,0,0);
        panel.add(gitInfo, constraints);

        gitInfoValue = new JLabel("Placeholder");
        gitInfoValue.setFont(new Font(gitInfoValue.getFont().getName(), Font.PLAIN , 13));
        gitInfoValue.setBackground(Color.yellow);
        gitInfoValue.setHorizontalTextPosition(SwingConstants.LEFT);
        constraints.anchor=GridBagConstraints.LINE_START;
        constraints.gridx = 1;
        constraints.insets = new Insets(3,3,0,3);
        panel.add(gitInfoValue, constraints);

        panel.setSize(panel.getPreferredSize());
        return panel;
    }

    public JLayeredPane getLayeredPane(JScrollPane bottomComponent,JPanel topComponent){
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                usersScrollPane.setSize(e.getComponent().getSize());
            }
        });

        layeredPane.add(bottomComponent,0,0);
        layeredPane.add(topComponent, 1,0);
        return layeredPane;
    }

    public void backBtnPressed(){
        changeViewAfterBackButtonPressed();
        revalidate();
        repaint();
    }

    public void changeViewAfterBackButtonPressed(){
        pane.removeAll();
        pane.add(welcomeScreen);
        appStartBtn.setEnabled(true);
    }

    public JPanel getUserPanel(User user){
        JPanel userPanel = new JPanel(new GridBagLayout());
        userPanel.setBackground(backGroundColor);
        userPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,borderColor));

        JLabel imageLbl = getImageLabel(user);
        JLabel nameLbl = getNameLabel(user);
        JLabel stackLinkLbl = getStackLinkLabel(user);
        JLabel gitLinkLbl = getGitLinkLabel(user);


        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(8,6,8,10);
        constraints.gridx=0;
        constraints.gridheight = 2;
        userPanel.add(imageLbl, constraints);

        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.insets = new Insets(8,6,0,0);
        constraints.gridx=1;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        userPanel.add(nameLbl, constraints);

        constraints.anchor = GridBagConstraints.PAGE_END;
        constraints.insets = new Insets(0,0,8,0);
        constraints.gridx=1;
        constraints.gridy=1;
        constraints.gridwidth = 1;
        userPanel.add(stackLinkLbl, constraints);

        constraints.gridx=2;
        constraints.gridy=1;
        constraints.insets = new Insets(0,0,8,0);
        userPanel.add(gitLinkLbl, constraints);

        addTagsToPanel(user, constraints, userPanel);

        JLabel filler = new JLabel("");
        constraints.gridx++;
        constraints.weightx=1;
        userPanel.add(filler,constraints);

        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) userPanel.getPreferredSize().getHeight()));
        return userPanel;
    }

    public JLabel getImageLabel(User user){
        BufferedImage image = user.getProfileImage();
        ImageIcon profileImageIcon = image!=null?new ImageIcon(image):new ImageIcon("src/main/resources/src/gui/BrokenImageUrl.png");
        return new JLabel(new ImageIcon(profileImageIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
    }

    public JLabel getNameLabel(User user){
        String displayName = user.getStackDisplayName();
        JLabel nameLbl = new JLabel(displayName + "("+user.getStackId()+")");
        nameLbl.setFont(new Font(nameLbl.getFont().getName(), Font.PLAIN , 15));
        return nameLbl;
    }

    public JLabel getStackLinkLabel(User user){
        ImageIcon stackImage = new ImageIcon(new ImageIcon("src/main/resources/src/gui/StackIcon.png").getImage().getScaledInstance(25,25, Image.SCALE_DEFAULT));
        JLabel stackLinkLbl = new JLabel("StackOverflow ", stackImage, JLabel.CENTER);
        String stackLink = user.getStackLink();

        stackLinkLbl.setForeground(linkColor);
        stackLinkLbl.setFont(new Font(stackLinkLbl.getFont().getName(), Font.PLAIN , 13));
        stackLinkLbl.setIconTextGap(0);
        stackLinkLbl.setVerticalTextPosition(JLabel.CENTER);
        stackLinkLbl.setBorder(new MatteBorder(0,0,0,1,borderColor));
        stackLinkLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        stackLinkLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(stackLink));

                } catch (IOException | URISyntaxException exception) {
                    Logger.error("Failed to open stackoverflow link in browser. ", exception);
                }
            }
        });
        return stackLinkLbl;
    }

    public JLabel getGitLinkLabel(User user){
        JLabel gitLinkLbl = new JLabel("Github", new ImageIcon(new ImageIcon("src/main/resources/src/gui/GitIcon.png").getImage().getScaledInstance(25,25, Image.SCALE_DEFAULT)), JLabel.CENTER);
        gitLinkLbl.setFont(new Font(gitLinkLbl.getFont().getName(), Font.PLAIN , 13));
        gitLinkLbl.setIconTextGap(0);
        gitLinkLbl.setVerticalTextPosition(JLabel.CENTER);

        String gitLink = user.getGitLink();
        if(user.getGitUser() instanceof DefaultGitUser){
            gitLinkLbl.setForeground(noLinkColor);
        } else{
            gitLinkLbl.setForeground(linkColor);
            gitLinkLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            gitLinkLbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        Desktop.getDesktop().browse(new URI(gitLink));

                    } catch (IOException | URISyntaxException exception) {
                        Logger.error("Failed to open github link in browser. ", exception);
                    }
                }
            });
        }
        return gitLinkLbl;
    }

    public void addTagsToPanel(User user, GridBagConstraints constraints, JPanel userPanel){
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridheight=2;
        constraints.insets = new Insets(0,15,0,0);
        HashMap<String, String> expertise = Expertise.getExpertiseAsDescriptions(user.getExpertise().getCombinedExpertise());
        for(String tag: Tags.tagsToCharacterize){
            JPanel tagPanel = new JPanel();
            tagPanel.setLayout(new GridBagLayout());
            GridBagConstraints tagPanelConstraints = new GridBagConstraints();
            tagPanel.setBackground(backGroundColor);
            JLabel tagLbl = new JLabel(tag);
            tagLbl.setFont(new Font(tagLbl.getFont().getName(), Font.PLAIN , 15));
            tagLbl.addMouseListener(getMouseAdapterForHoverInfo(user, tag));
            constraints.gridy = 0;
            constraints.gridx++;
            tagLbl.setHorizontalAlignment(SwingConstants.CENTER);
            tagPanelConstraints.fill=GridBagConstraints.HORIZONTAL;
            tagPanel.add(tagLbl, tagPanelConstraints);

            JLabel tagValueLbl = new JLabel(expertise.get(tag));
            tagValueLbl.setFont(new Font(tagValueLbl.getFont().getName(), Font.BOLD , 13));
            tagValueLbl.setHorizontalAlignment(SwingConstants.CENTER);

            tagPanelConstraints.gridy=1;
            tagPanel.add(tagValueLbl, tagPanelConstraints);
            userPanel.add(tagPanel, constraints);
        }
    }

    public MouseAdapter getMouseAdapterForHoverInfo(User user, String tag){
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Point b = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(b, pane);
                int xOffSet = 10;
                int yOffSet = 5;
                int position = (int) (b.getX() + xOffSet + hoverExpertisePanel.getSize().getWidth());
                if(position<WIDTH)
                    hoverExpertisePanel.setLocation((int) (b.getX() + xOffSet), (int) (b.getY() + yOffSet));
                else
                    hoverExpertisePanel.setLocation((int) (b.getX() - xOffSet - hoverExpertisePanel.getSize().getWidth()), (int) (b.getY() + yOffSet));
                HashMap<String,String> stackExpertise = Expertise.getExpertiseAsDescriptions(user.getExpertise().getStackExpertise());
                HashMap<String,String> gitExpertise = Expertise.getExpertiseAsDescriptions(user.getExpertise().getGitExpertise());
                setExpertiseHoverInfo(stackExpertise.get(tag),gitExpertise.get(tag));
                hoverExpertisePanel.setSize(hoverExpertisePanel.getPreferredSize());
                hoverExpertisePanel.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverExpertisePanel.setVisible(false);
            }
        };
    }
}
