package EncriptadoClavePublicaPrivada.GeneradorDeClavesRSA;

import EncriptadoClavePublicaPrivada.GestoraFicheros.GestionFicheros;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class GeneradorDeClaves {
    private static final String ALGORITMO_CLAVE_PUBLICA = "RSA";
    private static final int TAM_CLAVE = 1024;
    public static SecureRandom srand;


    /**
     *
     * @return
     */
    public KeyPair generarParClaves(String nomFichClavePublica, String nomFichClavePrivada){
        KeyPair parClaves = null;
        try{
            srand = SecureRandom.getInstanceStrong();
            KeyPairGenerator genParClaves = KeyPairGenerator.getInstance(ALGORITMO_CLAVE_PUBLICA);
            genParClaves.initialize(TAM_CLAVE, srand);
            parClaves = genParClaves.generateKeyPair();
            PublicKey cPublica = parClaves.getPublic();
            PrivateKey cPrivada = parClaves.getPrivate();
            GestionFicheros.claveAFichero(GestionFicheros.generarRuta(GestionFicheros.RUTA_FICH_CLAVE+nomFichClavePublica, 1),cPublica,false);
            GestionFicheros.claveAFichero(GestionFicheros.generarRuta(GestionFicheros.RUTA_FICH_CLAVE+nomFichClavePrivada, 2), cPrivada, true);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No se encontró el algoritmo de generacion de números aleatorios");
        }
        return parClaves;
    }



}
