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

import org.jboss.wfk.repotree.artifact.Artifact;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class Signature
{
   private final String signature;
   private final Artifact artifact;

   public Signature(String signature, Artifact artifact)
   {
      this.signature = signature;
      this.artifact = artifact;
   }

   /**
    * @return the signature
    */
   public String getSignature()
   {
      return signature;
   }

   /**
    * @return the artifact
    */
   public Artifact getArtifact()
   {
      return artifact;
   }

   public static class SignatureBuilder
   {
      private String signature;
      private String coordinates;

      private String groupId;
      private String artifactId;
      private String classifier = "";
      private String type = "jar";
      private String version;

      public Signature build()
      {
         if (signature == null || (coordinates == null && (groupId == null || artifactId == null || version == null)))
         {
            throw new IllegalArgumentException("Cannot build signature using incomplete information");
         }

         Artifact artifact;
         if (coordinates != null)
         {
            artifact = new Artifact(coordinates);
         }
         else
         {
            artifact = new Artifact(groupId, artifactId, type, classifier, version);
         }

         return new Signature(signature, artifact);
      }

      public SignatureBuilder signature(String signature)
      {
         this.signature = signature;
         return this;
      }

      public SignatureBuilder coordinates(String coordinates)
      {
         this.coordinates = coordinates;
         return this;
      }

      public SignatureBuilder artifact(String artifactId)
      {
         this.artifactId = artifactId;
         return this;
      }

      public SignatureBuilder group(String groupId)
      {
         this.groupId = groupId;
         return this;
      }

      public SignatureBuilder version(String version)
      {
         this.version = version;
         return this;
      }

      public SignatureBuilder type(String type)
      {
         this.type = type;
         return this;
      }

      public SignatureBuilder classifier(String classifier)
      {
         this.classifier = classifier;
         return this;
      }

   }
}
