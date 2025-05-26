package state;

import entity.Player.Inventory;
import entity.Player.Player;
import main.GamePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.Map;

public class InventoryState extends JFrame implements StateHandler {
    private GamePanel gp;
    private Player player;
    private final Inventory inventory;
    private JTable table;
    private String highlightItemName = null;    
    private final Color TEXT_COLOR = new Color(101, 67, 33);          // ini coklat
    private final Color TEXT_COLOR_COLUMN = new Color(255, 255, 255); // ini putih
    private Font vt323;  

    public InventoryState(GamePanel gp, Player player) {
        this(gp, player, null);
    }

    public InventoryState(GamePanel gp, Player player, String highlightItemName) {
        this.gp = gp;
        this.player = player;
        this.inventory = player.getInventory();
        this.highlightItemName = highlightItemName;
        
        try {
            InputStream is = getClass().getResourceAsStream("/res/Font/VT323-Regular.ttf");
            vt323 = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(vt323);
        } catch (Exception e) {
            
        }
        
        setTitle(player.getName() + "'s Inventory");
        setSize(500, 575);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        ImageIcon bgIcon = new ImageIcon(getClass().getResource("/res/bgInventory.png"));
        JLabel background = new JLabel(bgIcon);
        background.setLayout(new BorderLayout());
        setContentPane(background);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        background.add(mainPanel, BorderLayout.CENTER);

        ImageIcon icon = null;
        try {
            icon = new ImageIcon(getClass().getResource("/res/item/Inventory.png"));
        } catch (Exception e) { 
        
        }
        JLabel headerLabel = new JLabel(player.getName() + "'s Inventory", icon, JLabel.CENTER);
        headerLabel.setFont(vt323.deriveFont(Font.BOLD, 24f));
        headerLabel.setForeground(TEXT_COLOR_COLUMN);
        headerLabel.setOpaque(false);
        headerLabel.setHorizontalTextPosition(JLabel.RIGHT);
        headerLabel.setIconTextGap(16);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(35, 10, 0, 10));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        table = new JTable() {
            @Override public boolean isOpaque() { return false; }
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                ((JComponent)c).setOpaque(false);
                c.setForeground(TEXT_COLOR_COLUMN);
                c.setFont(vt323.deriveFont(Font.PLAIN, 18f)); 
                return c;
            }
        };
        table.setOpaque(false);
        table.setShowGrid(false);
        table.setRowHeight(34);
        table.setFillsViewportHeight(true);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        JTableHeader th = table.getTableHeader();
        th.setOpaque(false);
        th.setFont(vt323.deriveFont(Font.BOLD, 20f));
        th.setForeground(TEXT_COLOR);
        th.setReorderingAllowed(false);
        th.setResizingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        ((DefaultTableCellRenderer)th.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(400, 320));
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.add(scrollPane);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); 

        JPanel buttonPanel = new JPanel(); 
        buttonPanel.setOpaque(false);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(vt323.deriveFont(Font.BOLD, 16f));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(closeButton);
        footerPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        updateInventoryDisplay();
        setVisible(true);
    }

    public void updateInventoryDisplay() {
        Map<String, Integer> items = inventory.getItems();
        String[] columnNames = {"Item Name", "Quantity"};
        Object[][] data;
        if (items.isEmpty()) {
            data = new Object[][] { {"Inventory is empty", "-"} };
        } else {
            data = new Object[items.size()][2];
            int i = 0;
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                data[i][0] = entry.getKey();
                data[i][1] = entry.getValue();
                i++;
            }
        }
        table.setModel(new javax.swing.table.DefaultTableModel(
                data,
                columnNames
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    @Override
    public void update() {
        
    }

    @Override
    public void draw(Graphics2D g2) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void showInventory(Player player) {
        SwingUtilities.invokeLater(() -> {
            new InventoryState(null, player);
        });
    }

    public static void showInventory(Player player, String highlightItemName) {
        SwingUtilities.invokeLater(() -> {
            new InventoryState(null, player, highlightItemName);
        });
    }
}
