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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jboss.wfk.repotree.artifact.Artifact;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
public class Gavs
{
   private Map<String, Artifact> db;

   public Gavs()
   {
      this.db = new HashMap<String, Artifact>();
   }

   public Artifact lookup(String id)
   {
      if (id == null)
      {
         return null;
      }

      return db.get(id);
   }

   public Gavs load(File file) throws IOException
   {
      GavsLoader loader = new GavsLoader(file);
      for (Gav gav : loader.load())
      {
         this.db.put(gav.getName(), gav.getArtifact());
      }

      return this;
   }
}
