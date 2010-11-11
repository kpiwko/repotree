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
package org.jboss.wfk.repotree.traversal;

import java.io.File;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class DirectoryTraversal
{
   private File entry;

   public DirectoryTraversal(File entry)
   {
      if (!entry.isDirectory())
      {
         throw new IllegalArgumentException("A directory must be specified for the traversal, but " + entry.getAbsolutePath() + " is not a directory.");
      }

      this.entry = entry;
   }

   public void traverse(Visitor visitor) throws Exception
   {
      for (File child : entry.listFiles())
      {
         if (child.isDirectory())
         {
            new DirectoryTraversal(child).traverse(visitor);
         }
         else
         {
            visitor.entry(child);
         }
      }
   }
}
