package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * Esta clase principal lo que hace es generar archivos HTML a partir de un archivo CSV con listas de películas
 * y una plantilla HTML. Se lee los datos del archivo CSV, se aplica la plantilla HTML y genera un archivo
 * HTML por cada película del archivo CSV.
 */

public class Main {
    public static void main(String[] args) {


        String csvFilePath = "C:\\Users\\vanes\\IdeaProjects\\AD1\\peliculas.csv";
        String templateFilePath = "C:\\Users\\vanes\\IdeaProjects\\AD1\\template.html";

        generarHTML(csvFilePath, templateFilePath);

    }

    /**
     * Genera archivos HTML a partir de datos leídos del archivo CSV y de una plantilla HTML.
     * @param csvFilePath Ruta del archivo CSV que contiene los datos de las películas.
     * @param templateFilePath Ruta del archivo de plantilla HTML.
     */

    public static void generarHTML(String csvFilePath, String templateFilePath) {

        List<Pelicula> peliculas = leerCSV(csvFilePath);

        String template = leerPlantilla(templateFilePath);

        limpiarCarpetaSalida();


        for (Pelicula pelicula : peliculas) {
            String html = generarArchivoHTML(pelicula, template);
            guardarArchivoHTML(pelicula, html);
        }
    }

    /**
     * Lee los datos de las películas desde el archivo CSV y crea una lista de objetos Pelicula.
     * @param csvFilePath Ruta del archivo CSV que contiene los datos.
     * @return Lista de objetos Pelicula creados a partir de los datos del CSV.
     */

    private static List<Pelicula> leerCSV(String csvFilePath) {
        List<Pelicula> peliculas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    Pelicula pelicula = new Pelicula(
                            Integer.parseInt(datos[0].trim()), // ID
                            datos[1].trim(),                   // Título
                            Integer.parseInt(datos[2].trim()), // Año
                            datos[3].trim(),                   // Director
                            datos[4].trim()                    // Género
                    );
                    peliculas.add(pelicula);
                    System.out.println("Película añadida: " + pelicula.getTitulo());
                } else {
                    System.out.println("Línea con formato inválido: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo CSV: " + e.getMessage());
        }
        return peliculas;
    }

    /**
     * Lee el contenido de un archivo de plantilla HTML.
     * @param templateFilePath Ruta del archivo de plantilla HTML.
     * @return Contenido de la plantilla como un String.
     */

    private static String leerPlantilla(String templateFilePath) {
        StringBuilder contenido = new StringBuilder();
        try {
            contenido.append(Files.readString(Paths.get(templateFilePath)));
        } catch (IOException e) {
            System.out.println("La plantilla HTML no se ha podido leer.");
        }
        return contenido.toString();
    }

    /**
     * Limpia la carpeta de salida eliminando todos los archivos existentes.
     */

    private static void limpiarCarpetaSalida() {
        File folder = new File("salida");
        if (!folder.exists()) {
            folder.mkdir();
        } else {
            for (File file : folder.listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * Genera un archivo HTML para una película específica reemplazando los marcadores de la plantilla con los datos de la película.
     * Los marcadores a reemplazar en la plantilla son:
     * - %%1%% para el ID
     * - %%2%% para el título
     * - %%3%% para el año
     * - %%4%% para el director
     * - %%5%% para el género
     *
     * @param pelicula El objeto Pelicula que contiene los datos a insertar en la plantilla.
     * @param template La plantilla HTML que contiene los marcadores que serán reemplazados.
     * @return Una cadena que contiene el HTML generado para la película, con los datos reemplazados.
     */

    private static String generarArchivoHTML(Pelicula pelicula, String template) {
        return template
                .replace("%%1%%", String.valueOf(pelicula.getId()))
                .replace("%%2%%", pelicula.getTitulo())
                .replace("%%3%%", String.valueOf(pelicula.getAnho()))
                .replace("%%4%%", pelicula.getDirector())
                .replace("%%5%%", pelicula.getGenero());
    }

    /**
     * Guarda el contenido HTML generado en un archivo.
     * @param pelicula Objeto Pelicula utilizado para el nombre del archivo.
     * @param html Contenido HTML que se desea guardar.
     */

    private static void guardarArchivoHTML(Pelicula pelicula, String html) {
        String nombreArchivo = "salida/" + pelicula.getTitulo().replace(":", "_") + " - " + pelicula.getId() + ".html";
        System.out.println("Guardando archivo: " + nombreArchivo);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
            writer.write(html);
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo HTML para la película: " + pelicula.getTitulo());
        }
    }
}
















