package com.example.votebe;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddGroupPanel extends JPanel {
    private JTextField groupNameField = new JTextField(10);
    private JButton createGroupButton = new JButton("Create Group");
    private JList<String> groupList = new JList<>(new DefaultListModel<>());
    private JList<MyTag> allTagsList = new JList<>(new DefaultListModel<>());
    private JList<MyTag> selectedTagsList = new JList<>(new DefaultListModel<>());
    private JButton addTagToGroupButton = new JButton("Add >>");

    private MyUser myUser = ApplicationContextProvider.getApplicationContext().getBean(MyUser.class);

    public AddGroupPanel() {
        initializeUI();
        loadGroups();
        loadAllTags();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createGroupPanel(), createTagSelectionPanel());
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        JPanel newGroupPanel = new JPanel(new BorderLayout());
        newGroupPanel.add(createNewGroupPanel(), BorderLayout.NORTH);
        add(newGroupPanel, BorderLayout.SOUTH);
    }

    private JScrollPane createGroupPanel() {
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateSelectedGroupTags(groupList.getSelectedValue());
            }
        });
        return new JScrollPane(groupList);
    }

    private void updateSelectedGroupTags(String groupName) {
        ((DefaultListModel<MyTag>)selectedTagsList.getModel()).clear();
        if (groupName != null) {
            List<MyTag> tags = myUser.getTagsOfGroup(groupName); // Fetch tags for the selected group
            tags.forEach(tag -> ((DefaultListModel<MyTag>)selectedTagsList.getModel()).addElement(tag));
        }
    }

    private JPanel createTagSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(allTagsList), BorderLayout.CENTER);
        panel.add(addTagToGroupButton, BorderLayout.SOUTH);
        addTagToGroupButton.addActionListener(e -> addTagToSelectedGroup());
        return panel;
    }

    private void addTagToSelectedGroup() {
        MyTag selectedTag = allTagsList.getSelectedValue();
        if (selectedTag != null && !((DefaultListModel<MyTag>)selectedTagsList.getModel()).contains(selectedTag)) {
            ((DefaultListModel<MyTag>)selectedTagsList.getModel()).addElement(selectedTag);
        }
    }

    private JPanel createNewGroupPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("New Group Name:"));
        panel.add(groupNameField);
        panel.add(createGroupButton);

        createGroupButton.addActionListener(e -> {
            String groupName = groupNameField.getText().trim();
            List<String> tags = getSelectedTags();
            if (!groupName.isEmpty() && myUser.addGroup(groupName, tags) != -1) {
                ((DefaultListModel<String>)groupList.getModel()).addElement(groupName);
                groupNameField.setText("");
                ((DefaultListModel<MyTag>)selectedTagsList.getModel()).clear();
                JOptionPane.showMessageDialog(this, "Group created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create group.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
    private List<String> getSelectedTags() {
        List<String> tags = new ArrayList<>();
        for (MyTag tag : selectedTagsList.getSelectedValuesList()) { // Properly calling getSelectedValuesList()
            tags.add(tag.tag); // Assuming the MyTag class has a public field or a getter method for 'tag'
        }
        return tags;
    }


    private void loadGroups() {
        List<String> groups = myUser.getGroups(); // Assuming getGroups returns List<String>
        groups.forEach(group -> ((DefaultListModel<String>)groupList.getModel()).addElement(group));
    }

    private void loadAllTags() {
        List<MyTag> tags = myUser.getTags(); // Assuming getTags returns List<MyTag>
        tags.forEach(tag -> ((DefaultListModel<MyTag>)allTagsList.getModel()).addElement(tag));
    }
}

