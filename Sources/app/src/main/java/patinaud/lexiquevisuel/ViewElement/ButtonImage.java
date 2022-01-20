package patinaud.lexiquevisuel.ViewElement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

public class ButtonImage extends Button{

    private int offsetX, offsetY, largeurImageAgrandit, hauteurImageAgrandit;
    private Bitmap img_btn;


    // Permet de dessiner l'image dans la zone spécifiée en conservant ses proportions

    public ButtonImage(String filePath, int x , int y , int largeurZoneDessin , int hauteurZoneDessin, Canvas canvas)
    {
        super (x,y,largeurZoneDessin,hauteurZoneDessin,canvas);
        initPathFile( filePath,  x ,  y , largeurZoneDessin , hauteurZoneDessin, canvas, true);
    }

    public ButtonImage(String filePath, int x , int y , int largeurZoneDessin , int hauteurZoneDessin, Canvas canvas, boolean respectProportions)
    {
        super (x,y,largeurZoneDessin,hauteurZoneDessin,canvas);
        initPathFile( filePath,  x ,  y , largeurZoneDessin , hauteurZoneDessin, canvas, respectProportions);
    }


    public ButtonImage(int ressource, int x , int y , int largeurZoneDessin , int hauteurZoneDessin, Canvas canvas, View view)
    {
        super (x,y,largeurZoneDessin,hauteurZoneDessin,canvas);
        initRessource( ressource,  x ,  y , largeurZoneDessin , hauteurZoneDessin, canvas, view, true);
    }

    public ButtonImage(int ressource, int x , int y , int largeurZoneDessin , int hauteurZoneDessin, Canvas canvas, View view , boolean respectProportions)
    {
        super (x,y,largeurZoneDessin,hauteurZoneDessin,canvas);
        initRessource( ressource,  x ,  y , largeurZoneDessin , hauteurZoneDessin, canvas, view, respectProportions);
    }



    private void initPathFile(String filePath, int x , int y , int largeurZoneDessin , int hauteurZoneDessin, Canvas canvas, boolean respectProportions) {
        img_btn = BitmapFactory.decodeFile(filePath);
        init( x , y , largeurZoneDessin , hauteurZoneDessin, canvas, respectProportions);
    }

    private void initRessource(int ressource, int x , int y , int largeurZoneDessin , int hauteurZoneDessin, Canvas canvas, View view, boolean respectProportions) {
        img_btn = BitmapFactory.decodeResource(view.getContext().getResources(),ressource);
        init( x , y , largeurZoneDessin , hauteurZoneDessin, canvas, respectProportions);
    }

    private void init( int x , int y , int largeurZoneDessin , int hauteurZoneDessin, Canvas canvas, boolean respectProportions) {

        this.x = x;
        this.y = y;

        int hautImg = img_btn.getHeight();
        int largImg = img_btn.getWidth();

        Log.v("ButtonImage", "Haut : " + hautImg );
        Log.v("ButtonImage", "Larg : " + largImg );

        //on positionne l'image de respecter ses proportions
        double ratioHautLargImg = (double)hautImg / (double)largImg;
        double ratioHautLargZoneDessin = (double)hauteurZoneDessin / (double)largeurZoneDessin;

        largeurImageAgrandit = largeurZoneDessin;
        hauteurImageAgrandit = hauteurZoneDessin;
        offsetX = 0;
        offsetY = 0;

        if ( respectProportions) {
            if (ratioHautLargImg > ratioHautLargZoneDessin) {
                //il faut augmenter l'image par sa hauteur
                hauteurImageAgrandit = hauteurZoneDessin;
                largeurImageAgrandit = (int)(largImg * ((double)hauteurZoneDessin / (double)hautImg));
                offsetX = (largeurZoneDessin - largeurImageAgrandit) / 2;
            } else {
                //Il faut augmenter l'image par sa largeur
                largeurImageAgrandit = largeurZoneDessin;
                hauteurImageAgrandit = (int)(hautImg * ((double)largeurZoneDessin / (double)largImg));
                offsetY = (hauteurZoneDessin - hauteurImageAgrandit) / 2;

            }
        }

        Log.v("ButtonImage" , "Proportions : " + largeurZoneDessin + " : " + hauteurZoneDessin + " H " + largeurImageAgrandit + " : " + hauteurImageAgrandit + " : " + offsetX + " : " + offsetY );
        img_btn = Bitmap.createScaledBitmap(img_btn, largeurImageAgrandit, hauteurImageAgrandit, true);

    }

    public void setRotation(int angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        img_btn = Bitmap.createBitmap(img_btn, 0, 0, img_btn.getWidth(), img_btn.getHeight(), matrix, true);
    }



    public void drawOn(Canvas canvas)
    {
        Paint p = new Paint();
        p.setAlpha(alpha);
        drawOn(canvas, p);
    }



    public void drawOn(Canvas canvas, Paint p)
    {

        if (borderColor != null)
        {
            Paint pBorder = new Paint();
            pBorder.setColor(borderColor);

            Paint pWhite = new Paint();
            pWhite.setColor(Color.WHITE);

            canvas.drawRect(x + offsetX - borderStroke,y+ offsetY - borderStroke,x + offsetX + largeurImageAgrandit + borderStroke, y+ offsetY + hauteurImageAgrandit + borderStroke, pBorder);
            canvas.drawRect(x + offsetX,y+ offsetY,x + offsetX + largeurImageAgrandit, y+ offsetY + hauteurImageAgrandit, pWhite);
        }

        canvas.drawBitmap(img_btn, x + offsetX,y + offsetY, p);
    }


    public int getHeight()
    {
        return hauteurImageAgrandit;
    }

    public int getWidth() {return largeurImageAgrandit;}

    public int getOffsetY()
    {
        return offsetY;
    }

    public int getOffsetX(){ return offsetX; }

}
