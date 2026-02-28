package mx.com.qtx.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestHttp {
	public static String verbosHttp[] = {"GET","POST","PUT","DELETE", "GET","GET","GET"};
	public static String mediaTypes[] = {"application/json","text/plain","text/html"};
	public static String recursos[] = {"/img/gato.gif","/img/osoPolar.jpg",
			"/img/desierto.jpg","/img/snoopy.JPG","/Test","/Test","/Test"};
	
	public static String URI = "/330_TestDenegServicio";
	public static boolean MOSTRAR_RESPUESTA  = false;
	
	public static void main(String[] args) throws IOException {
		for(int i=0 ; i<10; i++) {
			URL url = new URL("http", "localhost", 8080, URI + getRecursoRandom() );
			generarPeticionHttp(url, getVerboRandom());
		}
		
	}
	
	private static String getVerboRandom() {
		int n = (int)(Math.random() * 10033);
		return verbosHttp[n%verbosHttp.length];
	}
	
	private static String getMediaTypeRandom() {
		int n = (int)(Math.random() * 10000);
		return mediaTypes[n%mediaTypes.length];
	}
	
	private static String getRecursoRandom() {
		int n = (int)(Math.random() * 77773);
		return recursos[n%recursos.length];
	}
	
	private static void generarPeticionHttp(URL url, String verboHttp) throws ProtocolException, IOException {
		HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
		conexion.setRequestMethod(verboHttp);
		conexion.setRequestProperty("User-Agent", "Programa de Alex");
		conexion.setRequestProperty("Accept", getMediaTypeRandom());
		conexion.setInstanceFollowRedirects(false);
		conexion.connect();
		/*
		conexion.setRequestProperty("Accept-Charset", "ISO-8959-1");
		conexion.setRequestProperty("Accept-Language", "es-MX");
		 */
		explorarRespuesta(conexion);
		if(MOSTRAR_RESPUESTA) {
			mostrarRespuesta(conexion);
		}
		conexion.disconnect();
	}
	
	private static void mostrarRespuesta(HttpURLConnection conexion) {
		System.out.println("Respuesta - - - - - - - - - - - - - -");
		if(conexion.getContentType() == null) {
			System.out.println("** No hay contenido **");
			return;			
		}
		if(conexion.getContentType().contains("image")) {
			System.out.println("Contenido binario:Imagen");
			return;
		}
		try( BufferedReader brUrlStream = 
				 new BufferedReader(
				     new InputStreamReader(conexion.getInputStream())) ){		 
			 while(true){
				 String linea = brUrlStream.readLine();
				 if(linea==null){
					 System.out.println("----------- Fin del flujo -----------");
					 break;
				 }
				 System.out.println(linea);		
			}
		}
		catch(Exception ex){
			System.out.println("Exception:"+ex.getClass().getSimpleName()+": " + ex.getMessage());
		}
	}

	private static void explorarRespuesta(HttpURLConnection conexion) throws IOException {
		System.out.println("\n=====================================================");
		
		System.out.println("RequestMethod: " + conexion.getRequestMethod());
		System.out.println("URL.Path     : " + conexion.getURL().getPath());
		

		System.out.println("\n\tConnectTimeout:" + conexion.getConnectTimeout());
		System.out.println("\tContentEncoding:" + conexion.getContentEncoding());
		System.out.println("\tContentLength:" + conexion.getContentLength());
		System.out.println("\tContentType:" + conexion.getContentType());
		Map<String, List<String>> mapHeaderFields = conexion.getHeaderFields();
		System.out.println("\tHeaderFields:");
		for(Entry<String, List<String>> fieldI : mapHeaderFields.entrySet()) {
			System.out.println("\t\t"+fieldI.getKey() + ": " + fieldI.getValue());
		}
		System.out.println("\tResponseCode:" + conexion.getResponseCode());
		System.out.println("\tResponseMessage:" + conexion.getResponseMessage());
		System.out.println("=====================================================");
	}

}
