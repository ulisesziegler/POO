public class Main {

    public static void main(String[] args) {

        /*

        Pantalla pantalla = new Pantalla(55, "4K");

        Televisor tv1 = new Televisor(
                "Samsung",
                "Crystal",
                2023,
                'O',
                true,
                pantalla
        );

        tv1.mostrarDatos();
    }
    */



    //ejercicio 2

        Pantalla pantalla = new Pantalla(55, "4K");

        Televisor tv1 = new Televisor(
                "Samsung",
                "Crystal",
                2023,
                'O',
                true,
                pantalla
        );

        Televisor tv2 = new Televisor(   "LG",     "AI ThinQ",    2024,    'L',  true, pantalla);
        System.out.println("");
        tv1.mostrarDatos();
        System.out.println("");
        tv2.mostrarDatos();

        // modificamos resoloción
        pantalla.resolucion = "1080p";

        System.out.println("Después del cambio:");

        System.out.println("");
        System.out.println("Televisión 1");
        tv1.mostrarDatos();
        System.out.println("");
        tv2.mostrarDatos();
    }





        /* ejercicio 3
        Pantalla pantalla = new Pantalla(55, "4K");

        Televisor tv1 = new Televisor("Samsung","Series 9", 2023, 'O',true,pantalla);

        System.out.println("Estado inicial:");
        tv1.mostrarEstado();

        tv1.encender();

        System.out.println("Después de encender:");
        tv1.mostrarEstado();

        tv1.apagar();

        System.out.println("Después de apagar:");
        tv1.mostrarEstado();
    }*/

}

