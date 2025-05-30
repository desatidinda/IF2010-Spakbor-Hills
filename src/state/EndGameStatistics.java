package state;

import main.GamePanel;
import state.StateHandler;
import main.GameStates;
import entity.NPC.*;
import entity.Item.Fish;
import entity.Item.FishType;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class EndGameStatistics implements StateHandler {
    private final GamePanel gp;
    private final Font vt323;

    private int scrollOffset = 0;
    private final int visibleLines = 16;

    private int totalIncome = 0;
    private int totalExpenditure = 0;
    private int totalDaysPlayed = 0;
    private int totalSeasonsPassed = 0;
    private int totalCropsHarvested = 0;
    private int totalFishCaught = 0;

    private final Map<NPC, Integer> npcVisitingFrequency = new HashMap<>();
    private final Map<FishType, Integer> fishStatistics = new HashMap<>();

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

    public void incrementCropsHarvested() {
        totalCropsHarvested++;
    }

    public void incrementTotalFishCaught() {
        totalFishCaught++;
    }

    public void updateFishStatistics(FishType fishType) {
        fishStatistics.put(fishType, fishStatistics.getOrDefault(fishType, 0) + 1);
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

    public int getTotalFishCaught() {
        return totalFishCaught;
    }

    public Map<NPC, Integer> getNpcVisitingFrequency() {
        return npcVisitingFrequency;
    }

    public Map<FishType, Integer> getFishStatistics() {
        return fishStatistics;
    }

    private List<String> getStatisticsLines() {
        List<String> lines = new ArrayList<>();
        lines.add("== END GAME STATISTICS ==");
        lines.add("Total Income     : " + totalIncome);
        lines.add("Total Expenditure: " + totalExpenditure);
        lines.add(String.format("Avg Income/Season: %.2f", getAverageSeasonIncome()));
        lines.add(String.format("Avg Expense/Season: %.2f", getAverageSeasonExpenditure()));
        lines.add("Total Days Played: " + totalDaysPlayed);
        lines.add("Crops Harvested  : " + totalCropsHarvested);
        lines.add("Fish Caught      : " + totalFishCaught);
        lines.add("");
        lines.add("-- NPC Status --");
        for (NPC npc : gp.npc) {
            lines.add(npc.getName() + " - Relationship: " + npc.getRelationshipStatus());
            lines.add("Chat: " + npc.getCountChatting()
                    + ", Gift: " + npc.getCountGifting()
                    + ", Visit: " + npc.getCountVisiting());
        }
        lines.add("");
        lines.add("-- Fish Statistics --");
        for (FishType type : FishType.values()) {
            lines.add(type + ": " + fishStatistics.getOrDefault(type, 0));
        }
        lines.add("");
        lines.add("Press ESC/ENTER to return, ↑/↓ to scroll");
        return lines;
    }

    // === UI ===
    @Override
    public void draw(Graphics2D g2) {
        List<String> lines = getStatisticsLines();
        int totalLines = lines.size();

        int boxWidth = 600;
        int lineHeight = 30;
        int boxHeight = 60 + Math.min(visibleLines, totalLines) * lineHeight;

        int boxX = (gp.screenWidth - boxWidth) / 2;
        int boxY = (gp.screenHeight - boxHeight) / 2;

        gp.ui.drawPopupWindow(g2, boxX, boxY, boxWidth, boxHeight);

        g2.setFont(vt323.deriveFont(Font.BOLD, 24F));
        gp.ui.drawCenteredText(g2, "END GAME STATISTICS", boxX, boxY + 38, boxWidth);

        g2.setFont(vt323.deriveFont(Font.PLAIN, 18F));
        int y = boxY + 70;

        int end = Math.min(scrollOffset + visibleLines, totalLines);
        for (int i = scrollOffset; i < end; i++) {
            gp.ui.drawCenteredText(g2, lines.get(i), boxX, y, boxWidth);
            y += lineHeight;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        List<String> lines = getStatisticsLines();
        int totalLines = lines.size();

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (scrollOffset > 0) scrollOffset--;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (scrollOffset < totalLines - visibleLines) scrollOffset++;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            gp.gameState = GameStates.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
