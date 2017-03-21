package com.company;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by klego on 20.03.2017.
 */
class Server {
    Logger logger = Logger.getLogger(Server.class.getCanonicalName());
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void listenOn(int port) throws IOException {

        try (ServerSocket serverSocker = new ServerSocket(port)) {
            logger.info(String.format("Listening on port %d", port));

            while (true) {
                Socket socket = serverSocker.accept();
                executorService.submit(() -> checkItOut(socket));
            }
        }

    }

    public void checkItOut(Socket socket) {
        logger.info("Handling socket: " + socket.getInetAddress().getHostAddress());

        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            String fileName = in.readUTF();
            logger.info("FileName: " + fileName);
            Path filePath = Paths.get(System.getProperty("user.home"), "outputLab2", fileName);
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);
            logger.info("Plik " + fileName + " jest przetwarzany !");
            try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(filePath))) {
                int bufferSize = 4096;
                byte[] buffer = new byte[bufferSize];
                int readSize;

                while ((readSize = in.read(buffer)) != -1) { //Read from input stream (network socket)
                    out.write(buffer, 0, readSize); //Write to output stream (to file)
                }
                logger.info("Plik " + fileName + " przesłany!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
