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
package org.jboss.wfk.repotree.filter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;

import org.jboss.wfk.repotree.api.Configuration;
import org.jboss.wfk.repotree.api.Filter;
import org.jboss.wfk.repotree.artifact.Artifact;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class SignatureFilter implements Filter
{
   private static final Logger log = Logger.getLogger(SignatureFilter.class.getName());

   private static final String SIGNATURE_ENTRY_ATTR = "SHA1-Digest-Manifest";

   private Configuration configuration;

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.filter.Filter#accept(java.io.File)
    */
   public boolean accept(File file) throws Exception
   {
      JarFile jar = new JarFile(file);

      Enumeration<JarEntry> elements = jar.entries();
      List<JarEntry> candidates = new ArrayList<JarEntry>();

      while (elements.hasMoreElements())
      {
         JarEntry entry = elements.nextElement();
         if (!entry.isDirectory() && isSignatureFile(entry.getName()))
         {
            candidates.add(entry);
         }
      }

      if (candidates.isEmpty())
      {
         return false;
      }

      for (JarEntry entry : candidates)
      {
         String signature = extractSignature(jar, entry);
         log.fine("Signature for: " + file.getName() + " is " + "'" + signature + "'");
         Artifact artifact = configuration.getSignatures().lookup(signature);
         if (artifact == null)
         {
            continue;
         }

         if (configuration.getRepositorySystem().installArtifact(artifact.attachFile(file), null))
         {
            return true;
         }
      }

      return false;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.filter.Filter#configure(org.jboss.wfk.repotree.Configuration)
    */
   public void configure(Configuration configuration)
   {
      this.configuration = configuration;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.filter.Filter#name()
    */
   public String name()
   {
      return "Signature";
   }

   private String extractSignature(JarFile jar, JarEntry signatureEntry)
   {
      try
      {
         Manifest signature = new Manifest();
         signature.read(jar.getInputStream(signatureEntry));
         Attributes attrs = signature.getMainAttributes();
         return attrs.getValue(SIGNATURE_ENTRY_ATTR);
      }
      catch (IOException e)
      {
         log.warning("Unable to extract signature from a signed jar file: " + jar.getName() + ", cause: " + e.getMessage());
      }

      return null;
   }

   private boolean isSignatureFile(String name)
   {
      return name.startsWith("META-INF") && name.toUpperCase().endsWith(".SF");
   }

}
