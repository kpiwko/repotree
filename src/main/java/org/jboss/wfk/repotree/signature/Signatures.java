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
package org.jboss.wfk.repotree.signature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.wfk.repotree.artifact.Artifact;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class Signatures
{
   private Map<String, Artifact> db;

   public Signatures()
   {
      this.db = new HashMap<String, Artifact>();
   }

   public Artifact lookup(String signature)
   {
      if (signature == null)
      {
         return null;
      }

      return db.get(signature);
   }

   public Signatures load(File file) throws IOException
   {
      SignatureLoader loader = new SignatureLoader(file);
      List<Signature> list = new ArrayList<Signature>();

      loader.levelTraversal(list, 2);

      for (Signature sig : list)
      {
         this.db.put(sig.getSignature(), sig.getArtifact());
      }

      return this;
   }
}
