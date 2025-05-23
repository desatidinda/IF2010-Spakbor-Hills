package controller;

import entity.Item.Fish;
import entity.Item.FishType;
import entity.Player.Player;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import main.GameClock;

public class FishingManager {

    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static void startFishing(Player player, List<Fish> fishPool) {
        System.out.println(player.getName() + " mulai memancing...");

        // Kurangi energi
        if (!player.performAction(5)) {
            System.out.println("Tidak cukup energi untuk memancing.");
            return;
        }

        // Filter ikan yang tersedia berdasarkan GameClock
        List<Fish> candidates = fishPool.stream()
            .filter(Fish::isAvailable)
            .toList();

        if (candidates.isEmpty()) {
            System.out.println("Tidak ada ikan yang tersedia saat ini.");
            return;
        }

        // Pilih ikan secara acak
        Fish selectedFish = candidates.get(random.nextInt(candidates.size()));
        System.out.println("Sebuah ikan berhasil terpancing...");

        // Tentukan range tebak angka berdasarkan tipe ikan
        int maxNumber = switch (selectedFish.getType()) {
            case COMMON -> 10;
            case REGULAR -> 100;
            case LEGENDARY -> 500;
        };

        int targetNumber = random.nextInt(maxNumber) + 1;
        int maxTries = selectedFish.getType() == FishType.LEGENDARY ? 7 : 10;

        System.out.println("Tebak angka antara 1 dan " + maxNumber + " (" + maxTries + " percobaan)");

        boolean success = false;
        for (int attempt = 1; attempt <= maxTries; attempt++) {
            System.out.print("Percobaan " + attempt + ": ");
            int guess = scanner.nextInt();

            if (guess == targetNumber) {
                success = true;
                break;
            } else {
                if (guess < targetNumber) {
                    System.out.println("Terlalu rendah!");
                } else {
                    System.out.println("Terlalu tinggi!");
                }
            }
        }

        if (success) {
            System.out.println("Selamat! Kamu berhasil menebak angkanya.");
            player.getInventory().addItem(selectedFish.getName());
            System.out.println("Selamat! Kamu menangkap: " + selectedFish.getName());
        } else {
            System.out.println("Sayang sekali! Ikan tersebut lepas...");
            System.out.println("Angka yang benar adalah " + targetNumber);
        }

        // Tambahkan waktu (simulasi 1 detik real = 30 menit game)
        GameClock.updateTime(1);
        System.out.println("Memancing selesai. Waktu sekarang: " + GameClock.getFormattedTime());
    }
}
