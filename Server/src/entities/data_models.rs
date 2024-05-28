use mongodb::bson::oid::ObjectId; // Importing the ObjectId type from MongoDB's BSON library.
use serde::{Deserialize, Serialize}; // Importing traits for serializing and deserializing data structures.

/// Represents a user in the system.
#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct UserCredentials {
    pub _id: ObjectId, // Unique identifier for the user.
    pub email: String, // Email address of the user.
    pub username: String, // Username chosen by the user.
    pub password: String, // User's password (should be stored securely).
    pub created_at: String, // Timestamp of when the user account was created.
}

/// Represents user save information in the system.
#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct UserInfo {
    pub _id: ObjectId, // Unique identifier for the user.
    pub rooms: Vec<Room>, // List of rooms associated with the user.
    pub currency: i32, // Amount of in-game currency the user has.
    pub inventory: Inventory, // User's inventory of items.
    pub last_updated: String, // Timestamp of the last update to the user's information.
}

/// Represents the inventory of a user.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Inventory {
    pub pottedPlants: Vec<PottedPlant>,
    pub pots: Vec<Pot>,
    pub seeds: Vec<Seed>,
    pub decorations: Vec<Deco>,
}

impl Inventory {
    /// Creates a new, empty inventory.
    pub fn new() -> Inventory {
        Inventory {
            pottedPlants: vec![], // Initialize with an empty list of potted plants.
            pots: vec![], // Initialize with an empty list of pots.
            seeds: vec![], // Initialize with an empty list of seeds.
            decorations: vec![], // Initialize with an empty list of decorations.
        }
    }
}

/// Represents a room owned by a user.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Room {
    pub slots: Vec<PlacementSlot>, // List of slots available for placing items in the room.
    pub imageFilePaths: Vec<String>, // File paths to images representing the room.
}

/// Represents the health statistics of a plant.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct HealthStat {
    pub overallMood: i32, // Overall mood of the plant.
    pub waterLevel: i32, // Water level of the plant.
    pub envSatisfaction: i32, // Satisfaction level with the environment.
}

/// Represents the top part of a plant with image file paths.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct PlantTop {
    pub imageFilePaths: Vec<String>, // File paths to images representing the plant's top.
}

/// Represents a generic item with a price.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Item {
    pub price: i32, // Price of the item.
}

/// Represents a seed with associated shop item details and plant species information.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Seed {
    pub shopItem: ShopItem, // Shop item details of the seed.
    pub possiblePlants: Vec<Species>, // List of possible plant species that can grow from the seed.
    pub rarity: Rarity, // Rarity level of the seed.
    pub species: Species, // Specific species of the seed.
}

/// Represents an item sold in the shop.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct ShopItem {
    pub item: Item, // General item details.
    pub image: String, // Image representing the shop item.
    pub name: String, // Name of the shop item.
}

/// Represents a decoration with shop item details and placement information.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Deco {
    pub shopItem: ShopItem, // Shop item details of the decoration.
    pub placedAt: PlacementSlot, // Placement slot where the decoration is placed.
    pub decoType: DecoType, // Type of the decoration.
}

/// Represents a slot where an item can be placed.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct PlacementSlot {
    pub environment: Enviroment, // Environment type of the slot.
    pub x: i32, // X coordinate of the slot.
    pub y: i32, // Y coordinate of the slot.
    pub taken: bool, // Indicates if the slot is already taken by an item.
}

/// Represents a pot with shop item details and placement information.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Pot {
    pub shopItem: ShopItem, // Shop item details of the pot.
    pub potType: PotType, // Type of the pot.
    pub placedAt: PlacementSlot, // Placement slot where the pot is placed.
}

/// Represents a potted plant with item details and placement information.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct PottedPlant {
    pub item: Item, // General item details of the potted plant.
    pub pot: Pot, // Pot in which the plant is placed.
    pub plant: PlantTop, // Top part of the plant.
    pub placementSlot: PlacementSlot, // Placement slot where the potted plant is placed.
}

/// Enum representing different types of environments.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub enum Enviroment {
    SHADE, // Shade environment.
    HALF_SHADE, // Half-shade environment.
    SUNNY, // Sunny environment.
    HUMID, // Humid environment.
}

/// Enum representing different rarity levels of items.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub enum Rarity {
    COMMOM, // Common rarity.
    RARE, // Rare rarity.
    EPIC, // Epic rarity.
    LEGENDARY, // Legendary rarity.
}

/// Enum representing different types of decorations.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub enum DecoType {
    TERRARIUM, // Terrarium decoration.
    BUDDHA, // Buddha decoration.
    GEODE, // Geode decoration.
    GLOBE_BLUE, // Blue globe decoration.
    GLOBE_GRAY, // Gray globe decoration.
    GLOBE_YELLOW_PINK, // Yellow and pink globe decoration.
    MUSIC_BOX, // Music box decoration.
    OLD_CLOCK, // Old clock decoration.
}

/// Enum representing different types of pots.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub enum PotType {
    POT_LILAC, // Lilac pot.
    POT_ORANGE, // Orange pot.
    POT_POLKA_PINK, // Polka dot pink pot.
    POT_STRIPED_BLUE, // Striped blue pot.
    ROUND_POT_CLAY, // Round clay pot.
    ROUND_POT_GOLDEN, // Round golden pot.
    ROUND_POT_RED, // Round red pot.
    ROUND_POT_STRIPED_GREEN, // Round striped green pot.
}

/// Enum representing different species of plants.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub enum Species {
    CACTUS, // Cactus plant.
    PARLOR_PALM, // Parlor palm plant.
    ARROWHEAD_PLANT, // Arrowhead plant.
    SWORD_FERN, // Sword fern plant.
    COFFEE_PLANT, // Coffee plant.
    PINEAPPLE_PLANT, // Pineapple plant.
    ORCHID, // Orchid plant.
    CHILI_PEPPER, // Chili pepper plant.
    ROSE_PLANT, // Rose plant.
}
