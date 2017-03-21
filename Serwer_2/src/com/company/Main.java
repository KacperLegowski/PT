package com.company;
import java.io.IOException;
/**
 * Created by klego on 20.03.2017.
 */
public class Main {

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.listenOn(1234);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
