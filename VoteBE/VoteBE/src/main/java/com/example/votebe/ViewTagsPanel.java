package com.example.votebe;

import javax.swing.*;
import java.awt.*;

class ViewTagsPanel extends JPanel {
    private JList<String> tagList;
    private DefaultListModel<String> tagModel;
    private JTextField tagTextField = new JTextField(20);
    private JButton addTagButton = new JButton("Add Tag");

    private MyUser myUser = ApplicationContextProvider.getApplicationContext().getBean(MyUser.class); // Assume this is being passed or set elsewhere in your application

    public ViewTagsPanel() {

        setLayout(new BorderLayout());
        initializeUI();
        loadTags();
    }

    private void initializeUI() {
        tagModel = new DefaultListModel<>();
        tagList = new JList<>(tagModel);
        JScrollPane scrollPane = new JScrollPane(tagList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Enter new tag:"));
        inputPanel.add(tagTextField);
        inputPanel.add(addTagButton);

        add(inputPanel, BorderLayout.NORTH);

        addTagButton.addActionListener(e -> addNewTag());
    }

    private void loadTags() {
        java.util.List<MyTag> tags = myUser.getTags(); // Assuming MyUser has a method to get all tags
        tagModel.clear();
        for (MyTag tag : tags) {
            tagModel.addElement(tag.tag);
        }
    }

    private void addNewTag() {
        String newTag = tagTextField.getText().trim();
        if (!newTag.isEmpty()) {
            Integer tagId = myUser.addTag(newTag); // Assuming MyUser has a method to add a new tag
            if (tagId != -1) {
                tagModel.addElement(newTag);
                tagTextField.setText(""); // Clear input field on successful addition
                JOptionPane.showMessageDialog(this, "Tag added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add tag. It might already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
