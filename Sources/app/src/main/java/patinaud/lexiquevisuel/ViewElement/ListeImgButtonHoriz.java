package patinaud.lexiquevisuel.ViewElement;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Properties;

import patinaud.lexiquevisuel.Utils.Tools;

public class ListeImgButtonHoriz {
    private ArrayList<String> listPathBtn = new ArrayList<String>();
    private ArrayList<ButtonImage> lstBtn = new ArrayList<ButtonImage>();
    private int x,y,larg,haut;


    public void addImg( String pathImg)
    {
        listPathBtn.add(pathImg);
    }

    public void draw(int x , int y , int larg , int haut, Canvas canvas, Context context)
    {
        this.x = x;
        this.y = y;
        this.larg = larg;
        this.haut = haut;

        for (int i = 0 ; i < listPathBtn.size() ; i++) {
            ButtonImage img = new ButtonImage(Tools.getFirstImageMot(listPathBtn.get(i), context) , x +larg / listPathBtn.size() * i , y, larg / listPathBtn.size(), haut, canvas);
            img.setId(listPathBtn.get(i));
            img.drawOn(canvas);
            lstBtn.add(img);
        }
    }

    //si la zone est cliquée renvoi l'id du bouton cliquée, sinon renvois null
    public String isClicked(int click_X, int click_Y)
    {
        if ( click_X > x && click_X < x + larg && click_Y > y && click_Y < y + haut)
        {
            for (int i = 0 ; i < lstBtn.size() ; i++)
            {
                if ( lstBtn.get(i).isClicked(click_X , click_Y))
                {
                    return  lstBtn.get(i).getId();
                }
            }
        }
        return null;
    }

}
