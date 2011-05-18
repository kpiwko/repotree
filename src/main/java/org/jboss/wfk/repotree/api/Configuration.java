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
import org.jboss.wfk.repotree.bom.BomCreator;
import org.jboss.wfk.repotree.gav.Gavs;
import org.jboss.wfk.repotree.signature.Signatures;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class Configuration
{
   private String versionSuffix = "xx";

   private Signatures signatures;

   private Gavs gavs;

   private Collection<Filter> filterPlugins;

   private List<File> directories;

   private File localRepository;

   private boolean installPoms;

   private MavenRepositorySystem maven;
   
   private BomCreator bomCreator;
   
   /**
    * @return the repositorySystem
    */
   public MavenRepositorySystem getRepositorySystem()
   {
      return maven;
   }
   
   /**
    * @param maven the maven to set
    */
   public Configuration setRepositorySystem(MavenRepositorySystem maven)
   {
      this.maven = maven;
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
   public Configuration setDirectories(List<File> directories)
   {
      this.directories = directories;
      return this;
   }

   /**
    * @return the installPoms
    */
   public boolean isInstallingPoms()
   {
      return installPoms;
   }

   /**
    * @param installPoms the installPoms to set
    */
   public Configuration setInstallingPoms(boolean installPoms)
   {
      this.installPoms = installPoms;
      return this;
   }

   /**
    * @param versionSuffix the versionSuffix to set
    */
   public Configuration setVersionSuffix(String versionSuffix)
   {
      this.versionSuffix = versionSuffix;
      return this;
   }

   /**
    * @return the versionSuffix
    */
   public String getVersionSuffix()
   {
      return versionSuffix;
   }

   /**
    * @param gavs the gavs to set
    */
   public Configuration setGavs(Gavs gavs)
   {
      this.gavs = gavs;
      return this;
   }

   /**
    * @return the gavs
    */
   public Gavs getGavs()
   {
      return gavs;
   }

   /**
    * @return the localRepository
    */
   public File getLocalRepository()
   {
      return localRepository;
   }

   /**
    * @param localRepository the localRepository to set
    */
   public Configuration setLocalRepository(File localRepository)
   {
      this.localRepository = localRepository;
      return this;
   }
   
   /**
    * @return the bomCreator
    */
   public BomCreator getBomCreator()
   {
      return bomCreator;
   }
   
   /**
    * @param bomCreator the bomCreator to set
    */
   public void setBomCreator(BomCreator bomCreator)
   {
      this.bomCreator = bomCreator;
   }

}
