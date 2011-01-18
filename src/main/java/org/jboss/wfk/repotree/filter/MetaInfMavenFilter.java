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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.ModelReader;
import org.jboss.wfk.repotree.api.Configuration;
import org.jboss.wfk.repotree.api.Filter;
import org.jboss.wfk.repotree.artifact.Artifact;
import org.jboss.wfk.repotree.artifact.MavenRepositorySystem;
import org.sonatype.aether.RepositorySystemSession;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class MetaInfMavenFilter implements Filter
{
   private static final Logger log = Logger.getLogger(MetaInfMavenFilter.class.getName());

   private MavenRepositorySystem system;
   private RepositorySystemSession session;

   private boolean installPoms;

   public MetaInfMavenFilter()
   {
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.filter.Filter#accept(java.io.File)
    */
   public boolean accept(File file) throws Exception
   {
      JarFile jar = new JarFile(file);

      Enumeration<JarEntry> elements = jar.entries();
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

      for (JarEntry entry : guess(file, candidates))
      {
         try
         {
            ModelReader reader = new DefaultModelReader();
            Model model = reader.read(jar.getInputStream(entry), null);

            Artifact artifact = new Artifact(model);

            // install jar
            if (system.installArtifact(session, artifact.attachFile(file), null))
            {
               if (installPoms)
               {
                  // install pom file for jar
                  try
                  {
                     Artifact pom = new Artifact(artifact.getGroupId(), artifact.getArtifactId(), "pom", artifact.getClassifier(), artifact.getVersion());
                     File pomFile = FileUtils.wrap(jar.getInputStream(entry));
                     system.installArtifact(session, pom.attachFile(pomFile), null);
                     pomFile.delete();
                  }
                  catch (IOException e)
                  {
                     log.warning("Unable to install a pom file for the correctly installed artifact: " + artifact.toString());
                  }
               }

               return true;
            }
         }
         catch (IOException e)
         {
            e.printStackTrace();
         }
      }

      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jboss.wfk.repotree.filter.Filter#configure(org.jboss.wfk.repotree.
    * Configuration)
    */
   public void configure(Configuration configuration)
   {
      this.system = configuration.getRepositorySystem();
      this.session = system.getSession();
      this.installPoms = configuration.isInstallingPoms();
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

   private boolean isPomFile(String name)
   {
      return name.startsWith("META-INF/maven") && name.endsWith("/pom.xml");
   }

   private List<JarEntry> guess(File file, List<JarEntry> candidates)
   {
      log.fine("Filtering " + candidates.size() + " candidates for Maven pom.xml file");

      if (candidates == null || candidates.size() <= 1)
      {
         return candidates;
      }

      List<JarEntry> filtered = new ArrayList<JarEntry>();

      // remove jar suffix from the artifact file name
      String jarName = file.getName();
      if (jarName.endsWith(".jar"))
      {
         jarName = jarName.substring(0, jarName.lastIndexOf(".jar"));
      }

      for (JarEntry candidate : candidates)
      {
         String name = candidate.getName();
         name = name.substring(0, name.lastIndexOf("/pom.xml"));
         name = name.substring(name.lastIndexOf("/") + 1);

         if (name.startsWith(jarName) || jarName.startsWith(name))
         {
            filtered.add(candidate);
         }
      }

      // omit filter if no matches found
      if (filtered.isEmpty())
      {
         log.fine("Filtering removed all poms, returning original candidates collection");
         return candidates;
      }

      log.fine("After filtering, there are " + filtered.size() + " candidates available");
      return filtered;

   }

}
