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
package org.jboss.wfk.repotree;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.cli.MavenCli;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class MavenInstaller
{
   private MavenCli maven;
   private String currentDir;
   private String repoDir;

   private List<String> args;

   private PrintStream out;
   private PrintStream err;

   public MavenInstaller(File localRepositoryPath, String... args)
   {
      this(localRepositoryPath, System.out, System.err, args);
   }

   public MavenInstaller(File localRepositoryPath, PrintStream out, PrintStream err, String... args)
   {
      this.args = Arrays.asList(args);
      this.maven = new MavenCli();
      this.out = out;
      this.err = err;
      try
      {
         this.repoDir = localRepositoryPath.getCanonicalPath();
      }
      catch (IOException e)
      {
         throw new RuntimeException("Unable to open local repository path at: " + localRepositoryPath.getAbsolutePath());
      }

      try
      {
         this.currentDir = new File(".").getCanonicalPath();
      }
      catch (IOException e)
      {
         throw new RuntimeException("Unable to open working directory", e);
      }
   }

   public boolean install(File artifact, File pom) throws IOException
   {
      List<String> args = new ArrayList<String>(this.args);

      args.add("install:install-file");
      args.add("--batch-mode");
      args.add("-DinteractiveMode=false");
      args.add("-DlocalRepositoryPath=" + repoDir);
      args.add("-DupdateReleaseInfo=true");
      args.add("-Dfile=" + artifact.getCanonicalPath());
      args.add("-DpomFile=" + pom.getCanonicalPath());

      int result = maven.doMain(args.toArray(new String[0]), currentDir, out, err);

      return result == 0;
   }
}
