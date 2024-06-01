package com.example.votebe;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Component
public class ViewObjectsPanel extends JPanel {
    private JList<MyObject> objectList;
    private DefaultListModel<MyObject> model;
    private JButton addButton = new JButton("Add Object");
    private JButton refreshButton = new JButton("Refresh");
    private JTextField searchField = new JTextField(20);

    @Autowired
    private MyUser myUser = ApplicationContextProvider.getApplicationContext().getBean(MyUser.class);

    public ViewObjectsPanel() {
        setLayout(new BorderLayout());

        // Search and refresh panel
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Object Name:"));
        searchPanel.add(searchField);
        searchPanel.add(refreshButton);
        add(searchPanel, BorderLayout.NORTH);

        // List setup
        model = new DefaultListModel<>();
        objectList = new JList<>(model);
        objectList.setCellRenderer(new ObjectListCellRenderer());
        add(new JScrollPane(objectList), BorderLayout.CENTER);

        // Add object button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Event handlers
        refreshButton.addActionListener(this::handleRefresh);
        addButton.addActionListener(this::handleAddObject);
    }

    private void handleRefresh(ActionEvent e) {
        String name = searchField.getText();
        refreshObjectList(name);
    }

    private void refreshObjectList(String name) {
        model.clear();
        List<MyObject> objects = myUser.getObjectsByName(name);  // Make sure this method is implemented in MyUser
        for (MyObject obj : objects) {
            model.addElement(obj);
        }
    }

    private void handleAddObject(ActionEvent e) {
        // Fields for object data entry
        JTextField nameField = new JTextField(20);
        JTextField descField = new JTextField(20);
        JTextField typeField = new JTextField(20);
        JTextField tagsField = new JTextField(20);
        JTextField imageURLField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Tags (comma-separated):"));
        panel.add(tagsField);
        panel.add(new JLabel("Image URL:"));
        panel.add(imageURLField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Object", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            MyObject newObj = new MyObject();
            newObj.name = nameField.getText();
            newObj.description = descField.getText();
            newObj.type = typeField.getText();
            newObj.imageURL = imageURLField.getText();
            newObj.tag = new ArrayList<>(List.of(tagsField.getText().split(",")));
            newObj.thumbs = 0;  // Default thumbs value

            String response = myUser.addObject(newObj);
            JOptionPane.showMessageDialog(this, response);
            refreshObjectList(searchField.getText());
        }
    }

    class ObjectListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof MyObject) {
                MyObject obj = (MyObject) value;
                setText(obj.name + " - " + obj.description+"    "+obj.tag);
            }
            return this;
        }
    }
}
