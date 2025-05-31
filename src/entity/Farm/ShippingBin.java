package entity.Farm;

import entity.Item.Fish;
import entity.Item.FishData;
import entity.Item.Item;
import entity.Player.Player;
import main.GamePanel;
import main.GameStates;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import objects.GameObject;
import state.EndGameStatistics;

public class ShippingBin extends GameObject {
    private GamePanel gp;
    private static ShippingBinGUI activeGUI = null;
    public static boolean hasUsedTodayBin = false;
    private static int currentDay = 1;
    
    public ShippingBin(GamePanel gp) {
        this.gp = gp;
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
        if (hasUsedTodayBin) {
            JOptionPane.showMessageDialog(null, "You have already used the shipping bin today!\nCome back tomorrow!!!!",  "Already Used Today", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            activeGUI = new ShippingBinGUI(player);
            activeGUI.setVisible(true);
        });
    }
    
    public static void processPendingSales() {
        if (activeGUI != null) {
            activeGUI.processPendingSales();
        }
    }
    
    // public static void markBinAsUsed() {
    //     hasUsedTodayBin = true;
    // }
    
    private double getItemPrice(String itemName) {
        Fish fish = FishData.ALL_FISH.stream().filter(f -> f.getName().equalsIgnoreCase(itemName)).findFirst().orElse(null);
        return fish != null ? fish.calculateSellPrice() : 10;
    }
    
    private class ShippingBinGUI extends JDialog {
        private Player player;
        private JList<String> itemList;
        private DefaultListModel<String> listModel;
        private JList<String> binItemsList; 
        private DefaultListModel<String> binListModel;
        private JLabel goldLabel, totalValueLabel, binSlotsLabel;
        private List<String> sellableItems;
        private Map<Item, Integer> playerItems;
        private Map<String, Integer> binItems;
        private int pendingGold = 0;
        private final int MAX_BIN_SLOTS = 16;

        public ShippingBinGUI(Player player) {
            this.player = player;
            this.playerItems = player.getInventory().getItems();
            this.sellableItems = getSellableItems();
            this.binItems = new HashMap<>();
            setupGUI();
            updateItemList();
            updateLabels();
        }
        
