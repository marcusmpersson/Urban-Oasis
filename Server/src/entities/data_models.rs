use mongodb::bson::oid::ObjectId;
use serde::{Deserialize, Serialize};

#[derive(Debug, Serialize, Deserialize, Clone)]
pub struct User {
    pub _id: ObjectId,
    pub email: String,
    pub username: String,
    pub password: String,
    pub created_at: String,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Inventory {
    pub _id: ObjectId,
    pub name: String,
    pub quantity: i32,
    pub user_id: ObjectId,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct HealthStat {
    pub overallMood: i32,
    pub waterLevel: i32,
    pub envSatisfaction: i32,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct PlantTop {
    pub imageFilePaths: Vec<String>, 
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Item {
    pub price: i32,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct ShopItem {
    pub item: Item,
    pub image: String,
    pub name: String,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct Deco {
    pub shopItem: ShopItem,
    
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub enum Enviroment {
    SHADE,
    HALF_SHADE,
    SUNNY,
    HUMID,
}

#[derive(Serialize, Deserialize, Debug, Clone)]
pub enum Rarity {
    COMMOM,
    RARE,
    EPIC,
    LEGENDARY,
}



