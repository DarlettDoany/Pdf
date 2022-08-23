package org.saeta.service.file;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface FileService {
    public void init();

     public void save(MultipartFile file);


    public Resource load(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();

    public String deleteFile(String filename) throws IOException;

}
