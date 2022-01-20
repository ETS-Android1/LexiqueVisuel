package patinaud.lexiquevisuel.ViewElement;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import patinaud.lexiquevisuel.MenuActivity;
import patinaud.lexiquevisuel.MenuJeuxActivity;
import patinaud.lexiquevisuel.MotsCategActivity;
import patinaud.lexiquevisuel.ParamActivity;
import patinaud.lexiquevisuel.R;
import patinaud.lexiquevisuel.Utils.Properties;

public class Header {

    private View viewParent;
    private final int offsetBorderHoriz = 10;
    private ButtonImage btnHome = null;
    private ButtonImage btnGame = null;
    private ButtonImage btnParam = null;
    private String titre;




    public Header(Canvas canvas, View parent, String titre, boolean displayHomeBtn) {
        init( canvas,  parent,  titre, displayHomeBtn);
    }



    private void init(Canvas canvas, View parent, String titre, boolean displayHomeBtn)
    {
        viewParent = parent;
        this.titre = titre;

        // affiche le bouton home
        if ( displayHomeBtn ) {
            btnHome = new ButtonImage(R.drawable.home_icon2, offsetBorderHoriz, 0, Properties.getHautHeader(), Properties.getHautHeader(), canvas, parent);
        }


        //affiche le bouton de jeu
        btnGame = new ButtonImage(R.drawable.game, canvas.getWidth() - ( Properties.getHautHeader() + offsetBorderHoriz ) * 2, 0, Properties.getHautHeader(), Properties.getHautHeader(), canvas, parent);

        //affiche le bouton de parametre
        btnParam = new ButtonImage(R.drawable.parametre, canvas.getWidth() - Properties.getHautHeader() - offsetBorderHoriz, 5, Properties.getHautHeader() - 10, Properties.getHautHeader() - 10, canvas, parent);

    }


    public void drawOn(Canvas canvas)
    {
        //dessine l'ecart en gris
        Paint p = new Paint();
        p.setColor( Color.rgb(200,200,200));
        canvas.drawRect(0,0, canvas.getWidth() , Properties.getHautHeader(), p);

        //desinne les boutons
        if ( btnHome != null)
        {
            btnHome.drawOn(canvas);
        }

        btnGame.drawOn(canvas);
        btnParam.drawOn(canvas);


        //Ecrit le titre
        Paint pWhite = new Paint();
        pWhite.setColor( Color.WHITE);
        pWhite.setTextSize( Properties.getHautHeader() - 40 );

        Rect bounds = new Rect();
        pWhite.getTextBounds(titre, 0, titre.length(), bounds);

        canvas.drawText(titre, canvas.getWidth() / 2 - bounds.width() / 2,Properties.getHautHeader() - 20,pWhite);
    }



    public boolean isClicked(float x, float y) {

        // Acceuil
        if( btnHome != null && btnHome.isClicked(x,y))
        {
            Intent intent = new Intent(viewParent.getContext(), MenuActivity.class);
            viewParent.getContext().startActivity(intent);
        }

        // Menu jeux
        if( btnGame != null && btnGame.isClicked(x,y))
        {
            Intent intent = new Intent(viewParent.getContext(), MenuJeuxActivity.class);
            viewParent.getContext().startActivity(intent);
        }

        // Parametres
        if( btnParam != null && btnParam.isClicked(x,y))
        {
            Intent intent = new Intent(viewParent.getContext(), ParamActivity.class);
            viewParent.getContext().startActivity(intent);
        }

        return ( y < Properties.getHautHeader()) ? true : false;
    }
}
