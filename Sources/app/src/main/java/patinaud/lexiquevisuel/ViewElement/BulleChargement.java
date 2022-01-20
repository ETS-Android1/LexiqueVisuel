package patinaud.lexiquevisuel.ViewElement;

import android.graphics.Color;

public class BulleChargement {

    private int color;
    private int x;
    private int y;
    private int larg;


    public BulleChargement(int posX , int posY) {
        init ( posX ,  posY);
    }

    private void init(int posX , int posY )
    {
        init( posX ,  posY , (int) ( Math.random() * 30) );
    }

    public BulleChargement(int posX , int posY , int larg)
    {
        init( posX ,  posY , larg );
    }

    private void init(int posX , int posY , int larg )
    {
        init( posX ,  posY , larg , Color.rgb((int)(Math.random() * 255) ,(int)(Math.random() * 255) ,(int)(Math.random() * 255) ) );
    }



    public BulleChargement(int posX , int posY , int larg , int color)
    {
        init( posX , posY , larg , color);
    }

    private void init(int posX , int posY , int larg , int color)
    {
        x = posX;
        y = posY;
        this.larg = larg;
        this.color = color;
    }


    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public int getX(){ return x;}
    public int getY(){ return y;}
    public int getLarg(){ return larg;}
    public void retrecir(int reduction)
    {
        larg = larg - reduction;
        if (larg < 0 ){  larg = 0; }
    }

    public void setLargeur(int largeur) {
        larg = largeur;
    }
}
