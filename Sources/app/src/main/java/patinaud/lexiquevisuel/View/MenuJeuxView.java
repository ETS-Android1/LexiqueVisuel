package patinaud.lexiquevisuel.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import patinaud.lexiquevisuel.Game1ImageActivity;
import patinaud.lexiquevisuel.Game1SonActivity;
import patinaud.lexiquevisuel.MenuActivity;
import patinaud.lexiquevisuel.R;
import patinaud.lexiquevisuel.Utils.Properties;
import patinaud.lexiquevisuel.ViewElement.ButtonImage;
import patinaud.lexiquevisuel.ViewElement.Header;

public class MenuJeuxView extends View implements View.OnTouchListener  {

    private Canvas canvas;
    private Header header;
    private ButtonImage btnGame1Img = null;
    private ButtonImage btnGame1Son = null;

    public MenuJeuxView(Activity activity)
    {
        super((Context)activity);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("MenuJeuxView",    event.getAction() + ""  );

        if ( event.getAction() == MotionEvent.ACTION_DOWN) {
            if (header.isClicked(event.getX(), event.getY())) {
                //Do nothing
            }
            if (btnGame1Img != null && btnGame1Img.isClicked(event.getX(), event.getY())) {
                Intent intent = new Intent(this.getContext(), Game1ImageActivity.class);
                this.getContext().startActivity(intent);
            }
            if (btnGame1Son != null && btnGame1Son.isClicked(event.getX(), event.getY())) {
                Intent intent = new Intent(this.getContext(), Game1SonActivity.class);
                this.getContext().startActivity(intent);
            }
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
            header  = new Header(canvas, this, "Jeux", true);
        }

        header.drawOn(canvas);
    }



    private void drawVertical(Canvas canvas)
    {

        //Charge les bouttons
        if ( btnGame1Img == null) {
            btnGame1Img = new ButtonImage(R.drawable.game_1image_4son, 0, Properties.getHautHeader(), canvas.getWidth(), (canvas.getHeight() - Properties.getHautHeader()) / 2, canvas, this);
        }
        if ( btnGame1Son == null) {
            btnGame1Son = new ButtonImage(R.drawable.game_1son_4images, 0, Properties.getHautHeader() + (canvas.getHeight() - Properties.getHautHeader()) / 2, canvas.getWidth(), (canvas.getHeight() - Properties.getHautHeader()) / 2, canvas, this);
        }


        btnGame1Img.drawOn(canvas);
        btnGame1Son.drawOn(canvas);


        //Dessine le trait de séparation entre les deux images
        Paint pGray = new Paint();
        pGray.setColor(Color.GRAY);
        pGray.setStrokeWidth(5);
        canvas.drawLine(0, Properties.getHautHeader() + (canvas.getHeight() - Properties.getHautHeader()) / 2, canvas.getWidth(),  Properties.getHautHeader() + (canvas.getHeight() - Properties.getHautHeader()) / 2, pGray);

    }




    private void drawHorizontal(Canvas canvas)
    {

        //Charge les bouttons
        if ( btnGame1Img == null) {
            btnGame1Img = new ButtonImage(R.drawable.game_1image_4son, 0, Properties.getHautHeader(), canvas.getWidth() / 2, canvas.getHeight() - Properties.getHautHeader(), canvas, this);
        }
        if ( btnGame1Son == null) {
            btnGame1Son = new ButtonImage(R.drawable.game_1son_4images, canvas.getWidth() / 2, Properties.getHautHeader() , canvas.getWidth() / 2, canvas.getHeight() - Properties.getHautHeader(), canvas, this);
        }


        btnGame1Img.drawOn(canvas);
        btnGame1Son.drawOn(canvas);


        //Dessine le trait de séparation entre les deux images
        Paint pGray = new Paint();
        pGray.setColor(Color.GRAY);
        pGray.setStrokeWidth(5);
        canvas.drawLine(canvas.getWidth() / 2, Properties.getHautHeader(), canvas.getWidth() / 2,  canvas.getHeight(), pGray);

    }

}