        private void setupGUI() {
            Font vt323;
            try {
                java.io.InputStream is = getClass().getResourceAsStream("/res/Font/VT323-Regular.ttf");
                if (is != null) {
                    vt323 = Font.createFont(Font.TRUETYPE_FONT, is);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(vt323);
                } else {
                    vt323 = new Font(Font.MONOSPACED, Font.PLAIN, 12);
                }
            } catch (Exception e) {
                vt323 = new Font(Font.MONOSPACED, Font.PLAIN, 12);
            }
            
            setTitle("Shipping Bin - Day " + currentDay);
            setSize(600, 500);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    returnItemsToInventory();
                    dispose();
                }
            });
            
            setResizable(false);
            
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(new Color(139, 69, 19));
            
            JLabel headerLabel = new JLabel("Shipping Bin - Add Items to Sell", JLabel.CENTER);
            headerLabel.setFont(vt323.deriveFont(Font.BOLD, 20f));
            headerLabel.setForeground(Color.WHITE);
            mainPanel.add(headerLabel, BorderLayout.NORTH);
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setBackground(new Color(139, 69, 19));
            
            JPanel inventoryPanel = new JPanel(new BorderLayout());
            inventoryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Your Inventory", 
                0, 0, vt323.deriveFont(Font.BOLD, 14f), Color.WHITE));
            inventoryPanel.setBackground(new Color(139, 69, 19));
            
            listModel = new DefaultListModel<>();
            itemList = new JList<>(listModel);
            itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            itemList.setFont(vt323.deriveFont(Font.PLAIN, 12f));
            itemList.setBackground(new Color(245, 245, 220));
            inventoryPanel.add(new JScrollPane(itemList), BorderLayout.CENTER);
            
            JButton addToBinButton = new JButton("Add to Bin");
            addToBinButton.setFont(vt323.deriveFont(Font.BOLD, 12f));
            addToBinButton.addActionListener(e -> addToBin());
            inventoryPanel.add(addToBinButton, BorderLayout.SOUTH);
            
            JPanel binPanel = new JPanel(new BorderLayout());
            binPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE), "Shipping Bin", 
                0, 0, vt323.deriveFont(Font.BOLD, 14f), Color.WHITE));
            binPanel.setBackground(new Color(139, 69, 19));
            
            binListModel = new DefaultListModel<>();
            binItemsList = new JList<>(binListModel);
            binItemsList.setFont(vt323.deriveFont(Font.PLAIN, 12f));
            binItemsList.setBackground(new Color(245, 245, 220));
            binPanel.add(new JScrollPane(binItemsList), BorderLayout.CENTER);
            
            splitPane.setLeftComponent(inventoryPanel);
            splitPane.setRightComponent(binPanel);
            splitPane.setDividerLocation(300);
            
            mainPanel.add(splitPane, BorderLayout.CENTER);
            
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);
            
            JPanel infoPanel = new JPanel(new GridLayout(3, 1));
            infoPanel.setOpaque(false);
            goldLabel = new JLabel("Current Gold: " + player.getGold());
            goldLabel.setFont(vt323.deriveFont(Font.BOLD, 14f));
            goldLabel.setForeground(Color.YELLOW);
            totalValueLabel = new JLabel("Total Value: 0 gold");
            totalValueLabel.setFont(vt323.deriveFont(Font.BOLD, 14f));
            totalValueLabel.setForeground(Color.GREEN);
            binSlotsLabel = new JLabel("Bin Slots: 0/" + MAX_BIN_SLOTS);
            binSlotsLabel.setFont(vt323.deriveFont(Font.BOLD, 14f));
            binSlotsLabel.setForeground(Color.CYAN);
            infoPanel.add(goldLabel);
            infoPanel.add(totalValueLabel);
            infoPanel.add(binSlotsLabel);
            bottomPanel.add(infoPanel, BorderLayout.WEST);
            
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setOpaque(false);
            
            JButton sellAllBinButton = new JButton("Sell All in Bin");
            sellAllBinButton.setFont(vt323.deriveFont(Font.BOLD, 12f));
            sellAllBinButton.addActionListener(e -> sellBinItems());
            
            JButton closeButton = new JButton("Close");
            closeButton.setFont(vt323.deriveFont(Font.BOLD, 12f));
            closeButton.addActionListener(e -> {
                returnItemsToInventory();
                dispose();
            });
            
            buttonPanel.add(sellAllBinButton);
            buttonPanel.add(closeButton);
            bottomPanel.add(buttonPanel, BorderLayout.EAST);
            
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);
            add(mainPanel);
        }
        
        private List<String> getSellableItems() {
            List<String> items = new ArrayList<>();
            for (Map.Entry<Item, Integer> entry : playerItems.entrySet()) {
                String itemName = entry.getKey().getItemName();
                Integer quantity = entry.getValue();
                if (quantity != null && quantity > 0 && getItemPrice(itemName) > 0 && !items.contains(itemName)) {
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
                int quantity = getQuantityByName(itemName);
                double price = getItemPrice(itemName);
                listModel.addElement(String.format("%s x%d (Price: %d each)", 
                    itemName, quantity, (int)price));
            }
        }
        
        public void updateLabels() {
            goldLabel.setText("Current Gold: " + player.getGold());
            
            int totalItemsInBin = binItems.values().stream().mapToInt(Integer::intValue).sum();
            double totalValue = binItems.entrySet().stream()
                .mapToDouble(entry -> getItemPrice(entry.getKey()) * entry.getValue())
                .sum();
            
            String totalValueText = "Total Value: " + (int)totalValue + " gold";
            if (pendingGold > 0) totalValueText += " | Pending: " + pendingGold + " gold";
            totalValueLabel.setText(totalValueText);
            
            binSlotsLabel.setText("Bin Slots: " + totalItemsInBin + "/" + MAX_BIN_SLOTS);
            binSlotsLabel.setForeground(totalItemsInBin >= MAX_BIN_SLOTS ? Color.RED : Color.CYAN);
        }
        
        private int getQuantityByName(String itemName) {
            return playerItems.entrySet().stream()
                .filter(entry -> entry.getKey().getItemName().equals(itemName))
                .mapToInt(Map.Entry::getValue)
                .sum();
        }
        
        private void addToBin() {
            int selectedIndex = itemList.getSelectedIndex();
            if (selectedIndex == -1 || sellableItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select an item to add!", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int totalItemsInBin = binItems.values().stream().mapToInt(Integer::intValue).sum();
            if (totalItemsInBin >= MAX_BIN_SLOTS) {
                JOptionPane.showMessageDialog(this, "Shipping bin is full! (Max: " + MAX_BIN_SLOTS + " items)", "Bin Full", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String selectedItemName = sellableItems.get(selectedIndex);
            int maxQuantity = getQuantityByName(selectedItemName);
            int remainingSlots = MAX_BIN_SLOTS - totalItemsInBin;
            int maxCanAdd = Math.min(maxQuantity, remainingSlots);
            
            String input = JOptionPane.showInputDialog(this, 
                String.format("How many %s to add to bin?\nAvailable: %d\nRemaining slots: %d", 
                selectedItemName, maxQuantity, remainingSlots), "Add to Bin", JOptionPane.QUESTION_MESSAGE);

            if (input == null) return;
            
            try {
                int quantityToAdd = Integer.parseInt(input);
                if (quantityToAdd <= 0 || quantityToAdd > maxCanAdd) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity! Max you can add: " + maxCanAdd, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                removeItemsByName(selectedItemName, quantityToAdd);
                binItems.merge(selectedItemName, quantityToAdd, Integer::sum);
                
                refreshData();
                updateBinDisplay();
                
                JOptionPane.showMessageDialog(this, String.format("Added %dx %s to shipping bin!", quantityToAdd, selectedItemName), "Added to Bin", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void updateBinDisplay() {
            binListModel.clear();
            if (binItems.isEmpty()) {
                binListModel.addElement("Empty ૮(˶╥︿╥)ა");
            } else {
                for (Map.Entry<String, Integer> entry : binItems.entrySet()) {
                    double price = getItemPrice(entry.getKey());
                    binListModel.addElement(String.format("%s x%d (Value: %d gold)", 
                        entry.getKey(), entry.getValue(), (int)(price * entry.getValue())));
                }
            }
        }
        
        private void returnItemsToInventory() {
            if (!binItems.isEmpty()) {
                for (Map.Entry<String, Integer> entry : binItems.entrySet()) {
                    String itemName = entry.getKey();
                    int quantity = entry.getValue();
                    
                    for (Map.Entry<Item, Integer> playerEntry : playerItems.entrySet()) {
                        if (playerEntry.getKey().getItemName().equals(itemName)) {
                            player.getInventory().addItem(playerEntry.getKey(), quantity);
                            break;
                        }
                    }
                }
                binItems.clear();
            }
        }
        
        private void sellBinItems() {
            if (binItems.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No items in bin to sell!", "Empty Bin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int totalItems = binItems.values().stream().mapToInt(Integer::intValue).sum();
            double totalValue = binItems.entrySet().stream()
                .mapToDouble(entry -> getItemPrice(entry.getKey()) * entry.getValue())
                .sum();
            
            if (JOptionPane.showConfirmDialog(this, 
                String.format("Sell %d items for %d gold?\nItems cannot be returned once sold!", 
                totalItems, (int)totalValue), "Confirm Sale", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                
                pendingGold += (int)totalValue;
                binItems.clear(); 
                hasUsedTodayBin = true;
                //markBinAsUsed();
                
                updateBinDisplay();
                updateLabels();
                
                JOptionPane.showMessageDialog(this, String.format("Sold %d items for %d gold!\nGold will be added after sleeping.\nShipping bin used for today!", totalItems, (int)totalValue), "Sale Complete", JOptionPane.INFORMATION_MESSAGE);
                
                dispose(); 
            }
        }
        
        private void removeItemsByName(String itemName, int quantityToRemove) {
            int remaining = quantityToRemove;
            for (Map.Entry<Item, Integer> entry : new ArrayList<>(playerItems.entrySet())) {
                if (entry.getKey().getItemName().equals(itemName) && remaining > 0) {
                    int toRemove = Math.min(remaining, entry.getValue());
                    player.getInventory().removeItem(entry.getKey(), toRemove);
                    remaining -= toRemove;
                }
            }
        }
        
        private void refreshData() {
            playerItems = player.getInventory().getItems();
            sellableItems = getSellableItems();
            updateItemList();
            updateLabels();
        }
        
        public void processPendingSales() {
            if (pendingGold > 0) {
                player.addGold(pendingGold);
                ((EndGameStatistics) gp.stateHandlers.get(GameStates.STATISTICS)).addIncome(pendingGold);
                pendingGold = 0;
                updateLabels();
            }
        }
    }
}
