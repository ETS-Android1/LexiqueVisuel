package patinaud.lexiquevisuel.ViewElement;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import patinaud.lexiquevisuel.Utils.Properties;

public class ImageSwitchMelt {
    private View view;

    // Gestion de la transparence
    private int transparencyLevel = 0;
    private boolean augmenteTrans = false;
    private boolean diminueTrans = true;
    private boolean pause = false;
    private int speedTrans = 30; //vitesse de changement du niveau de transparence
    private long startPause = 0;
    private int dureePause = 5 * 1000;
    private int indexImage = 0;
    private ArrayList<ButtonImage> lstImages = new ArrayList<ButtonImage>();
    private int x,y,haut, larg;


    public ImageSwitchMelt(View view)
    {
        this.view = view;
        lstImages = new ArrayList<ButtonImage>();

        this.view.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Animation des images
                if(augmenteTrans)
                {
                    transparencyLevel = transparencyLevel - speedTrans;
                    if(transparencyLevel <= 0)
                    {
                        transparencyLevel = 0;
                        //change d'image
                        if ( ! lstImages.isEmpty())
                        {
                            indexImage++;
                            if ( indexImage >= lstImages.size() )
                            {
                                indexImage = 0;
                            }
                        }

                        augmenteTrans = false;
                        diminueTrans = true;
                    }
                }
                if ( diminueTrans)
                {
                    transparencyLevel = transparencyLevel + speedTrans;
                    if(transparencyLevel >= 255)
                    {
                        transparencyLevel = 255;
                        diminueTrans = false;
                        pause = true;
                        startPause = Calendar.getInstance().getTimeInMillis();
                    }
                }
                if ( pause) {
                    if (Calendar.getInstance().getTimeInMillis() > startPause + dureePause) //après un temp de pause lance la disparition de l'image
                    {
                        pause = false;
                        augmenteTrans = true;
                    }
                }

                view.invalidate();
                view.postDelayed(this, 100);
            }
        }, 100);
    }


    public int getNmbImg()
    {
        return lstImages.size();
    }

    public void load(String mot, String[] listImages, int x, int y , int larg, int haut, Canvas canvas, Context context) {
        this.x = x;
        this.y = y;
        this.larg = larg;
        this.haut = haut;
        for (int itImg = 0 ; itImg < listImages.length; itImg++) {
            ButtonImage img_btn = new ButtonImage(Properties.getImageMotDirrectory(mot,context) + listImages[itImg] , x,y,larg,haut,canvas);
            lstImages.add(img_btn);
        }
    }


    public void draw(Canvas canvas)
    {
        if ( ! lstImages.isEmpty()) {
            Paint alphaPaint = new Paint();
            alphaPaint.setAlpha(transparencyLevel);
            lstImages.get(indexImage).drawOn(canvas , alphaPaint);
        }
    }


    public boolean isClicked(float x, float y) {
        if ( x > this.x && x < this.x + larg && y > this.y && y < this.y+haut)
        {
            return true;
        }
        return false;
    }


}
