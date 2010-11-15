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
package org.jboss.wfk.repotree.api;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.jboss.wfk.repotree.artifact.MavenRepositorySystem;
import org.jboss.wfk.repotree.signature.Signatures;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class Configuration
{
   private Signatures signatures;

   private MavenRepositorySystem repositorySystem;

   private Collection<Filter> filterPlugins;

   private List<File> directories;

   /**
    * @return the repositorySystem
    */
   public MavenRepositorySystem getRepositorySystem()
   {
      return repositorySystem;
   }

   /**
    * @param repositorySystem the repositorySystem to set
    */
   public Configuration setRepositorySystem(MavenRepositorySystem repositorySystem)
   {
      this.repositorySystem = repositorySystem;
      return this;
   }

   /**
    * @return the signatures
    */
   public Signatures getSignatures()
   {
      return signatures;
   }

   /**
    * @param signatures the signatures to set
    */
   public Configuration setSignatures(Signatures signatures)
   {
      this.signatures = signatures;
      return this;
   }

   /**
    * @return the filterPlugins
    */
   public Collection<Filter> getFilterPlugins()
   {
      return filterPlugins;
   }

   /**
    * @param filterPlugins the filterPlugins to set
    */
   public Configuration setFilterPlugins(Collection<Filter> filterPlugins)
   {
      this.filterPlugins = filterPlugins;
      return this;
   }

   /**
    * @return the directories
    */
   public List<File> getDirectories()
   {
      return directories;
   }

   /**
    * @param directories the directories to set
    */
   public void setDirectories(List<File> directories)
   {
      this.directories = directories;
   }

}
