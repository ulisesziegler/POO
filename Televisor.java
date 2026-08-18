public class Televisor {

    String marca;
    String modelo;
    int anio;
    char tipo;
    boolean inteligente;
    boolean encendido;

    Pantalla pantalla;

    public Televisor(String marca, String modelo, int anio,
                     char tipo, boolean inteligente, Pantalla pantalla) {

        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tipo = tipo;
        this.inteligente = inteligente;
        this.pantalla = pantalla;

        encendido = false;
    }

    public void encender() {

        encendido = true;
        System.out.println("Televisor encendido");
    }

    public void apagar() {

        encendido = false;
        System.out.println("Televisor apagado");
    }

    public void mostrarEstado() {

        System.out.println("Televisor: " + encendido);
    }

    public void mostrarDatos() {

        System.out.println(marca + " " + modelo);
        System.out.println(anio + " - Tipo: " + tipo);
        System.out.println(pantalla.pulgadas + " pulgadas");
        System.out.println(pantalla.resolucion);
        System.out.println("Smart: " + inteligente);
    }
}