package Main;

import Cifrado.Cifrado3DES;
import GeneracionClaves.Claves3DES;

import java.util.Scanner;

public class Main {

    public static final String PEDIR_RUTA_ARCHIVO = "Escriba la ruta completa del archivo a encriptar";
    public static final String PEDIR_RUTA_ARCHIVO2 = "Escriba la ruta completa del archivo a desencriptar";
    public static final String PEDIR_NOMARCHIVO2 = "Escribe el nombre del fichero donde se encuentra la clave";

    public static final String PEDIR_NOMARCHIVO = "Escribe el nombre del fichero donde quieres que se genere la clave";

    public static final String MENU_ALGORITMO = "Elige una opcion\n" +
            "1 -> Generar Clave\n" +
            "2 -> Encriptar\n" +
            "3 -> Desencriptar\n" +
            "0 -> Salir";
    public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);
    menu(sc);
    }




    /**
     * @param sc
     * @param peticion
     * @return
     */
    private static String pedir(Scanner sc, String peticion) {
        System.out.println(peticion);
        return sc.nextLine();
    }
    private static void menu(Scanner sc) {
        int eleccion;
        do {
            System.out.println(MENU_ALGORITMO);
            String input = sc.nextLine();
            try {
                eleccion = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("No es una opcion válida");
                eleccion = -1;
            }
            switch (eleccion) {
                case 0:
                    System.out.println("Bye!");
                    break;
                case 1:
                    opcion1(sc);
                    break;
                case 2:
                    opcion2(sc);
                    break;
                case 3:
                    opcion3(sc);
            }
        } while (eleccion != 0);

    }

    /**
     * Desde aquí se llama al método que pedirá al usuario
     * introducir el tipo de clave de cifrado que prefiere
     * para su fichero.
     *
     * @param sc
     */
    private static void opcion1(Scanner sc) {
        String eleccion = pedir(sc, PEDIR_NOMARCHIVO);
        Claves3DES.generarClave(eleccion);

        }

    private static void opcion2(Scanner sc) {

                Cifrado3DES.encriptar(pedir(sc, PEDIR_RUTA_ARCHIVO), pedir(sc, PEDIR_NOMARCHIVO2), 1);

        }

    private static void opcion3(Scanner sc) {

        Cifrado3DES.encriptar(pedir(sc, PEDIR_RUTA_ARCHIVO2), pedir(sc, PEDIR_NOMARCHIVO2), 2);

    }


}

