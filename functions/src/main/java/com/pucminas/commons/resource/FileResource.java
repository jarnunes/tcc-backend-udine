package com.pucminas.commons.resource;


import com.pucminas.commons.functions.ResourceConsumerThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileResource implements Resource {

    public static FileResource instance() {
        return new FileResource();
    }

    public void loadFile(final String fileName,
        final ResourceConsumerThrows<ObjectInputStream> consumerExistentFile,
        final ResourceConsumerThrows<File> consumerNonExistentFile,
        final ResourceConsumerThrows<Throwable> consumerOnException) {
        final File file = new File(createResourcePathName(fileName));

        try {
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    consumerExistentFile.accept(ois);
                } catch (OptionalDataException e) {
                    System.out.println("Dados não lidos: " + e.getMessage());
                }
            } else {
                consumerNonExistentFile.accept(file);
            }
        } catch (Throwable e) {
            try {
                consumerOnException.accept(e);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void writerImage(String fileName, byte[] photo) {
        final String filePathName = createResourceImagePathName( fileName);
        createImagePath();

        try{
            Path outputPath = Paths.get(filePathName);
            Files.write(outputPath, photo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] readImage(String fileName) {
        final String filePathName = createResourceImagePathName(fileName);
        final Path imagePath = Paths.get(filePathName);

        try {
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    public void removeImage(String fileName) {
        final String filePathName = createResourceImagePathName(fileName);
        final Path imagePath = Paths.get(filePathName);

        try {
            Files.delete(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writer(String fileName, Object object) {
        final String filePathName = createResourcePathName(fileName);
        createApplicationResourcePath();
        writeObject(filePathName, object);
    }

    private void writeObject(String filePathName, Object object) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePathName))) {
            out.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
