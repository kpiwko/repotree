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
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.model.Model;
import org.sonatype.aether.util.artifact.DefaultArtifact;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * @author Benjamin Bentmann
 * 
 */
public class Artifact
{
   private final String groupId;
   private final String artifactId;
   private final String extension;
   private final String classifier;
   private final String version;

   /**
    * 
    * @param coordinates The artifact coordinates in the format {@code <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>}, must not be {@code null}.
    */
   public Artifact(String coordinates)
   {
      Pattern p = Pattern.compile("([^: ]+):([^: ]+)(:([^: ]*)(:([^: ]+))?)?:([^: ]+)");
      Matcher m = p.matcher(coordinates);
      if (!m.matches())
      {
         throw new IllegalArgumentException("Bad artifact coordinates"
               + ", expected format is <groupId>:<artifactId>[:<extension>[:<classifier>]]:<version>");
      }
      groupId = m.group(1);
      artifactId = m.group(2);
      extension = get(m.group(4), "jar");
      classifier = get(m.group(6), "");
      version = m.group(7);
   }

   public Artifact(String groupId, String artifactId, String version)
   {
      this(groupId, artifactId, "jar", "", version);
   }

   public Artifact(String groupId, String artifactId, String extension, String classifier, String version)
   {
      this.groupId = groupId;
      this.artifactId = artifactId;
      this.extension = extension;
      this.classifier = classifier;
      this.version = version;
   }

   public Artifact(Model model)
   {
      this.groupId = !empty(model.getGroupId()) ? model.getGroupId() : model.getParent().getGroupId();
      this.artifactId = model.getArtifactId();
      this.version = !empty(model.getVersion()) ? model.getVersion() : model.getParent().getVersion();
      this.extension = model.getPackaging();
      this.classifier = "";
   }

   public static Artifact fromTree(String dependencyCoords)
   {
      // sanity checks
      int index = 0;
      while (index < dependencyCoords.length())
      {
         char c = dependencyCoords.charAt(index);
         if (c == '\\' || c == '|' || c == ' ' || c == '+' || c == '-')
         {
            index++;
         }
         else
         {
            break;
         }
      }

      for (int testIndex = index, i = 0; i < 4; i++)
      {
         testIndex = dependencyCoords.substring(testIndex).indexOf(":");
         if (testIndex == -1)
         {
            throw new IllegalArgumentException("Invalid format of the dependency coordinates for " + dependencyCoords);
         }
      }

      // parse
      StringTokenizer st = new StringTokenizer(dependencyCoords.substring(index), ":");

      String groupId = st.nextToken();
      String artifactId = st.nextToken();
      String extension = st.nextToken();
      String classifier, version;

      // this is the root artifact
      if (index == 0)
      {
         if (st.countTokens() == 1)
         {
            classifier = "";
            version = st.nextToken();
         }
         else if (st.countTokens() == 2)
         {
            classifier = st.nextToken();
            version = st.nextToken();
         }
         else
         {
            throw new IllegalArgumentException("Invalid format of the dependency coordinates for " + dependencyCoords);
         }
      }
      // otherwise, omitting the scope
      else
      {
         if (st.countTokens() == 2)
         {
            classifier = "";
            version = st.nextToken();
         }
         else if (st.countTokens() == 3)
         {
            classifier = st.nextToken();
            version = st.nextToken();
         }
         else
         {
            throw new IllegalArgumentException("Invalid format of the dependency coordinates for " + dependencyCoords);
         }
      }

      return new Artifact(groupId, artifactId, extension, classifier, version);
   }

   public org.sonatype.aether.artifact.Artifact attachFile(File file)
   {
      return new DefaultArtifact(groupId, artifactId, classifier, extension, version, null, file);
   }

   public String filename()
   {
      StringBuilder sb = new StringBuilder();
      sb.append(artifactId).append("-").append(version);
      if (classifier.length() != 0)
      {
         sb.append("-").append(classifier);
      }
      sb.append(".").append(extension);

      return sb.toString();
   }

   public String toString()
   {
      StringBuilder sb = new StringBuilder();

      sb.append("groupId=").append(groupId).append(", ");
      sb.append("artifactId=").append(artifactId).append(", ");
      sb.append("type=").append(extension).append(", ");
      sb.append("version=").append(version);

      if (classifier != "")
      {
         sb.append(", classifier=").append(classifier);
      }

      return sb.toString();
   }

   private static String get(String value, String defaultValue)
   {
      return (value == null || value.length() <= 0) ? defaultValue : value;
   }

   private static boolean empty(String value)
   {
      return value == null || value.length() == 0;
   }

}
