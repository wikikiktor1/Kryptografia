package org.example.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;

public class FileDao implements Dao<MyFile> {

    private String path;

    public FileDao(String path) {
        this.path = path;
    }

    private boolean isTextFile(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        return fileName.endsWith(".txt") || fileName.endsWith(".sig") || fileName.endsWith(".key");
    }

    @Override
    public MyFile read(String name) throws WrongInputException {
        Path fullPath = Path.of(name);
        try {
            if (isTextFile(fullPath)) {
                String content = Files.readString(fullPath);
                return new MyFile(fullPath.getFileName().toString(), content);
            } else {
                byte[] help = Files.readAllBytes(fullPath);
                String content = Base64.getEncoder().encodeToString(help);
                return new MyFile(fullPath.getFileName().toString(), content);
            }
        } catch (IOException e) {
            throw new WrongInputException("input", e);
        }
    }

    @Override
    public void write(String name, MyFile obj) throws WrongInputException {
        Path fullPath = Path.of(name);
        try {
            if (isTextFile(fullPath)) {
                Files.writeString(fullPath, obj.getContent());
            } else {
                byte[] help = Base64.getDecoder().decode(obj.getContent());
                Files.write(fullPath, help);
            }
        } catch (IOException e) {
            throw new WrongInputException("input", e);
        }
    }

    @Override
    public List<String> names() throws WrongInputException {
        return List.of();
    }

    @Override
    public void close() throws Exception {
        // Nothing to close
    }
}
