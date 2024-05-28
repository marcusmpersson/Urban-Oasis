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
            case ARROWHEAD_PLANT:
                return buildArrowheadPlant();
            case PINEAPPLE_PLANT:
                return buildPineapple();
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

        String desc = "This is a low maintenance flowering cactus from South America. " +
                "Place it in a sunny spot and water it once a blue moon for optimal health. " +
                "Be careful not to overwater it!";

        return new PlantTop(imageFilePaths, Species.CACTUS, 250, Rarity.COMMON, desc);
    }

    private static PlantTop buildPalm() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/palm_baby.png");
        imageFilePaths.add("plants/palm_young.png");
        imageFilePaths.add("plants/palm_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "Turn your room into the rainforests from southeastern Mexico with this parlor palm." +
                "Indoor palms thrive in half shade environments and less frequently require water.";

        return new PlantTop(imageFilePaths, Species.PARLOR_PALM, 250, Rarity.COMMON, desc);
    }

    private static PlantTop buildArrowheadPlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/arrowhead_baby.png");
        imageFilePaths.add("plants/arrowhead_young.png");
        imageFilePaths.add("plants/arrowhead_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "This variant of the arrowhead plant originated from Hawaii." +
                "To thrive indoors it has to be placed in a humid environment and watered moderately.";

        return new PlantTop(imageFilePaths, Species.ARROWHEAD_PLANT, 300, Rarity.COMMON, desc);
    }

    private static PlantTop buildSwordFern() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/swordFern_baby.png");
        imageFilePaths.add("plants/swordFern_young.png");
        imageFilePaths.add("plants/swordFern_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "Sword ferns are common in moist coniferous forests in the southern regions of British Columbia." +
                "To replicate this environment in your room, place the plant in a shady spot and water it often. ";

        return new PlantTop(imageFilePaths, Species.SWORD_FERN, 350, Rarity.RARE, desc);
    }

    private static PlantTop buildCoffeePlant() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/coffee_baby.png");
        imageFilePaths.add("plants/coffee_young.png");
        imageFilePaths.add("plants/coffee_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "The beloved, globally brewed coffee plant is native to Ethiopia, " +
                "and requires frequent watering and a moist environment indoors. " +
                "Place it in a greenhouse or a terrarium for optimal health and it may flower!";

        return new PlantTop(imageFilePaths, Species.COFFEE_PLANT, 400, Rarity.RARE, desc);
    }

    private static PlantTop buildPineapple() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/pineapple_baby.png");
        imageFilePaths.add("plants/pineapple_young.png");
        imageFilePaths.add("plants/pineapple_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "This variant of pineapple originated from Brazil. " +
                "If properly taken care of, it will grow into a full sized pineapple! " +
                "Luckily, it only requires a half shade environment and moderate watering.";

        return new PlantTop(imageFilePaths, Species.PINEAPPLE_PLANT, 450, Rarity.EPIC, desc);
    }

    private static PlantTop buildOrchid() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/orchid_baby.png");
        imageFilePaths.add("plants/orchid_young.png");
        imageFilePaths.add("plants/orchid_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "Orchids are beautiful natives of Japan and China and have been used in medication for thousands of years. " +
                "These flowers are delicate and require a moist and humid environment with lots of water.";

        return new PlantTop(imageFilePaths, Species.ORCHID, 500, Rarity.EPIC, desc);
    }

    private static PlantTop buildChiliPepper() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/pepper_baby.png");
        imageFilePaths.add("plants/pepper_young.png");
        imageFilePaths.add("plants/pepper_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "Chili peppers are not just fun to eat but also fascinating to look at. " +
                "These chili plants will transition from white blossoms into colorful chili peppers. " +
                "Give it lots of sun and water for optimal health. ";

        return new PlantTop(imageFilePaths, Species.CHILI_PEPPER, 600, Rarity.LEGENDARY, desc);
    }

    private static PlantTop buildRose() {
        ArrayList<String> imageFilePaths = new ArrayList<>();
        imageFilePaths.add("plants/planted.png");
        imageFilePaths.add("plants/rose_baby.png");
        imageFilePaths.add("plants/rose_young.png");
        imageFilePaths.add("plants/rose_adult.png");
        imageFilePaths.add("plants/tombstone.png");

        String desc = "Originated in central Asia, roses are among the most popular flowers worldwide. " +
                "Roses are high maintenance and require lots of water and direct sunlight exposure. " +
                "If you take proper care of it, this rose plant will bloom all year around.";

        return new PlantTop(imageFilePaths, Species.ROSE_PLANT, 650, Rarity.LEGENDARY, desc);
    }

}