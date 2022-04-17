package net.deechael.library.dcg.dynamic.minecraft;

import net.deechael.library.dcg.dynamic.JClass;
import org.bukkit.Bukkit;
import org.springframework.boot.loader.jar.JarFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarEntry;

/**
 * Run this class if you are using this to develop a bukkit plugin
 */
public class BukkitBootstrap {

    private static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static String getServerVersion() {
        String version = "null";
        try {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (Exception e) {
            try {
                version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[1];
            } catch (Exception ignored) {
            }
        }
        return version;
    }

    private static int getVersion() {
        switch (getServerVersion()) {
            case "v1_8_R1": return 1;
            case "v1_8_R2": return 2;
            case "v1_8_R3": return 3;
            case "v1_9_R1": return 4;
            case "v1_9_R2": return 5;
            case "v1_10_R1": return 6;
            case "v1_11_R1": return 7;
            case "v1_12_R1": return 8;
            case "v1_13_R1": return 9;
            case "v1_13_R2": return 10;
            case "v1_14_R1": return 11;
            case "v1_15_R1": return 12;
            case "v1_16_R1": return 13;
            case "v1_16_R2": return 14;
            case "v1_16_R3": return 15;
            case "v1_17_R1": return 16;
            case "v1_18_R1": return 17;
            case "v1_18_R2": return 18;
            default: return -1;
        }
    }

    public static void run() {
        try {
            if (isPaper()) {
                //Version part
                File paper = null;
                File root = new File(".");
                File[] files = root.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            if (file.getName().endsWith(".jar")) {
                                JarFile jarFile = new JarFile(file);
                                JarEntry manifest = jarFile.getJarEntry("META-INF/MANIFEST.MF");
                                InputStream inputStream = jarFile.getInputStream(manifest);
                                Scanner scanner = new Scanner(inputStream);
                                while (scanner.hasNextLine()) {
                                    if (scanner.nextLine().contains("io.papermc")) {
                                        paper = file;
                                        break;
                                    }
                                    if (!scanner.hasNextLine()) {
                                        break;
                                    }
                                }
                                scanner.close();
                                inputStream.close();
                            }
                        }
                    }
                }
                if (paper != null) {
                    JarFile paperJar = new JarFile(paper);
                    InputStream versionInputStream = paperJar.getInputStream(paperJar.getEntry("META-INF/versions.list"));
                    String version = null;
                    if (versionInputStream != null) {
                        BufferedReader versionBufferedReader = new BufferedReader(new InputStreamReader(versionInputStream));
                        String line;
                        if ((line = versionBufferedReader.readLine()) != null) {
                            version = line.split("\t")[2];
                        }
                    }
                    if (version != null) {
                        JClass.loadLibrary(new File("versions/" + version));
                    }

                    //Libraries part
                    InputStream libraryInputStream = paperJar.getInputStream(paperJar.getJarEntry("META-INF/libraries.list"));
                    List<String> libraries = new ArrayList<>();
                    if (libraryInputStream != null) {
                        BufferedReader libraryBufferedReader = new BufferedReader(new InputStreamReader(libraryInputStream));
                        String line;
                        while ((line = libraryBufferedReader.readLine()) != null) {
                            libraries.add(line.split("\t")[2]);
                        }
                    }
                    for (String fileName : libraries) {
                        File base_directory = new File("libraries");
                        JClass.loadLibrary(new File(base_directory, fileName));
                    }
                }
            } else if (getVersion() >= 17) {
                File bukkit = null;
                File root = new File(".");
                File[] files = root.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            if (file.getName().endsWith(".jar")) {
                                JarFile jarFile = new JarFile(file);
                                JarEntry manifest = jarFile.getJarEntry("META-INF/MANIFEST.MF");
                                InputStream inputStream = jarFile.getInputStream(manifest);
                                Scanner scanner = new Scanner(inputStream);
                                while (scanner.hasNextLine()) {
                                    if (scanner.nextLine().contains("org.bukkit")) {
                                        bukkit = file;
                                        break;
                                    }
                                    if (!scanner.hasNextLine()) {
                                        break;
                                    }
                                }
                                scanner.close();
                                inputStream.close();
                            }
                        }
                    }
                }
                if (bukkit != null) {
                    JarFile bukkitJar = new JarFile(bukkit);
                    //Version part
                    InputStream versionInputStream = bukkitJar.getInputStream(bukkitJar.getEntry("META-INF/versions.list"));
                    String version = null;
                    if (versionInputStream != null) {
                        BufferedReader versionBufferedReader = new BufferedReader(new InputStreamReader(versionInputStream));
                        String line;
                        if ((line = versionBufferedReader.readLine()) != null) {
                            version = line.split(" ")[1].substring(1);
                        }
                    }
                    if (version != null) {
                        JClass.loadLibrary(new File(new File("bundler/versions"), version));
                    }

                    //Libraries part
                    InputStream libraryInputStream = bukkitJar.getInputStream(bukkitJar.getEntry("META-INF/libraries.list"));
                    List<String> libraries = new ArrayList<>();
                    if (libraryInputStream != null) {
                        BufferedReader libraryBufferedReader = new BufferedReader(new InputStreamReader(libraryInputStream));
                        String line;
                        while ((line = libraryBufferedReader.readLine()) != null) {
                            libraries.add(line.split(" ")[1].substring(1));
                        }
                    }
                    for (String fileName : libraries) {
                        File base_directory = new File("bundler/libraries");
                        File library = new File(base_directory, fileName);
                        if (library.exists()) {
                            JClass.loadLibrary(library);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
