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
package org.jboss.wfk.repotree.filter;

import java.io.File;

import org.jboss.wfk.repotree.api.Configuration;
import org.jboss.wfk.repotree.api.Filter;
import org.jboss.wfk.repotree.artifact.Artifact;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
public class GavFilter implements Filter
{
   private Configuration configuration;

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.api.Filter#name()
    */
   public String name()
   {
      return "GavFilter";
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.api.Filter#accept(java.io.File)
    */
   public boolean accept(File file) throws Exception
   {
      String id = file.getName();
      Artifact artifact = configuration.getGavs().lookup(id);

      if (artifact != null)
      {
         configuration.getRepositorySystem().installArtifact(artifact.attachFile(file), null);
         return true;
      }
      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.api.Filter#configure(org.jboss.wfk.repotree.api.Configuration)
    */
   public void configure(Configuration configuration)
   {
      this.configuration = configuration;
   }

}
