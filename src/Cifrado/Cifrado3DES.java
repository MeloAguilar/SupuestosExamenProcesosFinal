package Cifrado;

import GeneracionClaves.Claves3DES;
import GestionFicheros.Ficheros;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static GeneracionClaves.Claves3DES.ALGORITMO_CLAVE_SIMETRICA_DESede;
import static GestionFicheros.Ficheros.NOM_FICH_CLAVE;
import static GestionFicheros.Ficheros.escribirFichero;

public class Cifrado3DES {

    public static String encriptar(String rutaFich, String nomFichClave, int accion){
        nomFichClave = NOM_FICH_CLAVE + nomFichClave + ".txt";

        String extension = null;
        byte[] valorClave = Ficheros.ficheroEnBytes(nomFichClave, 0);
        String modo = null;
        try{
            SecretKeySpec keySpec = new SecretKeySpec(valorClave, ALGORITMO_CLAVE_SIMETRICA_DESede);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITMO_CLAVE_SIMETRICA_DESede);
            SecretKey clave = keyFactory.generateSecret(keySpec);
            Cipher cifrado = Cipher.getInstance(ALGORITMO_CLAVE_SIMETRICA_DESede);
            if (accion == 1) {
                cifrado.init(Cipher.ENCRYPT_MODE, clave);
                extension = ".encript";
                modo = "encriptado";
            } else {
                cifrado.init(Cipher.DECRYPT_MODE, clave);
                extension = ".desencript";
                modo = "desencriptado";
            }

            rutaFich = escribirFichero(rutaFich, extension, cifrado );
            System.out.printf("El archivo fue %s en el fichero %s\n", modo, rutaFich);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No existe el algoritmo especificado");
        } catch (InvalidKeySpecException e) {
            System.out.println("La clave no corresponde con las especificaciones de descifrado para el fichero");
        } catch (InvalidKeyException e) {
            System.out.println("Las especificaciones de la clave no son válidas");
        }
        return nomFichClave;
    }
}
