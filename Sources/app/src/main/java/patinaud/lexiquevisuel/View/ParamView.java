package patinaud.lexiquevisuel.View;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import patinaud.lexiquevisuel.R;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.Utils.Utilisateur;
import patinaud.lexiquevisuel.ViewElement.ButtonImage;
import patinaud.lexiquevisuel.ViewElement.Header;

public class ParamView extends View implements View.OnTouchListener  {

    private Canvas canvas;
    private Header header;
    private ButtonImage btnDifficulteFacile;
    private ButtonImage btnDifficulteMoyenne;
    private ButtonImage btnDifficulteDure;

    public ParamView(Activity activity)
    {
        super((Context)activity);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("ParamView",    event.getAction() + ""  );

        if ( event.getAction() == MotionEvent.ACTION_DOWN) {
            if (header.isClicked(event.getX(), event.getY())) {
                //Do nothing
            }

            if (btnDifficulteFacile != null && btnDifficulteFacile.isClicked(event.getX(), event.getY())) {
                Utilisateur.saveDifficulty(1 , this.getContext());
            }
            if (btnDifficulteMoyenne != null && btnDifficulteMoyenne.isClicked(event.getX(), event.getY())) {
                Utilisateur.saveDifficulty(2 , this.getContext());
            }
            if (btnDifficulteDure != null && btnDifficulteDure.isClicked(event.getX(), event.getY())) {
                Utilisateur.saveDifficulty(3 , this.getContext());
            }
            this.invalidate();
        }

        return true;
    }

    public void clickAction(MotionEvent event) {   }



    //Permet de gérer les scroll vers le bas
    public void moveAction(MotionEvent event)
    {
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        //dessine le fond en blanc
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawPaint(p);

        //Dessine la page dans le sens vertical ou horizontal
        if ( canvas.getHeight() > canvas.getWidth() )
        {
            drawVertical(canvas);
        }
        else
        {
            drawHorizontal(canvas);
        }


        //termine par dessiner le header
        if ( header == null)
        {
            header  = new Header(canvas, this, "Difficulté", true);
        }

        header.drawOn(canvas);
    }



    private void drawVertical(Canvas canvas)
    {

        //Charge les bouttons
        if ( btnDifficulteFacile == null) {
            btnDifficulteFacile = new ButtonImage(R.drawable.faible, 0, Properties.getHautHeader(), canvas.getWidth(), (canvas.getHeight() - Properties.getHautHeader()) / 3, canvas, this);
        }
        if ( btnDifficulteMoyenne == null) {
            btnDifficulteMoyenne = new ButtonImage(R.drawable.moyen, 0, Properties.getHautHeader() + (canvas.getHeight() - Properties.getHautHeader()) / 3, canvas.getWidth(), (canvas.getHeight() - Properties.getHautHeader()) / 3, canvas, this);
        }
        if ( btnDifficulteDure == null) {
            btnDifficulteDure = new ButtonImage(R.drawable.fort, 0, Properties.getHautHeader() + (canvas.getHeight() - Properties.getHautHeader()) / 3 * 2, canvas.getWidth(), (canvas.getHeight() - Properties.getHautHeader()) / 3, canvas, this);
        }


        //Dessine le fond du bouton selectionne
        Paint pinceau = new Paint();
        pinceau.setColor(Color.GRAY);
        pinceau.setStrokeWidth(5);

        ButtonImage btnAEncadrer = btnDifficulteFacile;


        if ( Utilisateur.getNiveauDifficulte(this.getContext()) == 2 )
        {
            btnAEncadrer = btnDifficulteMoyenne;
        }

        if ( Utilisateur.getNiveauDifficulte(this.getContext()) == 3 )
        {
            btnAEncadrer = btnDifficulteDure;
        }

        canvas.drawRoundRect(  btnAEncadrer.getOffsetX() + btnAEncadrer.getX(), btnAEncadrer.getOffsetY() + btnAEncadrer.getY(), btnAEncadrer.getOffsetX() + btnAEncadrer.getX() + btnAEncadrer.getWidth(), btnAEncadrer.getOffsetY() + btnAEncadrer.getY() + btnAEncadrer.getHeight() , 150 , 150 , pinceau);


        // dessine les bouttons
        btnDifficulteFacile.drawOn(canvas);
        btnDifficulteMoyenne.drawOn(canvas);
        btnDifficulteDure.drawOn(canvas);

    }




    private void drawHorizontal(Canvas canvas)
    {

        //Charge les bouttons
        if ( btnDifficulteFacile == null) {
            btnDifficulteFacile = new ButtonImage(R.drawable.faible, 0, Properties.getHautHeader(), canvas.getWidth() / 3, canvas.getHeight() - Properties.getHautHeader(), canvas, this);
        }
        if ( btnDifficulteMoyenne == null) {
            btnDifficulteMoyenne = new ButtonImage(R.drawable.moyen, canvas.getWidth() / 3, Properties.getHautHeader(), canvas.getWidth() / 3, canvas.getHeight() - Properties.getHautHeader(), canvas, this);
        }
        if ( btnDifficulteDure == null) {
            btnDifficulteDure = new ButtonImage(R.drawable.fort, canvas.getWidth() / 3 * 2, Properties.getHautHeader(), canvas.getWidth() / 3, canvas.getHeight() - Properties.getHautHeader(), canvas, this);
        }


        //Dessine le fond du bouton selectionne
        Paint pinceau = new Paint();
        pinceau.setColor(Color.GRAY);
        pinceau.setStrokeWidth(5);

        ButtonImage btnAEncadrer = btnDifficulteFacile;


        if ( Utilisateur.getNiveauDifficulte(this.getContext()) == 2 )
        {
            btnAEncadrer = btnDifficulteMoyenne;
        }

        if ( Utilisateur.getNiveauDifficulte(this.getContext()) == 3 )
        {
            btnAEncadrer = btnDifficulteDure;
        }

        canvas.drawRoundRect(  btnAEncadrer.getOffsetX() + btnAEncadrer.getX(), btnAEncadrer.getOffsetY() + btnAEncadrer.getY(), btnAEncadrer.getOffsetX() + btnAEncadrer.getX() + btnAEncadrer.getWidth(), btnAEncadrer.getOffsetY() + btnAEncadrer.getY() + btnAEncadrer.getHeight() , 150 , 150 , pinceau);


        // dessine les bouttons
        btnDifficulteFacile.drawOn(canvas);
        btnDifficulteMoyenne.drawOn(canvas);
        btnDifficulteDure.drawOn(canvas);

    }

}
