package state;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

import main.GamePanel;
import main.GameClock;
import entity.Item.*;
import entity.NPC.*;
import entity.Player.Player;
import entity.Player.Inventory;

public class NPCHouseState extends InsideHouseState {
    private final GamePanel gp;
    private NPC npcInHouse;
    private boolean showInteractPopup = false;
    private boolean showChoicePopup = false;
    private boolean showDialogPopup = false;
    private String dialogText = "";

    private boolean showGiftPopup = false;
    private String giftText = "";
    private boolean showGiftSelectPopup = false;
    private List<Item> giftableItems = new ArrayList<>();
    private int selectedGiftIndex = 0;

    private int popupStartHour = 0;
    private int popupStartMinute = 0;
    private final int popupDuration = 10;

    private int selectedChoice = 0;
    private final String[] choices;
    private boolean showStorePopup = false;
    private List<Item> storeItems = new ArrayList<>();
    private int[] storeBuyAmount;
    private int storeSelectedIndex = 0;
    private final Set<String> purchasedOneTimeItems = new HashSet<>();
    private final Set<String> oneTimeItems = Set.of("Proposal Ring", "Fish n’ Chips Recipe", "Fish Sandwich Recipe");
    private final int VISIBLE_STORE_ITEMS = 5;
    private int storeScrollOffset = 0;
    private BufferedImage storeFrameImage;
    private String localPopupMessage = null;
    private long localPopupTime = 0;
    private static final long LOCAL_POPUP_DURATION = 7;

    public NPCHouseState(GamePanel gp, NPC npc) {
        super(gp);
        this.gp = gp;
        this.npcInHouse = npc;
        this.choices = npc.getName().equals("Emily")
                ? new String[]{"Chat", "Give Gift", "Propose", "Marry", "Store", "Cancel"}
                : new String[]{"Chat", "Give Gift", "Propose", "Marry", "Cancel"};
        npcInHouse.worldX = gp.player.houseX + gp.tileSize * 2 - 16;
        npcInHouse.worldY = gp.player.houseY - gp.tileSize * 3;
        super.loadBackground();
        super.deployFurniture();
        if (npc != null && "Emily".equals(npc.getName())) {
            List<Item> regular = new ArrayList<>();
            List<Item> oneTime = new ArrayList<>();

            for (Item item : entity.Item.SeedsData.ALL_SEEDS) {
                if (item instanceof entity.Item.Seeds s && s.getBuyPrice() > 0) regular.add(s);
            }
            for (Item item : entity.Item.CropsData.ALL_CROPS) {
                if (item instanceof entity.Item.Crops c && c.getBuyPrice() > 0) regular.add(c);
            }
            for (Item item : entity.Item.FoodData.ALL_FOOD) {
                if (item instanceof entity.Item.Food f && f.getBuyPrice() > 0) regular.add(f);
            }
            for (Item item : entity.Item.MiscData.ALL_MISC) {
                if (item instanceof entity.Item.Misc m) {
                    if (m.getBuyPrice() > 0 && !oneTimeItems.contains(m.getItemName())) regular.add(m);
                    else if (oneTimeItems.contains(m.getItemName())) oneTime.add(m);
                }
            }

            this.storeItems.addAll(regular);
            this.storeItems.addAll(oneTime);
            this.storeBuyAmount = new int[storeItems.size()];
        }
    }

    private void showLocalPopup(String msg) {
        localPopupMessage = msg;
        localPopupTime = System.currentTimeMillis();
    }

