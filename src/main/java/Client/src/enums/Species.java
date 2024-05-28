package enums;

public enum Species {

    //------------ common ------------
    CACTUS(Environment.SUNNY,"Flowering Cactus", 1),
    PARLOR_PALM(Environment.HALF_SHADE, "Parlor Palm", 3),
    ARROWHEAD_PLANT(Environment.HUMID, "Arrowhead Plant", 5),

    //------------ rare ------------
    SWORD_FERN(Environment.SHADE, "Sword Fern", 8),
    COFFEE_PLANT(Environment.HUMID, "Coffee Plant", 8),

    //------------ epic ------------
    PINEAPPLE_PLANT(Environment.HALF_SHADE, "Pineapple Plant", 4),
    ORCHID(Environment.HUMID, "Orchids", 7),

    //------------ legendary ------------
    CHILI_PEPPER(Environment.SUNNY, "Chili Pepper", 7),
    ROSE_PLANT(Environment.SUNNY, "Roses", 9);


    private final Environment preferredEnvironment;
    private final String speciesName;
    private final int waterRate; //number affecting the water levels of the species

    private Species(Environment preferredEnvironment, String speciesName, int waterRate){
        this.preferredEnvironment = preferredEnvironment;
        this.speciesName = speciesName;
        this.waterRate = waterRate;
    }
    public Environment getPreferredEnvironment(){
        return preferredEnvironment;
    }
    public String getSpeciesName(){
        return speciesName;
    }
    public int getWaterRate(){return waterRate; }
}