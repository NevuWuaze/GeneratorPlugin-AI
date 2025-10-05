package com.example.generatorai;

import org.bukkit.plugin.Plugin;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class PluginCompiler {

    private Plugin plugin;

    public PluginCompiler(Plugin plugin) {
        this.plugin = plugin;
    }

    public Class<?> compileAndLoad(String className, String code) throws Exception {
        // Create temp directory
        Path tempDir = Files.createTempDirectory("generated");

        // Write code to file
        Path sourceFile = tempDir.resolve(className + ".java");
        Files.write(sourceFile, code.getBytes());

        // Compile
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile.toFile()));
        List<String> options = Arrays.asList("-d", tempDir.toString(), "-cp", getClassPath());

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, options, null, compilationUnits);
        boolean success = task.call();

        fileManager.close();

        if (!success) {
            throw new RuntimeException("Compilation failed");
        }

        // Load class
        URLClassLoader classLoader = new URLClassLoader(new URL[]{tempDir.toUri().toURL()}, plugin.getClass().getClassLoader());
        Class<?> clazz = classLoader.loadClass("generated." + className);

        // Clean up temp files later if needed

        return clazz;
    }

    private String getClassPath() {
        // Get classpath including Spigot
        StringBuilder cp = new StringBuilder();
        cp.append(System.getProperty("java.class.path"));
        // Add more if needed
        return cp.toString();
    }
}
