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
import java.io.PrintStream;

import org.jboss.wfk.repotree.filter.MetaInfMavenFilter;
import org.jboss.wfk.repotree.traversal.DirectoryTraversal;
import org.jboss.wfk.repotree.traversal.JarVisitor;
import org.jboss.wfk.repotree.traversal.Visitor;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class RepoTree
{
   @SuppressWarnings("unchecked")
   public static void main(String[] args) throws Exception
   {
      PrintStream mavenOutput = new PrintStream(new File("maven.output"));
      MavenInstaller installer = new MavenInstaller(new File(args[0]), mavenOutput, mavenOutput);

      DirectoryTraversal traversal = new DirectoryTraversal(new File(args[1]));
      Visitor visitor = new JarVisitor(new MetaInfMavenFilter(installer));
      traversal.traverse(visitor);
   }
}
