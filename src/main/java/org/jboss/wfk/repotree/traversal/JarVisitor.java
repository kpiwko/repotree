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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.wfk.repotree.Configuration;
import org.jboss.wfk.repotree.filter.Filter;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class JarVisitor implements Visitor
{
   private static final Logger log = Logger.getLogger(JarVisitor.class.getName());

   private List<Filter> filters;

   public JarVisitor(Configuration configuration, Filter... filters)
   {
      this.filters = new ArrayList<Filter>(filters.length);
      for (Filter filter : filters)
      {
         filter.configure(configuration);
         this.filters.add(filter);
      }

   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.traversal.Visitor#entry(java.io.File)
    */
   public void entry(File file) throws Exception
   {
      if (!isJarFile(file))
      {
         log.fine("File '" + file.getAbsolutePath() + "' was ommited from processing");
         return;
      }

      boolean matched = false;
      for (Filter filter : filters)
      {
         matched = filter.accept(file);
         if (matched)
         {
            log.info("File '" + file.getAbsolutePath() + "' was accepted by filter: " + filter.name());
            break;
         }
      }

      if (matched == false)
      {
         log.warning("File '" + file.getAbsolutePath() + "' was NOT accepted by any filter");
      }

   }

   private boolean isJarFile(File file)
   {
      return file.isFile() && file.getName().endsWith(".jar");
   }
}
