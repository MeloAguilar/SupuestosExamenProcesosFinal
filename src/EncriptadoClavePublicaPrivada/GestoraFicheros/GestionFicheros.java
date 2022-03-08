package EncriptadoClavePublicaPrivada.GestoraFicheros;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class GestionFicheros {
    private static final String ALGORITMO_DE_FIRMA = "DSA";

    private static final String ALGORITMO_CLAVE_PUBLICA = "RSA";
    private static final String NOM_FICH_CLAVE_PUBLICA = ".der";
    private static final String NOM_FICH_CLAVE_PRIVADA = ".pkcs8";
    //Instituto 1º ->
    // Instituto 2º -> C:\Users\caguilar.INFO2\IdeaProjects\ClavePublicaPrivada\src\GeneracionClaveRSA\Claves\
    //Casa -> C:\Users\GL512\IdeaProjects\ClavePublicaPrivada\src\GeneracionClaveRSA\Claves\
    public static final String RUTA_FICH_CLAVE = "C:\\Users\\GL512\\IdeaProjects\\EncriptadoDESede\\src\\EncriptadoClavePublicaPrivada\\GeneradorDeClavesRSA\\Claves\\";


    public static String generarRuta(String nomFichClave, int accion) {
        StringBuilder stb = new StringBuilder();
        stb.append(nomFichClave);
        if (accion == 1) {
            stb.append(NOM_FICH_CLAVE_PUBLICA);
        } else {
            stb.append(NOM_FICH_CLAVE_PRIVADA);
        }
        nomFichClave = stb.toString();
        return nomFichClave;
    }

    public static String leerFichero(String fichero) {
        String contenidoFichero="";
        try (	Reader reader = new FileReader(String.valueOf(Paths.get(fichero)));
                 BufferedReader bufferedReader=new BufferedReader(reader)){
            contenidoFichero=bufferedReader.readLine();
        }catch(FileNotFoundException e){
            System.out.println("No se encrontró el archivo");
        }catch(IOException e){
            System.out.println("Error de Entrada/Salida");
        }
        return contenidoFichero;
    }


    public static byte[] bytesDeFichero(String rutaFichero) {
        byte valorClave[] = null;
        try (FileInputStream writer = new FileInputStream(rutaFichero)) {
            valorClave = writer.readAllBytes();
        }catch(IOException e){
            System.out.println("Error de Entrada/Salida");
        }
        return valorClave;
    }

    public static void escribirBytesEnFichero(String nomFich, String modo, byte[] mensaje) {
        try(FileOutputStream writer = new FileOutputStream(String.valueOf(Paths.get(nomFich+modo)))){
            writer.write(mensaje);
        }catch(FileNotFoundException t){
            System.out.println("Fichero no encontrado");
        }catch(IOException e){
            System.out.println("Error de Entrada/Salida");
        }
    }

    public static String escribirCadenaEnFichero(String nomFich, String modo, String mensaje) {
        try(Writer writer = new FileWriter(String.valueOf(Paths.get(nomFich+modo)));
            BufferedWriter bufferedWriter=new BufferedWriter(writer))
        {
            bufferedWriter.write(mensaje);
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Problemas escribiendo en el archivo");
        }
        return nomFich;
    }


    /**
     * @param clavePublica
     * @return
     */
    public static boolean escribirClavePublicaRSA(PublicKey clavePublica, String nomFichClavePublica) {
        X509EncodedKeySpec x509EncodedKeySpec = null;
        nomFichClavePublica = generarRuta(RUTA_FICH_CLAVE + nomFichClavePublica, 1);
        boolean salir = false;
        try (FileOutputStream fosClavePublica = new FileOutputStream(nomFichClavePublica)) {
            x509EncodedKeySpec = new X509EncodedKeySpec(
                    clavePublica.getEncoded(), nomFichClavePublica);
            fosClavePublica.write(x509EncodedKeySpec.getEncoded());
            System.out.printf("Clave pública guardada en formato %s en fichero %s:\n%s\n",
                    x509EncodedKeySpec.getFormat(), nomFichClavePublica,
                    Base64.getEncoder().encodeToString(x509EncodedKeySpec.getEncoded()).replaceAll("(.{76})", "$1\n"));  // clavePublica.getEncoded() tiene lo mismo);
            salir = true;
        } catch (IOException e) {
            System.out.println("Error de E/S escribiendo clave pública en fichero");
        }
        return salir;
    }


    /**
     * @param nomFichClave
     * @return
     */
    public static byte[] leerClavePublicaRSA(String nomFichClave) {
        byte[] clavePubCodif = null;

        try (FileInputStream fisClavePub = new FileInputStream(nomFichClave)) {
            clavePubCodif = fisClavePub.readAllBytes();
        } catch (FileNotFoundException e) {
            System.out.printf("ERROR: no existe fichero de clave pública %s\n.", nomFichClave);
        } catch (IOException e) {
            System.out.printf("ERROR: de E/S leyendo clave de fichero %s\n.", nomFichClave);
        }
        return clavePubCodif;
    }


    /**
     * @param clavePrivada
     * @return
     */
    public static boolean escribirClavePrivadaRSA(PrivateKey clavePrivada, String nomFichClave) {
        boolean salir = false;
        nomFichClave = generarRuta(RUTA_FICH_CLAVE + nomFichClave, 2);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = null;
        try (FileOutputStream fosClavePrivada = new FileOutputStream(new File(nomFichClave))) {
            pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                    clavePrivada.getEncoded(), ALGORITMO_CLAVE_PUBLICA);
            fosClavePrivada.write(pkcs8EncodedKeySpec.getEncoded());
            System.out.printf("Clave privada guardada en formato %s en fichero %s:\n%s\n",
                    // clavePrivada.getEncoded() tiene lo mismo
                    pkcs8EncodedKeySpec.getFormat(), nomFichClave,
                    Base64.getEncoder().encodeToString(pkcs8EncodedKeySpec.getEncoded()).replaceAll("(.{76})", "$1\n"));
            salir = true;
        } catch (IOException e) {
            System.out.println("Error de E/S escribiendo clave privada en fichero");

        }
        return salir;
    }


    public static byte[] leerClavePrivadaRSA(String nomFichClave) {
        nomFichClave = GestionFicheros.generarRuta(nomFichClave, 2);
        byte clavePrivCodif[] = null;
        try (FileInputStream fisClavePriv = new FileInputStream(nomFichClave)) {
            clavePrivCodif = fisClavePriv.readAllBytes();
        } catch (FileNotFoundException e) {
            System.out.printf("ERROR: no existe fichero de clave pública %s\n.", nomFichClave);
        } catch (IOException e) {
            System.out.printf("ERROR: de E/S leyendo clave de fichero %s\n.", nomFichClave);
        }
        return clavePrivCodif;
    }



    public static void firmarFichero(PrivateKey clavePrivada, byte[] datos, String fichero) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, FileNotFoundException, IOException {
        Signature signature = Signature.getInstance(ALGORITMO_DE_FIRMA);
        signature.initSign(clavePrivada);
        signature.update(datos);
        byte[] firma = signature.sign();
        GestionFicheros.escribirBytesEnFichero(fichero,".txt", firma);
    }

    public static boolean verificarFirmaFichero(PublicKey publicKey, byte[] datos, byte[] firma) throws Exception {
        Signature sign = Signature.getInstance(ALGORITMO_DE_FIRMA);
        sign.initVerify(publicKey);
        sign.update(datos);
        return sign.verify(firma);
    }
    public static byte[] decodificarEnBase64(String mensaje) throws FileNotFoundException, IOException {
        return Base64.getDecoder().decode(GestionFicheros.leerFichero(String.valueOf(Paths.get(mensaje))));
    }

    public static String codificarEnBase64(byte[] mensaje) {
        return Base64.getEncoder().encodeToString(mensaje);
    }


    public static void claveAFichero(String nomFich, Key clave, boolean privada){
        try(FileWriter fw = new FileWriter(String.valueOf(Paths.get((nomFich))));
            BufferedWriter bw = new BufferedWriter(fw)){
            EncodedKeySpec keySpec = privada?
                    new PKCS8EncodedKeySpec(clave.getEncoded(), ALGORITMO_CLAVE_PUBLICA)
                    :
                    new X509EncodedKeySpec(clave.getEncoded(), ALGORITMO_CLAVE_PUBLICA);
            String claveBase64 = GestionFicheros.codificarEnBase64(keySpec.getEncoded());
            bw.write(claveBase64);
            bw.close();
        } catch (IOException e) {
            System.out.println("Error en la búsqueda del fichero");
        }
    }


}

