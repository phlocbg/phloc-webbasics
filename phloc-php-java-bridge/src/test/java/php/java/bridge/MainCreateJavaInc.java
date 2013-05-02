package php.java.bridge;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainCreateJavaInc
{
  public static void main (final String [] args) throws IOException
  {
    final File f = new File ("src/main/resources/META-INF/java/Java.inc");
    final OutputStream aFOS = new FileOutputStream (f);
    aFOS.write (JavaInc.bytes);
    aFOS.close ();
  }
}
