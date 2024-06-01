package com.example.votebe;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);

    public MainFrame() {
        setTitle("Swing Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        initUI();
    }

    private void initUI() {
        // Adding different panels
        cardPanel.add(new ViewObjectsPanel(), "Objects");
        cardPanel.add(new ViewTagsPanel(), "Tags");
        cardPanel.add(new AddGroupPanel(), "AddGroup");

        // Navigation buttons (example for one, add others similarly)
        JButton btnObjects = new JButton("Objects");
        btnObjects.addActionListener(e -> cardLayout.show(cardPanel, "Objects"));

        JButton btnTags = new JButton("Tags");
        btnTags.addActionListener(e -> cardLayout.show(cardPanel, "Tags"));

        JButton btnAddGroup = new JButton("Add Group");
        btnAddGroup.addActionListener(e -> cardLayout.show(cardPanel, "AddGroup"));

        // Layout for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnObjects);
        buttonPanel.add(btnTags);
        buttonPanel.add(btnAddGroup);

        // Add panels to the frame
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
    }

}



