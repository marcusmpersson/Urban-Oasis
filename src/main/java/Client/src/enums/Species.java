package enums;

public enum Species {

    //------------ common ------------
    CACTUS(Environment.SUNNY,"Cactacea", 1),
    PARLOR_PALM(Environment.HALF_SHADE, "Chamaedorea Elegans", 3),
    ARROWHEAD_PLANT(Environment.HUMID, "Syngonium Podophyllum", 5),

    //------------ rare ------------
    SWORD_FERN(Environment.SHADE, "Polystichum Munitum", 8),
    PAINTED_NETTLE(Environment.SUNNY, "Coleus Scuttlearioides", 5),
    COFFEE_PLANT(Environment.HUMID, "Coffea Arabica", 8),

    //------------ epic ------------
    CRASSULA(Environment.SUNNY, "Crassula Orbicularis", 1),
    PINEAPPLE_PLANT(Environment.HALF_SHADE, "Ananas Comosus", 3),
    ORCHID(Environment.HUMID, "Orchidaceae", 5),

    //------------ legendary ------------
    CHILI_PEPPER(Environment.SUNNY, "Chili Capsicum", 8),
    BEGONIA_POLKA_PLANT(Environment.HALF_SHADE, "Begonia", 6),
    ROSE_PLANT(Environment.HALF_SHADE, "Rosa", 5);


    private final Environment preferredEnvironment;
    private final String scientificName;
    private final int waterRate; //number affecting the water levels of the species

    private Species(Environment preferredEnvironment, String scientificName, int waterRate){
        this.preferredEnvironment = preferredEnvironment;
        this.scientificName = scientificName;
        this.waterRate = waterRate;
    }
    public Environment getPreferredEnvironment(){
        return preferredEnvironment;
    }
    public String getScientificName(){
        return scientificName;
    }
    public int getWaterRate(){return waterRate; }
}