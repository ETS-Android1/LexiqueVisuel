package patinaud.lexiquevisuel.ViewElement;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

public class Button {

    protected int x,y,larg,haut;
    protected String id = "";
    protected Integer borderColor = null;
    protected int borderStroke = 0;
    protected int alpha = 255;

    public Button ()
    {

    }

    public Button (int x , int y , int larg , int haut, Canvas canvas)
    {
        this.x = x;
        this.y = y;
        this.larg = larg;
        this.haut = haut;
    }

    public Button (int x , int y , int larg , int haut, Canvas canvas, String id)
    {
        this.x = x;
        this.y = y;
        this.larg = larg;
        this.haut = haut;
        this.id = id;
    }

    public int getHaut()
    {
        return haut;
    }


    public boolean isClicked(float x, float y) {
        if ( x > this.x && x < this.x + larg && y > this.y && y < this.y+haut)
        {
            return true;
        }
        return false;
    }



    public void setId(String id){ this.id = id;}
    public String getId()
    {
        return id;
    }



    protected Bitmap addBitmapBorder(Bitmap bmp, int borderSize, int color) {

        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(color);
        bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() - 2 * borderSize, bmp.getHeight() - 2 * borderSize, true);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }


    public void setBorder(int color, int stroke)
    {
        this.borderColor = color;
        this.borderStroke = stroke;
    }

    public void removeBorder()
    {
        borderColor = null;
        borderStroke = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }



    public void setAlpha(int alpha)
    {
        this.alpha = alpha;
    }


}
