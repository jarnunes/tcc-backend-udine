package com.pucminas.commons.resource;


import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Resource {

    default String userDir() {
        return System.getProperty("user.dir");
    }

    default String createResourcePathName(String... pathNames) {
        return createPath(Paths.get(userDir(), "generated-sources"), pathNames);
    }

    default String createResourceImagePathName(String fileName){
        return createResourcePathName("images", fileName);
    }

    default void createApplicationResourcePath() {
        createPath();
    }

    default void createImagePath() {
        createPath("images");
    }

    default void createPath(String... paths) {
        File directory = new File(createResourcePathName(paths));

        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    default String createPath(Path path, String... pathNames) {
        for (String pathName : pathNames) {
            path = path.resolve(pathName);
        }
        return path.toString();
    }

}
