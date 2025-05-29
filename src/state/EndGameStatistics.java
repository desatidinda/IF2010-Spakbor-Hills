package state;

import main.GamePanel;
import state.StateHandler;
import main.GameStates;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class EndGameStatistics implements StateHandler {
    private final GamePanel gp;
    private final Font vt323;

    private int totalIncome = 0;
    private int totalExpenditure = 0;
    private int totalDaysPlayed = 0;
    private int totalSeasonsPassed = 0;
    private int totalCropsHarvested = 0;

    private final Map<String, String> npcRelationshipStatus = new HashMap<>();
    private final Map<String, Integer> npcChattingFrequency = new HashMap<>();
    private final Map<String, Integer> npcGiftingFrequency = new HashMap<>();
    private final Map<String, Integer> npcVisitingFrequency = new HashMap<>();
    private final Map<String, Integer> fishCaught = new HashMap<>();

    public EndGameStatistics(GamePanel gp) {
        this.gp = gp;
        this.vt323 = new Font("VT323", Font.PLAIN, 24);
    }

    // === Methods ===
    public void addIncome(int amount) {
        totalIncome += amount;
    }

    public void addExpenditure(int amount) {
        totalExpenditure += amount;
    }

    public void incrementDaysPlayed() {
        totalDaysPlayed++;
    }

    public void incrementSeason() {
        totalSeasonsPassed++;
    }

    public void incrementCropsHarvested() {
        totalCropsHarvested++;
    }

    public void addFish(String rarity) {
        fishCaught.put(rarity, fishCaught.getOrDefault(rarity, 0) + 1);
    }

    public void setRelationship(String npcName, String status) {
        npcRelationshipStatus.put(npcName, status);
    }

    public void chatWithNPC(String npcName) {
        npcChattingFrequency.put(npcName, npcChattingFrequency.getOrDefault(npcName, 0) + 1);
    }

    public void giftToNPC(String npcName) {
        npcGiftingFrequency.put(npcName, npcGiftingFrequency.getOrDefault(npcName, 0) + 1);
    }

    public void visitNPC(String npcName) {
        npcVisitingFrequency.put(npcName, npcVisitingFrequency.getOrDefault(npcName, 0) + 1);
    }

    // === Getter ===
    public int getTotalIncome() {
        return totalIncome;
    }

    public int getTotalExpenditure() {
        return totalExpenditure;
    }

    public float getAverageSeasonIncome() {
        return totalSeasonsPassed == 0 ? 0 : (float) totalIncome / totalSeasonsPassed;
    }

    public float getAverageSeasonExpenditure() {
        return totalSeasonsPassed == 0 ? 0 : (float) totalExpenditure / totalSeasonsPassed;
    }

    public int getTotalDaysPlayed() {
        return totalDaysPlayed;
    }

    public int getTotalCropsHarvested() {
        return totalCropsHarvested;
    }

    public Map<String, String> getNpcRelationshipStatus() {
        return npcRelationshipStatus;
    }

    public Map<String, Integer> getNpcChattingFrequency() {
        return npcChattingFrequency;
    }

    public Map<String, Integer> getNpcGiftingFrequency() {
        return npcGiftingFrequency;
    }

    public Map<String, Integer> getNpcVisitingFrequency() {
        return npcVisitingFrequency;
    }

    public Map<String, Integer> getFishCaught() {
        return fishCaught;
    }

    // === UI ===
    @Override
    public void draw(Graphics2D g2) {
        String[] lines = {
            "== END GAME STATISTICS ==",
            "Total Income     : " + totalIncome,
            "Total Expenditure: " + totalExpenditure,
            "Avg Income/Season: " + getAverageSeasonIncome(),
            "Avg Expense/Season: " + getAverageSeasonExpenditure(),
            "Total Days Played: " + totalDaysPlayed,
            "Crops Harvested  : " + totalCropsHarvested
        };

        int boxWidth = 600;
        int lineHeight = 30;
        int boxHeight = 60 + (lines.length + getTotalNpcLines()) * lineHeight;

        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;

        gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);

        g2.setFont(vt323.deriveFont(Font.BOLD, 24F));
        gp.ui.drawCenteredText(g2, "END GAME STATISTICS", boxX, boxY + 38, boxWidth);

        g2.setFont(vt323.deriveFont(Font.PLAIN, 18F));
        int y = boxY + 70;
        for (String line : lines) {
            gp.ui.drawCenteredText(g2, line, boxX, y, boxWidth);
            y += lineHeight;
        }

        gp.ui.drawCenteredText(g2, "-- NPC Status --", boxX, y, boxWidth); y += lineHeight;

        for (String npc : npcRelationshipStatus.keySet()) {
            gp.ui.drawCenteredText(g2, npc + " - Relationship: " + npcRelationshipStatus.get(npc), boxX, y, boxWidth);
            y += lineHeight;
            gp.ui.drawCenteredText(g2, "Chat: " + npcChattingFrequency.getOrDefault(npc, 0)
                                            + ", Gift: " + npcGiftingFrequency.getOrDefault(npc, 0)
                                            + ", Visit: " + npcVisitingFrequency.getOrDefault(npc, 0),
                                   boxX, y, boxWidth);
            y += lineHeight;
        }

        gp.ui.drawCenteredText(g2, "-- Fish Caught --", boxX, y, boxWidth); y += lineHeight;
        for (Map.Entry<String, Integer> entry : fishCaught.entrySet()) {
            gp.ui.drawCenteredText(g2, entry.getKey() + ": " + entry.getValue(), boxX, y, boxWidth);
            y += lineHeight;
        }
    }

    private int getTotalNpcLines() {
        int npcCount = npcRelationshipStatus.size();
        return npcCount * 2 + 2 + fishCaught.size();
    }

    @Override
    public void update() {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameStates.MAP;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
