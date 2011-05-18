/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.wfk.repotree.gav;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.wfk.repotree.artifact.Artifact;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
public class GavsLoader
{
   private static final Logger log = Logger.getLogger(GavsLoader.class.getName());

   private File file;

   public GavsLoader(File file)
   {
      this.file = file;
   }

   public List<Gav> load()
   {
      try
      {
         BufferedReader reader = new BufferedReader(new FileReader(file));
         List<Gav> list = new ArrayList<Gav>();
         String line;
         while ((line = reader.readLine()) != null)
         {
            list.add(constructGav(line));
         }
         return list;
      }
      catch (FileNotFoundException e)
      {
         throw new RuntimeException(e);

      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }
   }

   private Gav constructGav(String line)
   {
      StringTokenizer tokenizer = new StringTokenizer(line, "|");
      String id = tokenizer.nextToken().trim();
      String artifact = tokenizer.nextToken().trim();

      if (log.isLoggable(Level.FINE))
      {
         log.fine("Constructed gav: id=" + id + ", gav=" + artifact);
      }

      return new Gav(id, new Artifact(artifact));

   }
}
