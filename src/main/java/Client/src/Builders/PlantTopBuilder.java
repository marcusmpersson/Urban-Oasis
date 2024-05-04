package Builders;

import entities.PlantTop;
import enums.Rarity;
import enums.Species;

import java.util.ArrayList;

public class PlantTopBuilder {

    public static PlantTop buildPlantTop (Species species){
        switch (species){
            case CACTUS:
                return buildCactus();
            case ORCHID:
                return buildOrchid();
            case CRASSULA:
                return buildCrassula();
            case ROSE_PLANT:
                return buildRose();
            case SWORD_FERN:
                return buildSwordFern();
            case PARLOR_PALM:
                return buildPalm();
            case COFFEE_PLANT:
                return buildCoffeePlant();
            case CHILI_PEPPER:
                return buildChiliPepper();
            case PAINTED_NETTLE:
                return buildPaintedNettle();
            case ARROWHEAD_PLANT:
                return buildArrowheadPlant();
            case PINEAPPLE_PLANT:
                return buildPineapple();
            case BEGONIA_POLKA_PLANT:
                return buildPolkaPlant();
        }

        return null;
    }

    private static PlantTop buildCactus() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/cactus_baby.png");
        imageFilePaths.add("plants/cactus_young.png");
        imageFilePaths.add("plants/cactus_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.CACTUS, 250, Rarity.COMMON);
    }

    private static PlantTop buildPalm() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/palm_baby.png");
        imageFilePaths.add("plants/palm_young.png");
        imageFilePaths.add("plants/palm_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.PARLOR_PALM, 250, Rarity.COMMON);
    }

    private static PlantTop buildArrowheadPlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/arrowhead_baby.png");
        imageFilePaths.add("plants/arrowhead_young.png");
        imageFilePaths.add("plants/arrowhead_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.ARROWHEAD_PLANT, 300, Rarity.COMMON);
    }

    private static PlantTop buildSwordFern() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/swordFern_baby.png");
        imageFilePaths.add("plants/swordFern_young.png");
        imageFilePaths.add("plants/swordFern_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.SWORD_FERN, 350, Rarity.RARE);
    }

    private static PlantTop buildPaintedNettle() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/paintedNettle_baby.png");
        imageFilePaths.add("plants/paintedNettle_young.png");
        imageFilePaths.add("plants/paintedNettle_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.PAINTED_NETTLE, 350, Rarity.RARE);
    }

    private static PlantTop buildCoffeePlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/coffee_baby.png");
        imageFilePaths.add("plants/coffee_young.png");
        imageFilePaths.add("plants/coffee_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.COFFEE_PLANT, 400, Rarity.RARE);
    }

    private static PlantTop buildCrassula() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/crassula_baby.png");
        imageFilePaths.add("plants/crassula_young.png");
        imageFilePaths.add("plants/crassula_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.CRASSULA, 450, Rarity.EPIC);
    }

    private static PlantTop buildPineapple() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/pineapple_baby.png");
        imageFilePaths.add("plants/pineapple_young.png");
        imageFilePaths.add("plants/pineapple_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.PINEAPPLE_PLANT, 450, Rarity.EPIC);
    }

    private static PlantTop buildOrchid() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/orchid_baby.png");
        imageFilePaths.add("plants/orchid_young.png");
        imageFilePaths.add("plants/orchid_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.ORCHID, 500, Rarity.EPIC);
    }

    private static PlantTop buildChiliPepper() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/pepper_baby.png");
        imageFilePaths.add("plants/pepper_young.png");
        imageFilePaths.add("plants/pepper_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.CHILI_PEPPER, 600, Rarity.LEGENDARY);
    }

    private static PlantTop buildPolkaPlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/polka_baby.png");
        imageFilePaths.add("plants/polka_young.png");
        imageFilePaths.add("plants/polka_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.BEGONIA_POLKA_PLANT, 650, Rarity.LEGENDARY);
    }

    private static PlantTop buildRose() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/rose_baby.png");
        imageFilePaths.add("plants/rose_young.png");
        imageFilePaths.add("plants/rose_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        return new PlantTop(imageFilePaths, Species.ROSE_PLANT, 650, Rarity.LEGENDARY);
    }

}