/**
 * 
 */
package org.jboss.wfk.repotree.filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
public class FileUtils
{
   public static File wrap(InputStream input) throws IOException
   {
      int len;
      byte[] buf = new byte[4096];

      File file = File.createTempFile("file-io", "tmp");
      OutputStream out = new FileOutputStream(file);

      while ((len = input.read(buf)) > 0)
      {
         out.write(buf, 0, len);
      }

      out.close();
      input.close();

      return file;

   }
}