    {
        try {
            storeFrameImage = ImageIO.read(getClass().getResourceAsStream("/res/store.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawStorePopup(Graphics2D g2) {
        int popupWidth = 520;
        int popupHeight = 350;
        int popupX = gp.screenWidth / 2 - popupWidth / 2;
        int popupY = gp.screenHeight / 2 - popupHeight / 2;

        if (storeFrameImage != null) {
            g2.drawImage(storeFrameImage, popupX, popupY, popupWidth, popupHeight, null);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
        FontMetrics fm = g2.getFontMetrics();

        int slotHeight = 57;
        int slotStartX = popupX + 30;
        int slotPriceX = popupX + popupWidth / 2;
        int slotQtyX = popupX + popupWidth - 60;
        int slotStartY = popupY + 22;

        int start = storeScrollOffset;
        int end = Math.min(start + VISIBLE_STORE_ITEMS, storeItems.size());

        for (int i = start; i < end; i++) {
            int relY = i - start;
            int y = slotStartY + relY * slotHeight;

            if (i == storeSelectedIndex) {
                g2.setColor(new Color(255, 255, 180));
                g2.fillRoundRect(popupX + 16, y + 5, popupWidth - 32, slotHeight - 10, 10, 10);
            }

            Item item = storeItems.get(i);
            String name = item.getItemName();
            double price = 0;
            if (item instanceof Seeds s) price = s.getBuyPrice();
            else if (item instanceof Food f) price = f.getBuyPrice();
            else if (item instanceof Misc m) price = m.getBuyPrice();
            else if (item instanceof Crops c) price = c.getBuyPrice();

            String priceText = (int) price + "g";
            String qtyText = "[" + storeBuyAmount[i] + "]";

            boolean isOneTime = oneTimeItems.contains(name);
            boolean isPurchased = purchasedOneTimeItems.contains(name);

            if (isOneTime && isPurchased) {
                g2.setColor(Color.GRAY);
            } else {
                g2.setColor(Color.BLACK);
            }
            g2.drawString(name, slotStartX, y + 30);
            g2.drawString(priceText, slotPriceX - fm.stringWidth(priceText) / 2, y + 30);
            g2.drawString(qtyText, slotQtyX, y + 30);
        }

        int total = 0;
        if (storeSelectedIndex >= 0 && storeSelectedIndex < storeItems.size()) {
            Item sel = storeItems.get(storeSelectedIndex);
            int amt = storeBuyAmount[storeSelectedIndex];
            if (sel instanceof Seeds s) total = (int) (s.getBuyPrice() * amt);
            else if (sel instanceof Food f) total = (int) (f.getBuyPrice() * amt);
            else if (sel instanceof Misc m) total = (int) (m.getBuyPrice() * amt);
            else if (sel instanceof Crops c) total = (int) (c.getBuyPrice() * amt);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16F));
        g2.setColor(Color.BLACK);
        g2.drawString("Gold: " + (int) gp.player.getGold(), popupX + 40, popupY + popupHeight - 10);

        int boxW = 120, boxH = 30;
        int boxX = popupX + popupWidth - boxW - 15;
        int boxY = popupY + popupHeight - 10;

        g2.setColor(new Color(255, 235, 180));
        g2.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.setColor(new Color(120, 80, 40));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(boxX, boxY, boxW, boxH, 10, 10);
        g2.setColor(Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14F));
        g2.drawString("Total: " + total + "g", boxX + 15, boxY + 20);

        int instrX = popupX + 20;
        int instrY = popupY + popupHeight + 30;

        g2.setFont(new Font("Serif", Font.BOLD, 15));
        g2.drawString("← / →  : Kurangi / Tambah Jumlah", instrX, instrY);
        g2.drawString("↑ / ↓  : Pilih Item", instrX, instrY + 20);
        g2.drawString("ENTER  : Beli", instrX, instrY + 40);
        g2.drawString("ESC    : Keluar", instrX, instrY + 60);

        if (localPopupMessage != null) {
            g2.setFont(new Font("VT323", Font.BOLD, 20));
            FontMetrics msgFm = g2.getFontMetrics();
            int msgWidth = msgFm.stringWidth(localPopupMessage);
            int msgHeight = msgFm.getHeight();

            int boxPadding = 10;
            int boxWidth = msgWidth + boxPadding * 2;
            int boxHeight = msgHeight + boxPadding;

            int boX = gp.screenWidth / 2 - boxWidth / 2;
            int boY = popupY + popupHeight + 70;

            g2.setColor(new Color(255, 235, 180));
            g2.fillRoundRect(boX, boY, boxWidth, boxHeight, 10, 10);

            g2.setColor(new Color(120, 80, 40));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(boX, boY, boxWidth, boxHeight, 10, 10);

            g2.setColor(Color.RED);
            int msgX = gp.screenWidth / 2 - msgWidth / 2;
            int msgY = boY + boxHeight / 2 + msgFm.getAscent() / 2 - 2;
            g2.drawString(localPopupMessage, msgX, msgY);

            if (System.currentTimeMillis() - localPopupTime > LOCAL_POPUP_DURATION) {
                localPopupMessage = null;
            }
        }
    }

    @Override
    public void update() {
        gp.player.update();

        gp.player.collision = false;
        gp.cm.checkNPCCollision(gp.player, npcInHouse);
        showInteractPopup = gp.player.collision;

        if (showDialogPopup || showGiftPopup) {
            int elapsed = (GameClock.getHour() - popupStartHour) * 60 + (GameClock.getMinute() - popupStartMinute);
            if (elapsed < 0) elapsed += 24 * 60;
            if (elapsed >= popupDuration) {
                if (showDialogPopup)showDialogPopup = false;
                if (showGiftPopup)showGiftPopup = false;

            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        if (npcInHouse != null) {
            npcInHouse.draw(g2);
        }
        if (showStorePopup && npcInHouse.getName().equals("Emily")) {
            drawStorePopup(g2);
            return;
        }
        int popupWidth = 400;
        int popupHeight = showChoicePopup ? 210 : 80;
        int popupX = gp.screenWidth / 2 - popupWidth / 2;
        int popupY = gp.screenHeight - popupHeight - 40;

        if (showDialogPopup) {
            int dialogWidth = 500;
            int dialogHeight = 80;
            int dialogX = gp.screenWidth / 2 - dialogWidth / 2;
            int dialogY = gp.screenHeight/2;
            gp.ui.drawPopupWindow(g2, dialogX, dialogY, dialogWidth, dialogHeight);
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, npcInHouse.getName() + " says:", dialogX, dialogY + 30, dialogWidth);
            gp.ui.drawCenteredText(g2, dialogText, dialogX, dialogY + 50, dialogWidth);
        } else if (showGiftPopup) {
            gp.ui.drawPopupWindow(g2, popupX, popupY, popupWidth, popupHeight - 30);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18F));
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, giftText, popupX, popupY + 30, popupWidth);
        } else if (showGiftSelectPopup) {
            popupY = gp.screenHeight - (30 + giftableItems.size() * 28) - 50;
            gp.ui.drawPopupWindow(g2, popupX, popupY, popupWidth, (popupHeight - 30) + giftableItems.size() * 28);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, "Choose gift for " + npcInHouse.getName() + ":", popupX, popupY + 30, popupWidth);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            for (int i = 0; i < giftableItems.size(); i++) {
                int yy = popupY + 60 + i * 28;
                if (i == selectedGiftIndex) {
                    g2.setColor(new Color(255, 215, 0));
                    gp.ui.drawCenteredText(g2, "> " + giftableItems.get(i).getItemName(), popupX, yy, popupWidth);
                } else {
                    g2.setColor(Color.WHITE);
                    gp.ui.drawCenteredText(g2, giftableItems.get(i).getItemName(), popupX, yy, popupWidth);
                }
            }
        } else if (showChoicePopup) {
            gp.ui.drawPopupWindow(g2, popupX, popupY, popupWidth, popupHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(java.awt.Color.WHITE);
            gp.ui.drawCenteredText(g2, "Choose interaction:", popupX, popupY + 35, popupWidth);

            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 18F));
            for (int i = 0; i < choices.length; i++) {
                int y = popupY + 70 + i * 28;
                if (i == selectedChoice) {
                    g2.setColor(new Color(255, 215, 0));
                    gp.ui.drawCenteredText(g2, "> " + choices[i], popupX, y, popupWidth);
                } else {
                    g2.setColor(Color.WHITE);
                    gp.ui.drawCenteredText(g2, choices[i], popupX, y, popupWidth);
                }
            }
        } else if (showInteractPopup) {
            gp.ui.drawPopupWindow(g2, popupX, popupY, popupWidth, popupHeight);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20F));
            g2.setColor(Color.WHITE);
            gp.ui.drawCenteredText(g2, "Press SPACE to interact with " + npcInHouse.getName(), popupX, popupY + 45, popupWidth);
        } else if (choices[selectedChoice].equals("Store") && npcInHouse.getName().equals("Emily")) {
            showChoicePopup = false;
            showStorePopup = true;
            storeSelectedIndex = 0;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (showInteractPopup && !showChoicePopup && key == KeyEvent.VK_SPACE) {
            showChoicePopup = true;
            selectedChoice = 0;
        } else if (showChoicePopup) {
            if (key == KeyEvent.VK_UP) {
                selectedChoice = (selectedChoice + choices.length - 1) % choices.length;
            } else if (key == KeyEvent.VK_DOWN) {
                selectedChoice = (selectedChoice + 1) % choices.length;
            } else if (key == KeyEvent.VK_ENTER) {
                if (choices[selectedChoice].equals("Chat")) {
                    dialogText = npcInHouse.chat();
                    gp.player.chatWith(npcInHouse);
                    showChoicePopup = false;
                    showDialogPopup = true;
                    popupStartHour = GameClock.getHour();
                    popupStartMinute = GameClock.getMinute();
                } else if (choices[selectedChoice].equals("Give Gift")) {
                    giftableItems.clear();
                    for (Map.Entry<Item, Integer> entry : gp.player.getInventory().getItems().entrySet()) {
                        Item item = entry.getKey();
                        if (!gp.player.getInventory().isUnlimitedTool(item) && entry.getValue() > 0) {
                            giftableItems.add(item);
                        }
                    }
                    if (giftableItems.isEmpty()) {
                        giftText = "You don't have any item to give!";
                        showGiftPopup = true;
                        showChoicePopup = false;
                        popupStartHour = GameClock.getHour();
                        popupStartMinute = GameClock.getMinute();
                    } else {
                        showGiftSelectPopup = true;
                        selectedGiftIndex = 0;
                        showChoicePopup = false;
                    }
                } else if (choices[selectedChoice].equals("Store") && npcInHouse.getName().equals("Emily")) {
                    showChoicePopup = false;
                    showStorePopup = true;
                    storeSelectedIndex = 0;
                }
            } else if (key == KeyEvent.VK_ESCAPE || choices[selectedChoice].equals("Cancel")) {
                showChoicePopup = false;
            }
        } else if (showGiftSelectPopup) {
            if (key == KeyEvent.VK_UP) {
                selectedGiftIndex = (selectedGiftIndex + giftableItems.size() - 1) % giftableItems.size();
            } else if (key == KeyEvent.VK_DOWN) {
                selectedGiftIndex = (selectedGiftIndex + 1) % giftableItems.size();
            } else if (key == KeyEvent.VK_ENTER) {
                Item selectedGift = giftableItems.get(selectedGiftIndex);
                boolean success = gp.player.giveGift(npcInHouse, selectedGift.getItemName());
                if (success) {
                    gp.player.getInventory().removeItem(selectedGift);
                    npcInHouse.reactToGift(selectedGift);
                    gp.player.performAction(5);
                    giftText = "You gave " + selectedGift.getItemName() + " to " + npcInHouse.getName() + "!";
                } else {
                    giftText = "You don't have " + selectedGift.getItemName() + "!";
                }
                showGiftPopup = true;
                showGiftSelectPopup = false;
                popupStartHour = GameClock.getHour();
                popupStartMinute = GameClock.getMinute();
            } else if (key == KeyEvent.VK_ESCAPE) {
                showGiftSelectPopup = false;
                showChoicePopup = true;
            }
        } else if (showStorePopup) {
            if (key == KeyEvent.VK_UP) {
                storeSelectedIndex = (storeSelectedIndex - 1 + storeItems.size()) % storeItems.size();
                if (storeSelectedIndex < storeScrollOffset) {
                    storeScrollOffset = storeSelectedIndex;
                }
            } else if (key == KeyEvent.VK_DOWN) {
                storeSelectedIndex = (storeSelectedIndex + 1) % storeItems.size();
                if (storeSelectedIndex >= storeScrollOffset + VISIBLE_STORE_ITEMS) {
                    storeScrollOffset = storeSelectedIndex - VISIBLE_STORE_ITEMS + 1;
                }
            } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_MINUS) {
                if (storeBuyAmount[storeSelectedIndex] > 0) {
                    storeBuyAmount[storeSelectedIndex]--;
                }
            } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_EQUALS) {
                Item item = storeItems.get(storeSelectedIndex);
                String name = item.getItemName();
                boolean isOneTime = oneTimeItems.contains(name);
                if (isOneTime) {
                    storeBuyAmount[storeSelectedIndex] = 1;
                } else {
                    storeBuyAmount[storeSelectedIndex]++;
                }
            } else if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_SPACE) {
                Item item = storeItems.get(storeSelectedIndex);
                String name = item.getItemName();
                int amount = storeBuyAmount[storeSelectedIndex];
                int total = 0;

                if (item instanceof Seeds s) total = (int) (s.getBuyPrice() * amount);
                else if (item instanceof Food f) total = (int) (f.getBuyPrice() * amount);
                else if (item instanceof Misc m) total = (int) (m.getBuyPrice() * amount);
                else if (item instanceof Crops c) total = (int) (c.getBuyPrice() * amount);

                boolean isPurchased = purchasedOneTimeItems.contains(name);
                boolean isOneTime = oneTimeItems.contains(name);

                if (isOneTime && isPurchased) {
                    showLocalPopup("Item ini hanya bisa dibeli sekali.");
                } else if (amount == 0) {
                    showLocalPopup("Pilih jumlah dulu!");
                } else if (gp.player.useGold(total)) {
                    Item newItem = ItemFactory.createItem(name);
                    gp.player.getInventory().addItem(newItem, amount);
                    if (isOneTime) purchasedOneTimeItems.add(name);
                    showLocalPopup("Berhasil beli " + amount + " " + name);
                    storeBuyAmount[storeSelectedIndex] = 0;
                } else {
                    showLocalPopup("Uangmu tidak cukup!");
                }
            } else if (key == KeyEvent.VK_ESCAPE) {
                showStorePopup = false;
                showChoicePopup = true;
    }
    } else {
        // movement, teleport, dll
        super.keyPressed(e);
    }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    public NPC getNpcInHouse() {
        return npcInHouse;
    }
}
