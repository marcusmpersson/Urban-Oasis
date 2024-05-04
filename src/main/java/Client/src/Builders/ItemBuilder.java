package Builders;

import entities.Pot;
import entities.Seed;
import enums.PotType;
import enums.Rarity;
import enums.Species;

import java.util.ArrayList;

public class ItemBuilder {

    public static Seed buildSeed(Rarity rarity) {
        switch (rarity){
            case COMMON:
                return commonSeed();
            case RARE:
                return rareSeed();
            case EPIC:
                return epicSeed();
            case LEGENDARY:
                return legendarySeed();
        }

        return null;
    }

    public static Pot buildPot(PotType type) {
        switch (type) {
            case POT_LILAC:
                return new Pot("Pot (lilac)", "pots/pot_lilac.png", 100, PotType.POT_LILAC,
                        "Simple but vibrant lilac plant pot. Material: fired clay, painted");
            case POT_ORANGE:
                return new Pot("Pot (orange)", "pots/pot_orange.png", 100, PotType.POT_ORANGE,
                        "A classic orange plant pot. Material: fired clay");
            case POT_STRIPED_BLUE:
                return new Pot("Pot (blue striped)", "pots/pot_striped_blue.png", 200, PotType.POT_STRIPED_BLUE,
                        "A vibrant striped blue pot. Material: painted ceramic");
            case POT_POLKA_PINK:
                return new Pot("Pot (polka pink)", "pots/pot_polka_pink.png", 200, PotType.POT_POLKA_PINK,
                        "Polka dotted pot to give life to your room. Material: epoxy coated clay");
            case ROUND_POT_CLAY:
                return new Pot("Round Pot (clay)", "pots/round_pot_clay.png", 100, PotType.ROUND_POT_CLAY,
                        "A classic round pot. Material: fired clay");
            case ROUND_POT_GOLDEN:
                return new Pot("Round Pot (golden)", "pots/round_pot_golden.png", 200, PotType.ROUND_POT_CLAY,
                        "A luxurious golden pot for your very special plants. Material: gold coated clay");
            case ROUND_POT_STRIPED_GREEN:
                return new Pot("Round Pot (green striped)", "pots/round_pot_striped_green.png", 200, PotType.ROUND_POT_STRIPED_GREEN,
                        "Green striped pot to add texture to your place. Material: painted ceramic");
            case ROUND_POT_RED:
                return new Pot("Round Pot (red)", "pots/round_pot_red.png", 100, PotType.ROUND_POT_RED,
                        "Round pot with a deep red shade. Material: fired clay, painted");
        }

        return null;
    }

    public static ArrayList<Pot> buildAllPots(){
        ArrayList<Pot> pots = new ArrayList<>();

        pots.add(buildPot(PotType.POT_LILAC));
        pots.add(buildPot(PotType.POT_ORANGE));
        pots.add(buildPot(PotType.POT_POLKA_PINK));
        pots.add(buildPot(PotType.POT_STRIPED_BLUE));
        pots.add(buildPot(PotType.ROUND_POT_CLAY));
        pots.add(buildPot(PotType.ROUND_POT_GOLDEN));
        pots.add(buildPot(PotType.ROUND_POT_STRIPED_GREEN));
        pots.add(buildPot(PotType.ROUND_POT_RED));

        return pots;
    }

    /* ------------------
    * inner private methods
    * ------------------- */

    private static Seed commonSeed(){
        ArrayList<Species> possibleSpecies = new ArrayList<>();
        possibleSpecies.add(Species.CACTUS);
        possibleSpecies.add(Species.PARLOR_PALM);
        possibleSpecies.add(Species.ARROWHEAD_PLANT);

         return new Seed(Rarity.COMMON, possibleSpecies, "Common Seed",
                "seeds/common_seeds.png", 150,
                 "This seed package contains the seed of an unknown common and affordable plant.");
    }

    private static Seed rareSeed(){
        ArrayList<Species> possibleSpecies = new ArrayList<>();
        possibleSpecies.add(Species.SWORD_FERN);
        possibleSpecies.add(Species.PAINTED_NETTLE);
        possibleSpecies.add(Species.COFFEE_PLANT);

        return new Seed(Rarity.RARE, possibleSpecies, "Rare Seed",
                "seeds/rare_seeds.png", 250,
                "This seed package contains the seed of an unknown rare and unique plant.");
    }

    private static Seed epicSeed(){
        ArrayList<Species> possibleSpecies = new ArrayList<>();
        possibleSpecies.add(Species.CRASSULA);
        possibleSpecies.add(Species.PINEAPPLE_PLANT);
        possibleSpecies.add(Species.ORCHID);

        return new Seed(Rarity.EPIC, possibleSpecies, "Epic Seed",
                "seeds/epic_seeds.png", 350,
                "This seed package contains the seed of an unknown epic but pricey plant.");
    }

    private static Seed legendarySeed(){
        ArrayList<Species> possibleSpecies = new ArrayList<>();
        possibleSpecies.add(Species.CHILI_PEPPER);
        possibleSpecies.add(Species.BEGONIA_POLKA_PLANT);
        possibleSpecies.add(Species.ROSE_PLANT);

        return new Seed(Rarity.LEGENDARY, possibleSpecies, "Legendary Seed",
                "seeds/legendary_seeds.png", 250,
                "This expensive seed package contains the seed of an unknown legendary and exotic plant.");
    }
}
