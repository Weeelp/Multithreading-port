package com.port;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.port.model.Port;
import com.port.model.Ship;
import com.port.parser.ShipParser;
import com.port.parser.impl.ShipParserImpl;
import com.port.reader.TextReader;
import com.port.reader.impl.TextReaderImpl;

public class Application {
  private static final Logger Log = LogManager.getLogger();

  final TextReader reader;
  final ShipParser parser;

  public Application (TextReader reader, ShipParser parser) {
    this.reader = reader;
    this.parser = parser;
  }

  public void run() {
    Port.getInstance(2, 10, 30);
    ExecutorService executor = null;

    try {
      String text = reader.read();
      List<Ship> ships = parser.parse(text);
      executor = Executors.newFixedThreadPool(ships.size());
      List<Future<String>> futures = new ArrayList<>();

      for (Ship ship : ships) {
          futures.add(executor.submit(ship));
      }
        
      for (Future<String> future : futures) {
        Log.info(future.get()); 
      }
    } catch (Exception e) {
      Log.error("File reading exception: " + e);
    } finally {
      if(executor != null) {
        executor.shutdown();
        Log.info("Пул потоков успешно остановлен. Работа порта завершена.");
      }
    }
  }

  public static void main (String[] args) {
    TextReader reader = new TextReaderImpl("text.txt");
    ShipParser parser = new ShipParserImpl();
    Application app = new Application(reader, parser);
    app.run();
  }
}
