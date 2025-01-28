# Bootcamp_Project
Item Upgrade System This application in Java handles the upgrading of items by their respective levels of rarity. The application will provide the user with the capability of creating items, displaying their inventory, upgrading items, saving and loading their inventory.

Table of Contents
Overview
Features
Upgrade Paths
Installation
Usage
1. Create Item
2. Display Inventory
3. Upgrade Item
4. Save Inventory
5. Load Inventory
6. Exit
Example Interaction
Class Structure
Contributing
License
Overview
The Item Upgrade System is designed to manage a collection of items categorized by their rarity levels. Users can create items, upgrade them through defined rarity tiers, and maintain their inventory persistently. The system ensures that item names are treated case-insensitively and enforces specific rules for item upgrades.

Features
Case-Insensitive Item Handling: Treats item names like "gold", "Gold", and "GOLd" as the same item by storing all names in uppercase internally.
Manual Generation of Items is restricted to only EPIC0, or as it is more commonly known, "EPIC". Both EPIC1 and EPIC2 items are acquired through upgrade only.
Upgrade Logic Accuracy: Upgrade logic is exact-upgrade paths, with specific counts of items consumed and produced.
User-Friendly Interface: The UI gives detailed prompts and messages that help the user to operate on the inventory for different operations.
Data Persistence: Save and load of the current state of the inventory is provided using Java serialization.
Modular Codebase: Organized in separate classes for better readability and maintainability.
Upgrade Paths
The system defines explicit upgrade paths between the different levels of rarity. Each upgrade will consume a set of items in order to provide an item at a higher level of rarity.

Non-EPIC Upgrades
COMMON → GREAT

Consumes: 2 COMMON items
Produces: 1 GREAT item
GREAT → RARE

Consumes: 2 GREAT items
Produces: 1 RARE item
RARE → EPIC0

Consumes: 2 RARE items
Produces: 1 EPIC0 (EPIC) item
EPIC Upgrades
EPIC0 (EPIC) → EPIC1

Consumes: 2 EPIC0
Produces: 1 EPIC1 item
EPIC1 → EPIC2

Consumes: 1 EPIC1 item + 1 EPIC0 item
Produces: 1 EPIC2 item
EPIC2 → LEGENDARY

Consumes: 3 EPIC2 items
Produces: 1 LEGENDARY item
Legendary Items
LEGENDARY items are the highest rarity and cannot be upgraded further.
