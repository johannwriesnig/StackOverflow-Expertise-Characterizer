package com.wriesnig.gui;

import com.wriesnig.CharacterizerApplication;
import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.utils.Logger;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class CharacterizerApplicationGui extends JFrame implements Observer {
    private final JPanel welcomeScreen;
    private JScrollPane usersExpertisesScreen;
    private JPanel waitScreen;
    private final JPanel pane;
    private final Color backGroundColor = Color.decode("#fcfcfc");

    private JTextField idsInput;
    private JButton appStartBtn;
    private JButton backToStartBtn;

    public CharacterizerApplicationGui(){
        super("Characterizer");
        ImageIcon image = new ImageIcon("src/main/resources/src/gui/StackIcon.png");
        this.setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 550));
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.setBackground(backGroundColor);
        welcomeScreen = getWelcomeScreen();
        waitScreen = getWaitScreen();
        pane.add(welcomeScreen);

        this.getContentPane().add(pane);
        this.setVisible(true);
    }

    public JPanel getWelcomeScreen(){
        JPanel welcomeScreen = new JPanel();
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
        idsInput.getDocument().addDocumentListener(new DocumentListener() {
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
                boolean isInputCorrect = idsInput.getText().matches("\\d+(,\\d+)*");
                if(!isInputCorrect){
                    appStartBtn.setEnabled(false);
                    return;
                }
                String[] ids = idsInput.getText().split(",");
                String lastId = ids[ids.length-1];
                boolean isInteger=true;
                try{
                    Integer.parseInt(lastId);
                } catch(NumberFormatException e){
                    isInteger=false;
                }
                appStartBtn.setEnabled(isInteger);
            }
        });
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

        return welcomeScreen;
    }

    public void startButtonPressed(){
        appStartBtn.setEnabled(false);
        pane.removeAll();
        waitScreen.setVisible(true);
        pane.add(waitScreen);
        revalidate();
        repaint();

        startApp();
    }

    public void startApp(){
        String[] inputIdsSplit = idsInput.getText().split(",");
        ArrayList<Integer> ids = new ArrayList<>();
        for(String id: inputIdsSplit)
            ids.add(Integer.parseInt(id));

        Thread thread = new Thread(() -> {
            CharacterizerApplication characterizerApplication = new CharacterizerApplication(ids);
            characterizerApplication.addObserver(this);
            characterizerApplication.run();
        });
        thread.start();
    }


    public JPanel getWaitScreen(){
        JPanel waitScreen = new JPanel();
        waitScreen.setBackground(backGroundColor);

        ImageIcon spinner = new ImageIcon(new ImageIcon("src/main/resources/src/gui/Spinner.gif").getImage().getScaledInstance(40,40, Image.SCALE_DEFAULT));
        JLabel infoLbl = new JLabel("this could take some time...", spinner,JLabel.CENTER);
        infoLbl.setFont(new Font(infoLbl.getFont().getName(), Font.PLAIN , 17));
        waitScreen.add(infoLbl);

        return waitScreen;
    }


    @Override
    public void notifyUpdate(ArrayList<User> users) {
        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        usersPanel.setBackground(backGroundColor);

        for(User user: users){
            JPanel userPanel = getUserPanel(user);
            usersPanel.add(userPanel);
        }
        usersExpertisesScreen = new JScrollPane(usersPanel,  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        usersExpertisesScreen.getVerticalScrollBar().setUnitIncrement(10);
        usersExpertisesScreen.setBorder(BorderFactory.createEmptyBorder());
        usersExpertisesScreen.setVisible(true);

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

        pane.removeAll();
        pane.add(usersExpertisesScreen);
        pane.add(btnPanel);
        revalidate();
        repaint();
    }

    public void backBtnPressed(){
        pane.removeAll();
        pane.add(welcomeScreen);
        appStartBtn.setEnabled(true);
        revalidate();
        repaint();
    }

    public JPanel getUserPanel(User user){
        Color borderColor = Color.decode("#DADCE0");
        Color linkColor = Color.decode("#4287f5");
        Color noLinkColor = Color.decode("#d41313");

        JPanel userPanel = new JPanel(new GridBagLayout());
        userPanel.setBackground(backGroundColor);
        userPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,borderColor));
        BufferedImage image = user.getProfileImage();
        ImageIcon profileImageIcon = image!=null?new ImageIcon(image):new ImageIcon("src/main/resources/src/gui/BrokenImageUrl.png");
        String displayName = user.getStackDisplayName();
        String stackLink = user.getStackLink();
        String gitLink = user.getGitLink();


        JLabel imageLbl = new JLabel(new ImageIcon(profileImageIcon.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
        JLabel nameLbl = new JLabel(displayName + "("+user.getStackId()+")");
        nameLbl.setFont(new Font(nameLbl.getFont().getName(), Font.PLAIN , 15));

        ImageIcon stackImage = new ImageIcon(new ImageIcon("src/main/resources/src/gui/StackIcon.png").getImage().getScaledInstance(25,25, Image.SCALE_DEFAULT));
        JLabel stackLinkLbl = new JLabel("StackOverflow ", stackImage, JLabel.CENTER);
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

        JLabel gitLinkLbl = new JLabel("Github", new ImageIcon(new ImageIcon("src/main/resources/src/gui/GitIcon.png").getImage().getScaledInstance(25,25, Image.SCALE_DEFAULT)), JLabel.CENTER);
        gitLinkLbl.setFont(new Font(gitLinkLbl.getFont().getName(), Font.PLAIN , 13));
        gitLinkLbl.setIconTextGap(0);
        gitLinkLbl.setVerticalTextPosition(JLabel.CENTER);

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
        constraints.insets = new Insets(0,0,8,40);
        userPanel.add(gitLinkLbl, constraints);
        constraints.anchor = GridBagConstraints.CENTER;

        HashMap<String, Double> expertise = user.getExpertise().getOverAllExpertise();
        for(String tag: Tags.tagsToCharacterize){
            constraints.insets = new Insets(8,0,0,20);
            JLabel tagLbl = new JLabel(tag);
            tagLbl.setFont(new Font(tagLbl.getFont().getName(), Font.PLAIN , 15));
            constraints.gridy = 0;
            constraints.gridx++;
            userPanel.add(tagLbl, constraints);


            constraints.insets = new Insets(0,0,8,20);
            constraints.gridy = 1;
            double tagExpertise = expertise.get(tag);
            JLabel valueLbl = new JLabel(String.valueOf(tagExpertise));
            tagLbl.setFont(new Font(valueLbl.getFont().getName(), Font.PLAIN , 15));
            userPanel.add(valueLbl, constraints);
        }

        JLabel filler = new JLabel("");
        constraints.gridx++;
        constraints.weightx=1;
        userPanel.add(filler,constraints);

        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) userPanel.getPreferredSize().getHeight()));

        return userPanel;
    }
}
