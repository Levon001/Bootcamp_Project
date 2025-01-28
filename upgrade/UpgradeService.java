package upgrade;

import inventory.Inventory;
import items.Rarity;


public class UpgradeService {
    private Inventory inventory;

    public UpgradeService() {
        inventory = Inventory.getInstance();
    }


    public boolean upgradeNonEpicItem(String name, Rarity currentRarity) {
        if (currentRarity == Rarity.LEGENDARY) {
            System.out.println("Cannot upgrade beyond Legendary rarity.");
            return false;
        }

        Rarity nextRarity = getNextRarity(currentRarity);
        if (nextRarity == null) {
            System.out.println("Invalid upgrade path.");
            return false;
        }

        int required = getRequiredItemsForUpgrade(currentRarity);
        if (required < 0) {
            System.out.println("Invalid upgrade path.");
            return false;
        }

        int level = 0;

        int availableCount = inventory.getItemCount(name, currentRarity, level);
        if (availableCount < required) {
            System.out.println("Not enough items to perform the upgrade.");
            return false;
        }


        boolean removed = inventory.removeItems(name, currentRarity, level, required);
        if (!removed) {
            System.out.println("Failed to remove items for upgrade.");
            return false;
        }


        boolean added = inventory.addItem(name, nextRarity, 0, true);
        if (added) {
            System.out.println("Upgraded to " + nextRarity.getDisplayName() + " " + name);
            return true;
        } else {
            System.out.println("Failed to add the upgraded item.");
            return false;
        }
    }


    public boolean upgradeEpicItem(String name, Rarity currentRarity, int currentLevel) {
        switch (currentRarity) {
            case EPIC0:
                return upgradeEpic0ToEpic1(name);
            case EPIC1:
                return upgradeEpic1ToEpic2(name);
            case EPIC2:
                return upgradeEpic2ToLegendary(name);
            default:
                System.out.println("Invalid EPIC rarity.");
                return false;
        }
    }


    private boolean upgradeEpic0ToEpic1(String name) {
        int required = 2;
        int level = 0;

        int availableCount = inventory.getItemCount(name, Rarity.EPIC0, level);
        if (availableCount < required) {
            System.out.println("Not enough EPIC0 items to perform the upgrade.");
            return false;
        }

        boolean removed = inventory.removeItems(name, Rarity.EPIC0, level, required);
        if (!removed) {
            System.out.println("Failed to remove EPIC0 items for upgrade.");
            return false;
        }

        boolean added = inventory.addItem(name, Rarity.EPIC1, 1, true);
        if (added) {
            System.out.println("Upgraded to EPIC1 " + name);
            return true;
        } else {
            System.out.println("Failed to add EPIC1 item.");
            return false;
        }
    }


    private boolean upgradeEpic1ToEpic2(String name) {
        int requiredEpic1 = 1;
        int requiredEpic0 = 1;

        int availableEpic1 = inventory.getItemCount(name, Rarity.EPIC1, 1);
        int availableEpic0 = inventory.getItemCount(name, Rarity.EPIC0, 0);

        if (availableEpic1 >= requiredEpic1 && availableEpic0 >= requiredEpic0) {
            boolean removedEpic1 = inventory.removeItems(name, Rarity.EPIC1, 1, requiredEpic1);
            boolean removedEpic0 = inventory.removeItems(name, Rarity.EPIC0, 0, requiredEpic0);
            if (removedEpic1 && removedEpic0) {
                boolean added = inventory.addItem(name, Rarity.EPIC2, 2, true);
                if (added) {
                    System.out.println("Upgraded to EPIC2 " + name);
                    return true;
                } else {
                    System.out.println("Failed to add EPIC2 item.");
                    return false;
                }
            } else {
                System.out.println("Failed to remove items for EPIC1 to EPIC2 upgrade.");
                return false;
            }
        } else {
            System.out.println("Not enough EPIC1 and/or EPIC0 items to perform the upgrade.");
            return false;
        }
    }


    private boolean upgradeEpic2ToLegendary(String name) {
        int required = 3;
        int level = 2;

        int availableCount = inventory.getItemCount(name, Rarity.EPIC2, level);
        if (availableCount < required) {
            System.out.println("Not enough EPIC2 items to perform the upgrade.");
            return false;
        }

        boolean removed = inventory.removeItems(name, Rarity.EPIC2, level, required);
        if (!removed) {
            System.out.println("Failed to remove EPIC2 items for upgrade.");
            return false;
        }

        boolean added = inventory.addItem(name, Rarity.LEGENDARY, 0, true);
        if (added) {
            System.out.println("Upgraded to LEGENDARY " + name);
            return true;
        } else {
            System.out.println("Failed to add LEGENDARY item.");
            return false;
        }
    }


    private Rarity getNextRarity(Rarity current) {
        switch (current) {
            case COMMON:
                return Rarity.GREAT;
            case GREAT:
                return Rarity.RARE;
            case RARE:
                return Rarity.EPIC0;
            case EPIC0:
                return Rarity.EPIC1;
            case EPIC1:
                return Rarity.EPIC2;
            case EPIC2:
                return Rarity.LEGENDARY;
            case LEGENDARY:
                return null;
            default:
                return null;
        }
    }


    private int getRequiredItemsForUpgrade(Rarity current) {
        switch (current) {
            case COMMON:
            case GREAT:
            case RARE:
            case EPIC0:
            case EPIC1:
                return 2;
            case EPIC2:
                return 3;
            case LEGENDARY:
                return -1;
            default:
                return -1;
        }
    }
}

