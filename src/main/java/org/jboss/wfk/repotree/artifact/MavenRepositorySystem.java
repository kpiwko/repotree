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
package org.jboss.wfk.repotree.artifact;

import java.io.File;

import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.codehaus.plexus.DefaultPlexusContainer;
import org.codehaus.plexus.PlexusContainerException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.installation.InstallRequest;
import org.sonatype.aether.installation.InstallationException;
import org.sonatype.aether.metadata.Metadata;
import org.sonatype.aether.repository.LocalRepository;

/**
 * Abstraction of the repository system for purposes of dependency
 * resolution used by Maven
 * 
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class MavenRepositorySystem
{
   private LocalRepository localRepository;

   private final RepositorySystem system;

   public MavenRepositorySystem(File localRepositoryDir)
   {
      this.localRepository = new LocalRepository(localRepositoryDir);
      this.system = getRepositorySystem();
   }

   /**
    * Spawns a working session from the repository system. Working session is
    * shared between all Maven based commands
    * @param system A repository system
    * @param settings A configuration of current session, such as local or remote repositories and listeners
    * @return The working session for dependency resolution
    */
   public RepositorySystemSession getSession()
   {
      MavenRepositorySystemSession session = new MavenRepositorySystemSession();
      session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepository));
      session.setTransferListener(new LogTransferListerer());
      session.setRepositoryListener(new LogRepositoryListener());

      return session;
   }

   public boolean installArtifact(RepositorySystemSession session, org.sonatype.aether.artifact.Artifact artifact, Metadata metadata)
   {
      try
      {
         InstallRequest request = new InstallRequest();
         request.addArtifact(artifact);
         if (metadata != null)
         {
            request.addMetadata(metadata);
         }
         system.install(session, request);
         return true;
      }
      catch (InstallationException e)
      {
         e.printStackTrace();
      }

      return false;
   }

   /**
    * Finds a current implementation of repository system.
    * A {@link RepositorySystem} is an entry point to dependency resolution
    * @return A repository system
    */
   private RepositorySystem getRepositorySystem()
   {
      try
      {
         return new DefaultPlexusContainer().lookup(RepositorySystem.class);
      }
      catch (ComponentLookupException e)
      {
         throw new RuntimeException("Unable to lookup component RepositorySystem, cannot establish Aether dependency resolver.", e);
      }
      catch (PlexusContainerException e)
      {
         throw new RuntimeException("Unable to load RepositorySystem component by Plexus, cannot establish Aether dependency resolver.", e);
      }
   }

}
