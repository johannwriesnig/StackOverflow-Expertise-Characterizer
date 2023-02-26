package com.wriesnig.gui;


import com.wriesnig.CharacterizerApplication;
import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.utils.AccountsMatchScorer;
import com.wriesnig.utils.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CharacterizerApplicationGui extends JFrame implements Observer {
    private final JPanel welcomeScreen;
    private JScrollPane usersExpertisesScreen;
    private final JPanel waitScreen;
    private final JPanel pane;
    private final Color backGroundColor = Color.decode("#fcfcfc");

    private ArrayList<Integer> ids;
    private JTextField idsInput;
    private JButton appStartBtn;

    public CharacterizerApplicationGui(){
        super("Characterizer");
        ImageIcon image = new ImageIcon("src/main/resources/src/gui/StackIcon.png");
        this.setIconImage(image.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(900, 550));
        this.setLocationRelativeTo(null);
        this.setResizable(false);


        pane = new JPanel();
        pane.setLayout(new GridLayout(0,1));
        welcomeScreen = getWelcomeScreen();
        waitScreen = getWaitScreen();
        usersExpertisesScreen = new JScrollPane();

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
        constraints.ipadx = 200;
        welcomeScreen.add(idsInput, constraints);

        appStartBtn = new JButton("Start");

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
        boolean isInputCorrect = idsInput.getText().matches("\\d+(,\\d+)*");
        if(!isInputCorrect){

            return;
        }
        //appStartBtn.setEnabled(false);
        pane.remove(welcomeScreen);
        waitScreen.setVisible(true);
        pane.add(waitScreen);
        SwingUtilities.updateComponentTreeUI(pane);

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


        GridBagConstraints constraints = new GridBagConstraints();
        for(User user: users){
            JPanel userPanel = getUserPanel(user);
            usersPanel.add(userPanel);
            constraints.gridy++;

        }

        usersExpertisesScreen = new JScrollPane(usersPanel,  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        usersExpertisesScreen.getVerticalScrollBar().setUnitIncrement(10);
        pane.removeAll();
        usersExpertisesScreen.setVisible(true);
        pane.add(usersExpertisesScreen);
        revalidate();
        repaint();
    }

    public JPanel getUserPanel(User user){
        JPanel userPanel = new JPanel(new GridBagLayout());
        Color borderColor = Color.decode("#DADCE0");
        userPanel.setBackground(backGroundColor);
        userPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,borderColor));
        AccountsMatchScorer accountsMatchScorer = new AccountsMatchScorer();
        //BufferedImage image = accountsMatchScorer.getImageFromUrl(user.getProfileImageUrl());
        String displayName = user.getStackDisplayName();
        String stackLink = user.getStackLink();
        String gitLink = user.getGitLink();

        Color linkColor = Color.decode("#4287f5");
        Color noLinkColor = Color.decode("#d41313");
        JLabel imageLbl = new JLabel(new ImageIcon(new ImageIcon("src/main/resources/src/gui/StackIcon.png").getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT)));
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
                    Desktop.getDesktop().browse(new URI(user.getStackLink()));

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
                        Desktop.getDesktop().browse(new URI(user.getGitLink()));

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
