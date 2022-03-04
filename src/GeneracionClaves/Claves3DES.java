package GeneracionClaves;

import GestionFicheros.Ficheros;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import static javax.security.auth.callback.ConfirmationCallback.NO;

public class Claves3DES {

    private static final String ALGORITMO_GEN_NUM_ALEAT = "SHA1PRNG";
    public static final String ALGORITMO_CLAVE_SIMETRICA_DESede = "DESede";// 3DES


    public static String generarClave(String nombreFichero){
        try{
            KeyGenerator genClaves = KeyGenerator.getInstance(ALGORITMO_CLAVE_SIMETRICA_DESede);

            SecureRandom srand = SecureRandom.getInstance(ALGORITMO_GEN_NUM_ALEAT);

            genClaves.init(srand);

            SecretKey clave = genClaves.generateKey();

            System.out.printf("Formato de clave: %s\n", clave.getFormat());

            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITMO_CLAVE_SIMETRICA_DESede);

            KeySpec keySpec = null;

            byte[] valorClave = null;

            keySpec = factory.getKeySpec(clave, DESedeKeySpec.class);
            valorClave = ((DESedeKeySpec) keySpec).getKey();

            System.out.printf("Longitud de clave: %d bits\n", valorClave.length * 8);
            System.out.printf("Valor de la clave: [%s] en fichero %s",
                    valorHexadecimal(valorClave), Ficheros.NOM_FICH_CLAVE + nombreFichero);
            Ficheros.escribirFicheroClave(valorClave,Ficheros.NOM_FICH_CLAVE + nombreFichero + ".txt");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No existe el algoritmo especificado");
        } catch (InvalidKeySpecException e) {
            System.out.println("Clave no válida");
        }
        nombreFichero = Ficheros.NOM_FICH_CLAVE + nombreFichero + ".txt";
        return nombreFichero;
    }
    static String valorHexadecimal(byte[] bytes) {
        String result = "";
        for (byte b : bytes) {
            result += String.format(String.format("%x", b));
        }
        return result;
    }


}
