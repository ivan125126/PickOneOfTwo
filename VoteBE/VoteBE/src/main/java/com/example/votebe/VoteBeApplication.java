package com.example.votebe;

import javax.swing.SwingUtilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;

@SpringBootApplication
public class VoteBeApplication {

    public static void main(String[] args) {
        // Start Spring Boot application and keep the context
        ConfigurableApplicationContext context = SpringApplication.run(VoteBeApplication.class, args);

        // Start Swing Application
        if (!GraphicsEnvironment.isHeadless()) {
            // Run GUI code
            SwingUtilities.invokeLater(() -> {
                // Access Spring-managed beans from the context
                MainFrame ex = new MainFrame();
                ex.setVisible(true);
            });
        } else {
            // Fallback or headless handler
            System.out.println("Running in headless mode. No GUI will be displayed.");
        }

        // Add a shutdown hook to clean up resources properly
        Runtime.getRuntime().addShutdownHook(new Thread(context::close));
    }
}
