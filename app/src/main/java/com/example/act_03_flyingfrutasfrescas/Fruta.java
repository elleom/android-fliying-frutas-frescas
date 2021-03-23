package com.example.act_03_flyingfrutasfrescas;

public final class Fruta {

    private int imagen;
    private String nombre;
    private int valor;
    private int tiempo;
    private int peso;


    public Fruta(String nombre, int valor, int tiempo, int peso) {
        this.nombre = nombre;
        this.valor = valor;
        this.tiempo = tiempo;
        this.peso = peso;
    }
    public String getNombre() {
        return nombre;
    }

    private void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void ajustaImagen(){

        switch (nombre){

            case "Banana":
                setImagen(R.drawable.banana);
                break;
            case "Mora":
                setImagen(R.drawable.black_berry_dark);
            case "Mora Clara":
                setImagen(R.drawable.black_berry_light);
                break;
            case "Ciruela":
                setImagen(R.drawable.black_cherry);
                break;
            case "Coco":
                setImagen(R.drawable.coconut);
                break;
            case "Manzana Verde":
                setImagen(R.drawable.green_apple);
                break;
            case "Uva Verde":
                setImagen(R.drawable.green_grape);
                break;
            case "Limon":
                setImagen(R.drawable.lemon);
                break;
            case "Lima":
                setImagen(R.drawable.lime);
                break;
            case "Naranja":
                setImagen(R.drawable.orange);
                break;
            case "Durazno":
                setImagen(R.drawable.peach);
                break;
            case "Pera":
                setImagen(R.drawable.pear);
                break;
            case "Higo":
                setImagen(R.drawable.plum);
                break;
            case "Fresa":
                setImagen(R.drawable.raspberry);
                break;
            case "Manzana Roja":
                setImagen(R.drawable.red_apple);
                break;
            case "Cereza":
                setImagen(R.drawable.red_cherry);
                break;
            case "Uva Roja":
                setImagen(R.drawable.red_grape);
                break;
            case "Carambola":
                setImagen(R.drawable.star_fruit);
                break;
            case "Sandia":
                setImagen(R.drawable.watermelon);
                break;
            case "Bomba":
                setImagen(R.drawable.bomba);
                break;



        }
    }
}
