/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.wfk.repotree.filter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jboss.wfk.repotree.IOUtil;
import org.jboss.wfk.repotree.MavenInstaller;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class MetaInfMavenFilter implements Filter<JarFile>
{

   private MavenInstaller installer;

   public MetaInfMavenFilter(MavenInstaller installer)
   {
      this.installer = installer;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.filter.Filter#accept(java.lang.Object)
    */
   public boolean accept(JarFile element)
   {
      Enumeration<JarEntry> elements = element.entries();
      List<JarEntry> candidates = new ArrayList<JarEntry>();

      while (elements.hasMoreElements())
      {
         JarEntry entry = elements.nextElement();
         if (!entry.isDirectory() && isPomFile(entry.getName()))
         {
            candidates.add(entry);
         }
      }

      if (candidates.isEmpty())
      {
         return false;
      }

      boolean installed = false;
      for (JarEntry entry : candidates)
      {
         try
         {
            File pom = File.createTempFile("pom", "xml");

            IOUtil.copy(element.getInputStream(entry), new FileOutputStream(pom));
            installed = installer.install(new File(element.getName()), pom);
            if (installed == true)
            {
               break;
            }
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }

      return installed;
   }

   private boolean isPomFile(String name)
   {
      return name.startsWith("META-INF/maven") && name.endsWith("pom.xml");
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.filter.Filter#name()
    */
   public String name()
   {
      return "META-INF/maven";
   }

}
