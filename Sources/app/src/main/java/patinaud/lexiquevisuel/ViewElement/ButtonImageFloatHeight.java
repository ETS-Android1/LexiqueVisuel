package patinaud.lexiquevisuel.ViewElement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class ButtonImageFloatHeight extends Button{

    private Bitmap img_btn;

    public ButtonImageFloatHeight(String filePath, int x , int y , int largeurZoneDessin , Canvas canvas) {
        init(filePath,  x ,  y ,  largeurZoneDessin ,  canvas, "");
    }


    public ButtonImageFloatHeight(String filePath, int x , int y , int largeurZoneDessin , Canvas canvas, String id) {
        init(filePath,  x ,  y ,  largeurZoneDessin ,  canvas, id);
    }


    public void init(String filePath, int x , int y , int largeurZoneDessin , Canvas canvas, String id)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.larg = largeurZoneDessin;

        img_btn = BitmapFactory.decodeFile(filePath);

        int hautImg = img_btn.getHeight();
        int largImg = img_btn.getWidth();

        Log.e("ButtonImage", "Haut : " + hautImg );
        Log.e("ButtonImage", "Larg : " + largImg );


        //Il faut augmenter l'image par sa hauteur
        this.haut = (int)(hautImg * ((double)largeurZoneDessin / (double)largImg));


        img_btn = Bitmap.createScaledBitmap(img_btn, largeurZoneDessin, this.haut, true);
        img_btn = addBitmapBorder( img_btn, 2, Color.GRAY);
        drawOn(canvas, x ,y);
    }

    //Permet de redessinner le boutton sur un autre Canvas
    public void drawOn(Canvas canvas, int x, int y)
    {
        this.x = x;
        this.y = y;
        canvas.drawBitmap(img_btn, x ,y , (new Paint()));
    }

}
