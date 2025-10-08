package ch.usi.inf.bsc.sa4.lab02spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Class of the method to execute to run the backend */
@SpringBootApplication
public class SimUrbs {

  /**
   * main method of the project, if executed, it runs the backend
   *
   * @param args unused
   */
  public static void main(String[] args) {
    SpringApplication.run(SimUrbs.class, args);
  }
}
