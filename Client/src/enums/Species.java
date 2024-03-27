package enums;

public enum Species {

    CACTUS(Environment.SUNNY,"Cactacea"),
    CRASSULA(Environment.SUNNY, "Crassula Orbicularis"),
    ARROWHEAD_PLANT(Environment.HUMID, "Syngonium Podophyllum"),
    SWORD_FERN(Environment.SHADE, "Polystichum Munitum"),
    COFFEE_PLANT(Environment.HUMID, "Coffea Arabica"),
    PAINTED_NETTLE(Environment.SUNNY, "Coleus Scuttlearioides");

    private final Environment preferredEnvironment;
    private final String scientificName;

    private Species(Environment preferredEnvironment, String scientificName){
        this.preferredEnvironment = preferredEnvironment;
        this.scientificName = scientificName;
    }
    public Environment getPreferredEnvironment(){
        return preferredEnvironment;
    }
    public String getScientificName(){
        return scientificName;
    }
}
