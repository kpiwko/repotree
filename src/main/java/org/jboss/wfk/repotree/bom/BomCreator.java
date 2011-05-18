/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.wfk.repotree.bom;

import org.sonatype.aether.artifact.Artifact;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//import org.jboss.wfk.repotree.artifact.Artifact;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
public class BomCreator
{
   private Document doc;

   private Node dependencies;

   private org.jboss.wfk.repotree.artifact.Artifact artifact;

   public BomCreator(org.jboss.wfk.repotree.artifact.Artifact artifact)
   {
      this.artifact = artifact;
      this.doc = XmlUtils.create();
      this.doc.setXmlStandalone(true);
      header(artifact);
   }

   public Document getDocument()
   {
      return doc;
   }

   /**
    * @return the artifact
    */
   public org.jboss.wfk.repotree.artifact.Artifact getArtifact()
   {
      return artifact;
   }

   public BomCreator append(Artifact artifact)
   {
      Node dependency = appendChild(dependencies, "dependency");
      appendChild(dependency, "groupId", artifact.getGroupId());
      appendChild(dependency, "artifactId", artifact.getArtifactId());
      appendChild(dependency, "version", artifact.getVersion());
      appendChild(dependency, "type", artifact.getExtension());
      appendChild(dependency, "classifier", artifact.getClassifier());
      return this;
   }

   private Node appendChild(Node parent, String tag, String value)
   {
      if (value == null || value == "")
      {
         return null;
      }

      Element child = doc.createElement(tag);
      child.setTextContent(value);
      parent.appendChild(child);
      return child;
   }

   private Node appendChild(Node parent, String tag)
   {
      Element child = doc.createElement(tag);
      parent.appendChild(child);
      return child;
   }

   private void header(org.jboss.wfk.repotree.artifact.Artifact artifact)
   {
      Element root = doc.getDocumentElement();

      root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
      root.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd");

      appendChild(root, "modelVersion", "4.0.0");
      appendChild(root, "groupId", artifact.getGroupId());
      appendChild(root, "artifactId", artifact.getArtifactId());
      appendChild(root, "type", "pom");
      appendChild(root, "version", artifact.getVersion());

      Node dmngt = appendChild(root, "dependencyManagement");
      this.dependencies = appendChild(dmngt, "dependencies");
   }

}
