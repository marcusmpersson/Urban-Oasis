package Builders;

import entities.PlantTop;
import entities.Pot;
import enums.Rarity;
import enums.Species;

import java.util.ArrayList;

public class PlantTopBuilder {

    public static PlantTop buildCactus() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/cactus_baby.png");
        imageFilePaths.add("plants/cactus_young.png");
        imageFilePaths.add("plants/cactus_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.CACTUS, 250, Rarity.COMMON);
    }

    public static PlantTop buildPalm() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/palm_baby.png");
        imageFilePaths.add("plants/palm_young.png");
        imageFilePaths.add("plants/palm_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.PARLOR_PALM, 250, Rarity.COMMON);
    }

    public static PlantTop buildArrowheadPlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/arrowhead_baby.png");
        imageFilePaths.add("plants/arrowhead_young.png");
        imageFilePaths.add("plants/arrowhead_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.ARROWHEAD_PLANT, 300, Rarity.COMMON);
    }

    public static PlantTop buildSwordFern() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/swordFern_baby.png");
        imageFilePaths.add("plants/swordFern_young.png");
        imageFilePaths.add("plants/swordFern_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.SWORD_FERN, 350, Rarity.RARE);
    }

    public static PlantTop buildPaintedNettle() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/paintedNettle_baby.png");
        imageFilePaths.add("plants/paintedNettle_young.png");
        imageFilePaths.add("plants/paintedNettle_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.PAINTED_NETTLE, 350, Rarity.RARE);
    }

    public static PlantTop buildCoffeePlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/coffee_baby.png");
        imageFilePaths.add("plants/coffee_young.png");
        imageFilePaths.add("plants/coffee_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.COFFEE_PLANT, 400, Rarity.RARE);
    }

    public static PlantTop buildCrassula() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/crassula_baby.png");
        imageFilePaths.add("plants/crassula_young.png");
        imageFilePaths.add("plants/crassula_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.CRASSULA, 450, Rarity.EPIC);
    }

    public static PlantTop buildPineapple() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/pineapple_baby.png");
        imageFilePaths.add("plants/pineapple_young.png");
        imageFilePaths.add("plants/pineapple_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.PINEAPPLE_PLANT, 450, Rarity.EPIC);
    }

    public static PlantTop buildOrchid() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/orchid_baby.png");
        imageFilePaths.add("plants/orchid_young.png");
        imageFilePaths.add("plants/orchid_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.ORCHID, 500, Rarity.EPIC);
    }

    public static PlantTop buildVenusTrap() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/venusTrap_baby.png");
        imageFilePaths.add("plants/venusTrap_young.png");
        imageFilePaths.add("plants/venusTrap_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.VENUS_FLYTRAP, 600, Rarity.LEGENDARY);
    }

    public static PlantTop buildPolkaPlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/polka_baby.png");
        imageFilePaths.add("plants/polka_young.png");
        imageFilePaths.add("plants/polka_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.BEGONIA_POLKA_PLANT, 650, Rarity.LEGENDARY);
    }

    public static PlantTop buildLaceLeaf() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/laceLeaf_baby.png");
        imageFilePaths.add("plants/laceLeaf_young.png");
        imageFilePaths.add("plants/laceLeaf_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.LACELEAF, 650, Rarity.LEGENDARY);
    }

}