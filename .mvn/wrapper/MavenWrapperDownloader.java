package org.apache.maven.wrapper;

import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Properties;

public class MavenWrapperDownloader {

    private static final String WRAPPER_VERSION = "3.3.2";
    private static final String DEFAULT_DOWNLOAD_URL = "https://repo1.maven.org/maven2/org/apache/maven/wrapper/maven-wrapper/"
            + WRAPPER_VERSION + "/maven-wrapper-" + WRAPPER_VERSION + ".jar";

    public static void main(String[] args) {
        System.out.println("- Downloading maven-wrapper.jar");
        try {
            downloadFileFromURL(DEFAULT_DOWNLOAD_URL, "maven-wrapper.jar");
        } catch (IOException e) {
            System.out.println("- Error downloading: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void downloadFileFromURL(String urlString, String fileName) throws IOException {
        URL url = new URL(urlString);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(fileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
}
