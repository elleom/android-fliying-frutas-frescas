package com.example.act_03_flyingfrutasfrescas;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ArrayList<Fruta> arrayFrutas = new ArrayList<>();
    private Juego miJuego;
    private int cestaX;
    private Random random = new Random();
    private boolean running = true;
    private Handler handler = new Handler();
    private int frutaY;
    private int seleccionado;
    private int frutaX;
    private int cajaY;
    private Integer relojPrincipal = 20;
    private Integer puntuacion= 0;
    private MediaPlayer mp = new MediaPlayer();
    private MediaPlayer go = new MediaPlayer();
    private MediaPlayer mpObjeto = new MediaPlayer();
    private MediaPlayer mpBomba = new MediaPlayer();
    private MediaPlayer speedUp = new MediaPlayer();
    private MediaPlayer gameloop = new MediaPlayer();
    private Rect frutaRect;
    private Rect cajaRect;
    private int controlPeso = 1;

    /*instancia las diferentes frutas con sus atributos
     */
    final Fruta banana = new Fruta("Banana",50,4,3);
    final Fruta black_berry = new Fruta("Mora",20,5,2);
    final Fruta black_berry_light = new Fruta("Mora Clara",20,5,2);
    final Fruta ciruela = new Fruta("Ciruela",12,8,2);
    final Fruta coco = new Fruta("Coco",200,2,3);
    final Fruta uvaV = new Fruta("Uva Verde",50,4,2);
    final Fruta geeen_apple = new Fruta("Manzana Verde",50,4,3);
    final Fruta limon = new Fruta("Limon",50,4,3);
    final Fruta lima = new Fruta("Lima",10,5,2);
    final Fruta naranja = new Fruta("Naranja",50,3,3);
    final Fruta durazno = new Fruta("Durazno",50,4,3);
    final Fruta pera = new Fruta("Pera",50,4,2);
    final Fruta higo = new Fruta("Higo",50,4,3);
    final Fruta fresa = new Fruta("Fresa",50,4,1);
    final Fruta manzanaR  = new Fruta("Manzana Roja",50,4,3);
    final Fruta cereza  = new Fruta("Cereza",25,5,2);
    final Fruta uvaR  = new Fruta("Uva Roja",15,5,2);
    final Fruta carambola  = new Fruta("Carambola",250,2,3);
    final Fruta sandia  = new Fruta("Sandia",300,1,5);
    final Fruta bomba = new Fruta("Bomba",-200,-5,4);

    @Override
    protected void onDestroy(){
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
        gameloop.stop();
        mp.stop();
        Log.d(TAG, "onDestroy: JUEGO TERMINADO");
                
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starts");

        super.onCreate(savedInstanceState);
        miJuego = new Juego(this);
        setContentView(miJuego);

        mp = MediaPlayer.create(this,R.raw.beep);
        go = MediaPlayer.create(this,R.raw.gameover);
        speedUp = MediaPlayer.create(this,R.raw.speed_up);
        gameloop = MediaPlayer.create(this,R.raw.gameloop);

        //define la posicion inicial de la caja
        cestaX =420;
        //define el lugar de inicio de la fruta
        frutaY = miJuego.getTop();

        //selecciona una objeto(fruta/bomba) aleatioriamente
        seleccionado = random.nextInt(23);

        //ingresa los objetos al hashmap
        arrayFrutas.add(banana);
        arrayFrutas.add(black_berry);
        arrayFrutas.add(bomba);
        arrayFrutas.add(bomba);
        arrayFrutas.add(ciruela);
        arrayFrutas.add(coco);
        arrayFrutas.add(bomba); //repite bomba
        arrayFrutas.add(bomba); //repite bomba
        arrayFrutas.add(uvaV);
        arrayFrutas.add(bomba);
        arrayFrutas.add(geeen_apple);
        arrayFrutas.add(lima);
        arrayFrutas.add(limon);
        arrayFrutas.add(bomba); //bmb
        arrayFrutas.add(bomba); //bmb
        arrayFrutas.add(naranja);
        arrayFrutas.add(durazno);
        arrayFrutas.add(pera);
        arrayFrutas.add(bomba);
        arrayFrutas.add(higo);
        arrayFrutas.add(fresa);
        arrayFrutas.add(bomba); //bmb
        arrayFrutas.add(bomba); //bmb
        arrayFrutas.add(manzanaR);
        arrayFrutas.add(cereza);
        arrayFrutas.add(uvaR);
        arrayFrutas.add(bomba);
        arrayFrutas.add(carambola);
        arrayFrutas.add(sandia);
        arrayFrutas.add(bomba);
        arrayFrutas.add(bomba);

        /***
         * asigna cada foto a su respectivo objeto
         */
        for (Fruta fruta: arrayFrutas) {
            fruta.ajustaImagen();
        }

        //inicia el sountrack

        gameloop.start();

        //mantiene el loop del soundtrack
        gameloop.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                gameloop.start();
            }
        });

        /***
         * Aumenta la velocidad de caida de los objetos
         * cada 30 segundos.
         */
        final Timer timerDificultad = new Timer();
        timerDificultad.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "run: Vuelta Timer ");

                speedUp.start();
                speedUp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //emite una advertencia
                        speedUp.stop();
                        speedUp = MediaPlayer.create(MainActivity.this,R.raw.speed_up);
                    }
                });
                controlPeso +=1;

                if (relojPrincipal <=0){
                    timerDificultad.cancel();
                }

            }

        },30000,30000);

        /**
         * activa el sonido del reloj en los
         * ultimos 4 segundos del juego.
         */
        Timer reloj = new Timer();
        reloj.schedule(new TimerTask() {
            @Override
            public void run() {
                if ( relojPrincipal > 0){
                relojPrincipal -=1;
                }
                if (relojPrincipal <= 4){
                    mp.start();

                }
                if (relojPrincipal == 0){
                    mp.stop();
                }

            }
        },0,1000);

        /**
         * refresca la imagen en pantalla
         */
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if ( relojPrincipal <= 0){
                    handler.removeCallbacks(this);
                }
                else{

                    handler.post(new Runnable(){
                        public void run(){
                            //establece la velocidad de movimiento, relativo al peso del objeto
                            frutaY +=5*arrayFrutas.get(seleccionado).getPeso()*controlPeso;
                            //refreca la pantalla
                            miJuego.invalidate();
                        }

                    });
                }



            }
        },0,20);

    }

    class Juego extends View {
        

        private Bitmap background, crate, fruta, bt_OK, bt_cancel,bt_reset;
        private Canvas canvas;

        public Juego(Context c){
            super(c);
            frutaX = random.nextInt(480);
        }

        @Override
        protected void onDraw(Canvas canvas){
            Log.d(TAG, "onDraw: starts");

            //llama a los metodos de dibujado en pantalla
            pintarFondo(canvas);
            pintarDatos(canvas);

            if (colicion(canvas)){
                //selecciona aleatoriamente un objeto
                sigObjeto();
            }
            pintarCaja(canvas);
            pintarFruta(canvas);

            //selecciona un nuevo objeto una vez que el ultimo a caido
            if (frutaY > miJuego.getBottom()) {
            sigObjeto();
            
            }
            /**
             * una vez que el juego finaliza
             * llama a los metodos de pintado de reiniciar,
             *
             */
            if (relojPrincipal <= 0){
                running = false;
                pintarGameOver(canvas);
                mp.stop();
                go.start();
                go.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //cancela los sonidos utilizados hasta el momento
                        //salvo el soundtrac principal
                        mp.stop();
                        speedUp.stop();
                    }
                });

                reiniciarJuego(canvas);

            }
            Log.d(TAG, "onDraw: ends");
        }

        public void pintarFondo(Canvas canvas){
            Log.d(TAG, "pintarFondo: llamo");
            background = BitmapFactory.decodeResource(getResources(),R.drawable.forest_back);
            Bitmap fit_back=Bitmap.createScaledBitmap(background,getWidth(), getHeight(),true);

            canvas.drawBitmap(fit_back,0,0,null);
        }

        public void pintarCaja(Canvas canvas) {
            Log.d(TAG, "pintarCaja: llamado");
            crate = BitmapFactory.decodeResource(getResources(),R.drawable.crate);

            canvas.drawBitmap(crate,cestaX,getHeight()-250,null);
        }
        public void pintarFruta(Canvas canvas){
            Log.d(TAG, "pintarFruta: llamado");

            fruta = BitmapFactory.decodeResource(getResources(),arrayFrutas.get(seleccionado).getImagen());

            canvas.drawBitmap(fruta, frutaX-50,frutaY,null);
        }

        public void pintarDatos(Canvas canvas){
            Log.d(TAG, "pintarDatos: llamado");

            //setea un tipo de fuente custom para el reloj y la puntuacion
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fuente.ttf");

            Paint reloj = new Paint();
            Paint puntos = new Paint();

            //alinea el reloj a la izq (de acuerdo a su pos en X)
            reloj.setTextAlign(Paint.Align.LEFT);
            //alinea el puntaje a la izq (de acuerdo a su pos en X)
            puntos.setTextAlign(Paint.Align.RIGHT);

            reloj.setTextSize(100);
            reloj.setColor(Color.WHITE);

            //cambia el color del reloj de acuerdo al tiempo restante
            if (relojPrincipal >= 10){
                reloj.setColor(Color.WHITE);
            }
            else if (relojPrincipal < 10 && relojPrincipal >=5){
                reloj.setColor(Color.YELLOW);
            }
            else{
                reloj.setColor(Color.RED);
            }

            puntos.setTextSize(100);
            puntos.setColor(Color.WHITE);

            //establece la fuente
            reloj.setTypeface(typeface);
            puntos.setTypeface(typeface);
            canvas.drawText(relojPrincipal.toString(),getLeft()+50,150,reloj);
            canvas.drawText(puntuacion.toString(), getRight()-50,150,puntos);


        }

        public void pintarGameOver(Canvas canvas){
            Log.d(TAG, "pintarGameOver: llamada");

            //muestra un msj de juego terminado
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/gameover.ttf");
            Paint gameover = new Paint();
            gameover.setTypeface(typeface);
            gameover.setColor(Color.WHITE);
            gameover.setTextAlign(Paint.Align.CENTER);
            gameover.setTextSize(100);

            canvas.drawText("GAME OVER",getWidth()/2,getBottom()/2,gameover);

        }

        //captura las pulsaciones sobre la pantalla
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Log.d(TAG, "onTouchEvent: starts");

            //modifica la posicion de la caja
            if (event.getAction()== MotionEvent.ACTION_DOWN){
                float x = event.getX();
                if (x>cestaX){
                    cestaX = cestaX+50;
                }
                else{
                    cestaX = cestaX -50;
                }
            }
            invalidate();
            return true;
        }

        //comprueba si hay colicion de la caja con los pbjetos
        private boolean colicion(Canvas canvas){
            Log.d(TAG, "colicion: Llamado");

            frutaRect = new Rect(frutaX+25,frutaY,frutaX+75,frutaY+50);
            cajaRect = new Rect(cestaX+50,getHeight()-250,cestaX+200,getHeight()-200);

            if ( Rect.intersects(frutaRect,cajaRect)){
                puntuacion += arrayFrutas.get(seleccionado).getValor();
                relojPrincipal += arrayFrutas.get(seleccionado).getTiempo();
                sonidoColision();

                //controla que la puntuacion no baj de 0;
                if ( puntuacion < 0){
                    puntuacion = 0;
                }

                return true;
            }
            else{
                return false;
            }

        }

        //selecciona un nuevo objeto
        public void sigObjeto(){
            Log.d(TAG, "sigObjeto:  llamado");
            frutaY = miJuego.getTop();
            seleccionado = random.nextInt(arrayFrutas.size());
            frutaX = random.nextInt(getRight());
        }

        //genera un sonido de acuerdo al tipo de objeto (fruta o bomba)
        public void sonidoColision(){
            Log.d(TAG, "sonidoColision: llamado");
            mpObjeto = MediaPlayer.create(MainActivity.this, R.raw.fruit);
            mpBomba = MediaPlayer.create(MainActivity.this, R.raw.bomb);

            if ( arrayFrutas.get(seleccionado).getNombre() == "Bomba"){
                mpBomba.start();
                mpBomba.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mpBomba.stop();
                    }
                });
            }
            else{
                mpObjeto.start();
                mpObjeto.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mpObjeto.stop();
                    }
                });
            }

        }

        /**
         * muestra un "cuadro de dialogo donde poder reiniciar el juego o cerrarlo.
         * @param canvas
         */
        public void reiniciarJuego(Canvas canvas){
            Log.d(TAG, "reiniciarJuego: llamado");

            bt_OK = BitmapFactory.decodeResource(getResources(), R.drawable.ok);
            bt_cancel = BitmapFactory.decodeResource(getResources(), R.drawable.cancel);
            bt_reset = BitmapFactory.decodeResource(getResources(), R.drawable.reset);

            canvas.drawBitmap(bt_OK,getWidth()/4,getBottom()/2+100,null);
            canvas.drawBitmap(bt_cancel,getWidth()/2+50,getBottom()/2+100,null);
            canvas.drawBitmap(bt_reset,(getWidth()/2)/2,getBottom()/2-400,null);


            //captura la coordenada de la pulsacion
            setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){

                        if (event.getX() >= getWidth()/4
                                && event.getX() <= (getWidth()/4)+200
                                && event.getY() >= getBottom()/2+200
                                && event.getY() <= getBottom()/2+300 ){

                            gameloop.stop();
                            Intent juegoPrincipal = getIntent();
                            finish();
                            startActivity(juegoPrincipal);

                        }
                        else if (event.getX() >= (getWidth()/4)*2
                                && event.getX() <= (getWidth()/4)*2+200
                                && event.getY() >= getBottom()/2+200
                                && event.getY() <= getBottom()/2+300){

                            gameloop.stop();
                            finish();

                        }
                    }
                    return true;
                }
            });


        }

    }//Juego
}//class
