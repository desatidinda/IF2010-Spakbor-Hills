package entity.Farm;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import objects.GameObject;
import entity.Player.Player;
import entity.Player.Inventory;

public class ShippingBin extends GameObject {
    public ShippingBin() {
        name = "Shipping Bin";
        collision = true;
        widthInTiles = 3;
        heightInTiles = 2;

        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/ObjectImage/shippingbin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }   
    }
    
    public void sellInventory(Player player) {
        SwingUtilities.invokeLater(() -> {
            new ShippingBinGUI(player).setVisible(true);
        });
    }
    
    private double getItemPrice(String itemName) {
        // switch (itemName.toLowerCase()) {
        //     return 10;
        // }
        return 10;
    }
    
    private class ShippingBinGUI extends JDialog {
        private Player player;
        private JList<String> itemList;
        private DefaultListModel<String> listModel;
        private JLabel goldLabel;
        private JLabel totalValueLabel;
        private List<String> sellableItems;
        private Map<String, Integer> playerItems;
        private Font vt323;

        public ShippingBinGUI(Player player) {
            this.player = player;
            this.playerItems = player.getInventory().getItems();
            this.sellableItems = getSellableItems();
            
            setupGUI();
            updateItemList();
            updateLabels();
        }
        
        private void setupGUI() {
            try {
                java.io.InputStream is = getClass().getResourceAsStream("/res/Font/VT323-Regular.ttf");
                if (is != null) {
                    vt323 = Font.createFont(Font.TRUETYPE_FONT, is);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(vt323);
                }
            } catch (Exception e) {

            }
            
            setTitle("Shipping Bin");
            setSize(500, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setModal(true);
            setResizable(false);
            
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(new Color(139, 69, 19)); 
            
            JLabel headerLabel = new JLabel("Shipping Bin - Sell Items", JLabel.CENTER);
            headerLabel.setFont(vt323.deriveFont(Font.BOLD, 20f));
            headerLabel.setForeground(Color.WHITE);
            mainPanel.add(headerLabel, BorderLayout.NORTH);
            
            JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
            centerPanel.setOpaque(false);
            
            JLabel itemsLabel = new JLabel("Available Items:");
            itemsLabel.setFont(vt323.deriveFont(Font.BOLD, 14f));
            itemsLabel.setForeground(Color.WHITE);
            centerPanel.add(itemsLabel, BorderLayout.NORTH);
            
            listModel = new DefaultListModel<>();
            itemList = new JList<>(listModel);
            itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            itemList.setFont(vt323.deriveFont(Font.PLAIN, 12f));
            itemList.setBackground(new Color(245, 245, 220)); 
            
            JScrollPane scrollPane = new JScrollPane(itemList);
            scrollPane.setPreferredSize(new Dimension(450, 200));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
            
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            
            JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
            bottomPanel.setOpaque(false);
            
            JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            infoPanel.setOpaque(false);
            
            goldLabel = new JLabel("Current Gold: " + player.getGold());
            goldLabel.setFont(vt323.deriveFont(Font.BOLD, 14f));
            goldLabel.setForeground(Color.YELLOW);
            
            totalValueLabel = new JLabel("Total Value: 0 gold");
            totalValueLabel.setFont(vt323.deriveFont(Font.BOLD, 14f));
            totalValueLabel.setForeground(Color.GREEN);
            
            infoPanel.add(goldLabel);
            infoPanel.add(totalValueLabel);
            bottomPanel.add(infoPanel, BorderLayout.WEST);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setOpaque(false);
            
            JButton sellSelectedButton = new JButton("Sell Selected");
            sellSelectedButton.setFont(vt323.deriveFont(Font.BOLD, 12f));
            sellSelectedButton.addActionListener(e -> sellSelectedItem());
            
            JButton sellAllButton = new JButton("Sell All");
            sellAllButton.setFont(vt323.deriveFont(Font.BOLD, 12f));
            sellAllButton.addActionListener(e -> sellAllItems());
            
            JButton closeButton = new JButton("Close");
            closeButton.setFont(vt323.deriveFont(Font.BOLD, 12f));
            closeButton.addActionListener(e -> dispose());
            
            buttonPanel.add(sellSelectedButton);
            buttonPanel.add(sellAllButton);
            buttonPanel.add(closeButton);
            bottomPanel.add(buttonPanel, BorderLayout.EAST);
            
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);
            
            add(mainPanel);
        }
        
        private List<String> getSellableItems() {
            List<String> items = new ArrayList<>();
            
            for (Map.Entry<String, Integer> entry : playerItems.entrySet()) {
                String itemName = entry.getKey();
                Integer quantity = entry.getValue();
                
                if (quantity == null || quantity <= 0 || quantity == -1) {
                    continue;
                }
                
                if (getItemPrice(itemName) > 0) {
                    items.add(itemName);
                }
            }
            
            return items;
        }
        
        private void updateItemList() {
            listModel.clear();
            
            if (sellableItems.isEmpty()) {
                listModel.addElement("No items to sell");
                return;
            }
            
            for (String itemName : sellableItems) {
                int quantity = playerItems.get(itemName);
                double price = getItemPrice(itemName);
                double totalValue = price * quantity;
                String displayText = String.format("%s x%d (Price: %d each | Total: %d gold)", itemName, quantity, (int)price, (int)totalValue);
                listModel.addElement(displayText);
            }
        }
        
        private void updateLabels() {
            goldLabel.setText("Current Gold: " + player.getGold());
            
            double totalValue = 0; 
            for (String itemName : sellableItems) {
                int quantity = playerItems.get(itemName);
                double price = getItemPrice(itemName);
                totalValue += price * quantity;
            }
            totalValueLabel.setText("Total Value: " + (int)totalValue + " gold"); 
        }
        
        private void sellSelectedItem() {
            int selectedIndex = itemList.getSelectedIndex();
            
            if (selectedIndex == -1 || sellableItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an item to sell!", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String selectedItem = sellableItems.get(selectedIndex);
            int maxQuantity = playerItems.get(selectedItem);
            double itemPrice = getItemPrice(selectedItem);
            
            String input = JOptionPane.showInputDialog(this, 
                String.format("How many %s to sell?\nAvailable: %d\nPrice: %d gold each", selectedItem, maxQuantity, (int)itemPrice), "Sell Item", JOptionPane.QUESTION_MESSAGE);
    
            if (input == null) return; 
            
            try {
                int quantityToSell = Integer.parseInt(input);
                
                if (quantityToSell <= 0) {
                    JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (quantityToSell > maxQuantity) {
                    JOptionPane.showMessageDialog(this, "Not enough items!", "Invalid Quantity", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double totalValue = itemPrice * quantityToSell;
                player.getInventory().removeItem(selectedItem, quantityToSell);
                player.addGold((int)totalValue); 
                playerItems = player.getInventory().getItems();
                sellableItems = getSellableItems();
                
                updateItemList();
                updateLabels();
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Sold %dx %s for %d gold!", quantityToSell, selectedItem, (int)totalValue), "Sale Complete", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void sellAllItems() {
            if (sellableItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No items to sell!", "No Items", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to sell all items?", "Confirm Sale", JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                double totalEarnings = 0;
                int itemsSold = 0;
                
                for (String itemName : new ArrayList<>(sellableItems)) {
                    int quantity = playerItems.get(itemName);
                    double itemPrice = getItemPrice(itemName);
                    double itemTotal = itemPrice * quantity;
                    totalEarnings += itemTotal;
                    itemsSold += quantity;
                    player.getInventory().removeItem(itemName, quantity);
                }
                
                player.addGold((int)totalEarnings); 
                playerItems = player.getInventory().getItems();
                sellableItems = getSellableItems();
                
                updateItemList();
                updateLabels();
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Sold %d items for %d gold!", itemsSold, (int)totalEarnings), "Sale Complete", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
