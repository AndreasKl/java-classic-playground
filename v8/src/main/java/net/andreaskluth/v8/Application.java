package net.andreaskluth.v8;

import com.eclipsesource.v8.V8;

public class Application {

  private static String script = "var hello = 'hello, ';\n" + "var world = 'world!';\n"
      + "hello.concat(world).length;\n";

  public static void main(String[] args) {
    V8 runtime = V8.createV8Runtime();
    try {

      System.out.println(runtime.executeIntegerScript(script));

    } finally {
      runtime.release();
    }
  }

}