package EncriptadoClavePublicaPrivada.CifradoRSA;

import EncriptadoClavePublicaPrivada.GestoraFicheros.GestionFicheros;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Encriptador {
    private static final String ALGORITMO_CLAVE_PUBLICA = "RSA";
    private static final String FICH_CLAVE_PUB = ".der";
    public static final String RUTA_CLAVES = "C:\\Users\\GL512\\IdeaProjects\\EncriptadoDESede\\src\\EncriptadoClavePublicaPrivada\\GeneradorDeClavesRSA\\Claves\\";

    /**
     * @param nomFichClave
     * @param rutaFichero
     */
    public void encriptarFichero(String nomFichClave, String rutaFichero) {
        //C:\Users\caguilar.INFO2\IdeaProjects\ClavePublicaPrivada\src\GeneracionClaveRSA\Claves
        KeyFactory factory;
        try {
            byte[] contenidoFichero = GestionFicheros.bytesDeFichero(rutaFichero);
            byte[] clavePubCodif = GestionFicheros.decodificarEnBase64(RUTA_CLAVES + nomFichClave + FICH_CLAVE_PUB);

            factory = KeyFactory.getInstance(ALGORITMO_CLAVE_PUBLICA);
            X509EncodedKeySpec pKeySpec = new X509EncodedKeySpec(clavePubCodif);
            PublicKey clavePublica = factory.generatePublic(pKeySpec);

            Cipher cifrado = getPublicCipher(clavePublica);


            byte[] mensajeCifrado = cifrado.doFinal(contenidoFichero);

            String mensajeBase64 = GestionFicheros.codificarEnBase64(mensajeCifrado);

            System.out.printf("Fichero cifrado codificado en base 64 como texto:\n%s\nEsta información fué guardada en el fichero %s",
                    mensajeBase64.replaceAll("(.{76})", "$1\n"), rutaFichero+".encrypt");

            GestionFicheros.escribirCadenaEnFichero(rutaFichero, ".encrypt", mensajeBase64);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No existe el algoritmo");
        } catch (InvalidKeySpecException e) {
            System.out.println("Las especificaciones de la clave publica no son exactas");
        } catch (IllegalBlockSizeException e) {
            System.out.println("Numero ilegal de bloques");
        } catch (BadPaddingException e) {
            System.out.println("Problemas con el padding");
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Error de Entrada/Salida");
        }
    }


    /**
     * @param nomFichClave
     * @param rutaFichero
     */
    public void desencriptarFichero(String nomFichClave, String rutaFichero) {

        KeyFactory factory;
        try {
            byte[] clavePrivCodificada = GestionFicheros.decodificarEnBase64(GestionFicheros.generarRuta(RUTA_CLAVES+nomFichClave, 2));

            factory = KeyFactory.getInstance(ALGORITMO_CLAVE_PUBLICA);
            PKCS8EncodedKeySpec pKSpec = new PKCS8EncodedKeySpec(clavePrivCodificada);
            PrivateKey clavePrivada = factory.generatePrivate(pKSpec);

            Cipher cifrado = Cipher.getInstance(ALGORITMO_CLAVE_PUBLICA);
            cifrado.init(Cipher.DECRYPT_MODE, clavePrivada);
            byte[] mensajeCifrado = GestionFicheros.decodificarEnBase64(rutaFichero);
            byte[] mensajeDescifrado = cifrado.doFinal(mensajeCifrado);
            GestionFicheros.escribirBytesEnFichero(rutaFichero, ".desencrypt", mensajeDescifrado);

            System.out.printf("Fichero descifrado:\n%s\nEn Fichero %s", new String(mensajeDescifrado, "UTF-8"), rutaFichero+".desencrypt");

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No existe algoritmo");
        } catch (InvalidKeySpecException e) {
            System.out.println("Clave publica inválida");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("No se puede obtener el valor en Base64");
        } catch (BadPaddingException e) {
            System.out.println("No tiene padding");
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado");
        } catch (IOException e) {
            System.out.println("Error de Entrada/Salida");
        } catch (NoSuchPaddingException e) {
            System.out.println("Las especificaciones no son correctas");
        } catch (InvalidKeyException e) {
            System.out.println("Clave no Válida");
        }
    }

    /**
     * @param clavePublica
     * @return
     */
    private Cipher getPublicCipher(PublicKey clavePublica) {
        Cipher cifrado = null;
        try {
            cifrado = Cipher.getInstance("RSA");
            cifrado.init(Cipher.ENCRYPT_MODE, clavePublica);
        } catch (NoSuchPaddingException e) {
            System.out.println("No existe padding");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("El algoritmo de encriptacion no existe");
        } catch (InvalidKeyException e) {
            System.out.println("clave no válida");
        }
        return cifrado;
    }

    /**
     * @param clavePrivada
     * @return
     */
    private Cipher getPrivateCipher(PrivateKey clavePrivada) {
        Cipher cifrado = null;
        try {
            cifrado = Cipher.getInstance(ALGORITMO_CLAVE_PUBLICA);
            cifrado.init(Cipher.DECRYPT_MODE, clavePrivada);
        } catch (NoSuchPaddingException e) {
            System.out.println("No existe padding");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("El algoritmo elegido no existe");
        } catch (InvalidKeyException e) {
            System.out.println("La clave pública no es válida para este fichero");
        }
        return cifrado;
    }


}
