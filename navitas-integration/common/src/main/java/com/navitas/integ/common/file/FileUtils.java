package com.navitas.integ.common.file;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Return an entire file's content as a string via a specific class' class-loader
     * @param aClass
     * @param reference
     * @return
     */
    public static String readFile(Class aClass, String reference) {
        try {
            InputStream in = aClass.getClassLoader().getResourceAsStream(reference);
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Can't read file: "+ reference, e);
        }
    }

    /**
     * Return an entire file's content as a string via the current class' loader
     * @param reference
     * @return
     */
    public static String readFile(String reference) {
        try {
            return org.apache.commons.io.FileUtils.readFileToString(ResourceUtils.getFile("classpath:" + reference), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Can't read file: "+ reference, e);
        }
    }

    public static String streamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
        String text = scanner.useDelimiter(System.lineSeparator()).next();
        return text;
    }
}
