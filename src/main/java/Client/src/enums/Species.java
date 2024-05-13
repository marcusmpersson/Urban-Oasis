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
    PINEAPPLE_PLANT(Environment.HALF_SHADE, "Ananas Comosus", 4),
    ORCHID(Environment.HUMID, "Orchidaceae", 7),

    //------------ legendary ------------
    CHILI_PEPPER(Environment.SUNNY, "Chili Capsicum", 7),
    ROSE_PLANT(Environment.SUNNY, "Rosa", 9);


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