package inventory;
import items.Rarity;

import java.io.*;
import java.util.*;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Inventory instance;

    private Map<String, Map<Rarity, Map<Integer, Integer>>> map;

    private Inventory() {
        map = new HashMap<>();
    }

    protected Object readResolve() {
        return getInstance();
    }


    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }


    public boolean addItem(String name, Rarity rarity, int level, boolean isUpgrade) {
        if (!isUpgrade && map.containsKey(name)) {
            Map<Rarity, Map<Integer, Integer>> rarityMap = map.get(name);

            if (!rarityMap.containsKey(rarity)) {

                System.out.println("Error: Cannot create item with name '" + name +
                        "' and rarity '" + rarity.getDisplayName() + "'.");
                System.out.println("Only items with existing rarities can be created for this name.");
                return false;
            }
        }

        map
                .computeIfAbsent(name, k -> new HashMap<>())
                .computeIfAbsent(rarity, k -> new HashMap<>())
                .merge(level, 1, Integer::sum);

        return true;
    }

    public boolean removeItems(String name, Rarity rarity, int level, int count) {
        if (!map.containsKey(name)) return false;

        Map<Rarity, Map<Integer, Integer>> rarityMap = map.get(name);
        if (!rarityMap.containsKey(rarity)) return false;

        Map<Integer, Integer> levelMap = rarityMap.get(rarity);
        if (!levelMap.containsKey(level)) return false;

        int currentCount = levelMap.get(level);
        if (currentCount < count) return false;

        if (currentCount == count) {
            levelMap.remove(level);
            if (levelMap.isEmpty()) {
                rarityMap.remove(rarity);
                if (rarityMap.isEmpty()) {
                    map.remove(name);
                }
            }
        } else {
            levelMap.put(level, currentCount - count);
        }

        return true;
    }

    public int getItemCount(String name, Rarity rarity, int level) {
        if (!map.containsKey(name)) return 0;

        Map<Rarity, Map<Integer, Integer>> rarityMap = map.get(name);
        if (!rarityMap.containsKey(rarity)) return 0;

        Map<Integer, Integer> levelMap = rarityMap.get(rarity);
        return levelMap.getOrDefault(level, 0);
    }

    public void displayInventory() {
        if (map.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Map.Entry<String, Map<Rarity, Map<Integer, Integer>>> nameEntry : map.entrySet()) {
            String itemName = nameEntry.getKey();
            Map<Rarity, Map<Integer, Integer>> rarityMap = nameEntry.getValue();

            System.out.println("Item: " + itemName);
            for (Map.Entry<Rarity, Map<Integer, Integer>> rarityEntry : rarityMap.entrySet()) {
                Rarity rarity = rarityEntry.getKey();
                Map<Integer, Integer> levelMap = rarityEntry.getValue();

                System.out.println("  Rarity: " + rarity.getDisplayName());
                for (Map.Entry<Integer, Integer> levelEntry : levelMap.entrySet()) {
                    int level = levelEntry.getKey();
                    int count = levelEntry.getValue();
                    if (isEpicRarity(rarity)) {
                        System.out.println("    Level " + level + ": " + count);
                    } else {
                        System.out.println("    Count: " + count);
                    }
                }
            }
        }
    }


    private boolean isEpicRarity(Rarity rarity) {
        return rarity == Rarity.EPIC0 || rarity == Rarity.EPIC1 || rarity == Rarity.EPIC2;
    }


    public boolean saveInventory(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(instance);
            System.out.println("Inventory saved successfully to " + filename);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save inventory: " + e.getMessage());
            return false;
        }
    }


    public boolean loadInventory(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Inventory loadedInventory = (Inventory) ois.readObject();
            this.map = loadedInventory.map;
            System.out.println("Inventory loaded successfully from " + filename);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return false;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load inventory: " + e.getMessage());
            return false;
        }
    }
}
