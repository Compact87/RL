package model;

public enum SHIP {
    BLUE("playerShip1_blue.png","playerLife1_blue.png"),
    GREEN("playerShip1_green.png","playerLife1_green.png"),
    ORANGE("playerShip1_orange.png","playerLife1_orange.png"),
    RED("playerShip1_red.png","playerLife1_red.png");
    String urlShip;
    String urlLife;
    private SHIP(String urlShip,String urlLife){
        this.urlShip=urlShip;this.urlLife=urlLife;
    }
    public String getUrlShip(){return urlShip;}
    public String getUrlLife(){return urlLife;}
}
