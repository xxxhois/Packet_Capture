package org.example;

import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PacketCaptureGUI {

    private JFrame frame;
    private JComboBox<String> interfaceComboBox;
    private JTextField targetIpField;
    private JTextArea outputArea;
    private PacketCaptureManager manager;
    private List<PcapNetworkInterface> interfaces;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                PacketCaptureGUI window = new PacketCaptureGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public PacketCaptureGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);

        JLabel lblInterface = new JLabel("Network Interface:");
        panel.add(lblInterface);

        interfaceComboBox = new JComboBox<>();
        panel.add(interfaceComboBox);

        JLabel lblTargetIp = new JLabel("Target IP:");
        panel.add(lblTargetIp);

        targetIpField = new JTextField();
        panel.add(targetIpField);
        targetIpField.setColumns(10);

        JButton btnStart = new JButton("Start Capture");
        panel.add(btnStart);

        JButton btnStop = new JButton("Stop Capture");
        panel.add(btnStop);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startCapture();
            }
        });

        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopCapture();
            }
        });

        loadNetworkInterfaces();
    }

    private void loadNetworkInterfaces() {
        try {
            interfaces = Pcaps.findAllDevs();
            if (interfaces == null || interfaces.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No network interfaces found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (PcapNetworkInterface nif : interfaces) {
                String description = nif.getDescription() != null ? nif.getDescription() : "No description available";
                interfaceComboBox.addItem(nif.getName() + " (" + description + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error finding network interfaces.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startCapture() {
        int selectedIndex = interfaceComboBox.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= interfaces.size()) {
            JOptionPane.showMessageDialog(frame, "Invalid network interface selected.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PcapNetworkInterface nif = interfaces.get(selectedIndex);
        String targetIp = targetIpField.getText();

        manager = new PacketCaptureManager(nif, targetIp, outputArea);
        manager.startCapture();
    }

    private void stopCapture() {
        if (manager != null) {
            manager.stopCapture();
        }
    }
}
