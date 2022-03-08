package Encriptado3DES.GestionFicheros;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;

public class Ficheros {

    private static final String ALGORITMO_CLAVE_SIMETRICA_DESede = "DESede";// 3DES

    public static final String NOM_FICH_CLAVE = "C:\\Users\\GL512\\IdeaProjects\\EncriptadoDESede\\src\\Encriptado3DES\\GeneracionClaves\\Claves\\";


    public static String generarRuta(String nomFichClave) {
        StringBuilder stb = new StringBuilder(NOM_FICH_CLAVE);
        stb.append(nomFichClave);
        return stb.toString();
    }


    /**
     *
      * @param ruta
     * @param accion sirve para saber si queremos
     *               leer una clave o un fichero.
     *               En caso de ser 1 se leera un fichero con clave,
     *               en cualquier otro caso se dejar´ña la ruta tal cual
     * @return
     */
    public static byte[] ficheroEnBytes(String ruta, int accion){
        byte[] valorFichero = null;
        if(accion == 1){
            ruta = generarRuta(ruta);
        }
        try(FileInputStream fis = new FileInputStream(ruta)) {
            valorFichero = fis.readAllBytes();
        }catch(FileNotFoundException e){
            System.out.printf("No existe el fichero de ruta %s", ruta);
        }catch(IOException r){
            System.out.println("Error de E/S leyendo el fichero");
        }
        return valorFichero;
    }


    public static String escribirFicheroClave(byte[] valorClave, String ficheroClave){
        try (FileOutputStream fos = new FileOutputStream(ficheroClave)) {
            fos.write(valorClave);
        } catch (IOException e) {
            System.out.println("Error de E/S escribiendo clave en fichero");
        }
        return ficheroClave;
    }

    public static String escribirFichero(String rutaFichero, String modo, Cipher cifrado){

        try (FileInputStream fis = new FileInputStream(rutaFichero);
             FileOutputStream fos = new FileOutputStream(rutaFichero + modo);
             BufferedInputStream is = new BufferedInputStream(fis);
             BufferedOutputStream os = new BufferedOutputStream(fos)) {
            byte[] buff = new byte[cifrado.getBlockSize()];
            int bytesLeidos;
            while ((bytesLeidos = is.read(buff)) != -1) {
                os.write(cifrado.update(buff, 0, bytesLeidos));
            }
            os.write(cifrado.doFinal());
        } catch (IllegalBlockSizeException e) {
            System.out.println("Tamaño de bloque no válido.");
        } catch (BadPaddingException e) {
            System.out.println("Excepción con relleno.");
        } catch (IOException e) {
            System.out.println("ERROR: de E/S encriptando fichero");
        }
        return rutaFichero + modo;
    }
}
