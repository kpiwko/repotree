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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.wfk.repotree.signature.Signature.SignatureBuilder;
import org.jboss.wfk.repotree.signature.TinyTreeWalker.Traversal;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeFilter;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class SignatureLoader extends Traversal<List<Signature>>
{

   /**
    * @param root
    */
   public SignatureLoader(File file)
   {
      super(parseFile(file), NodeFilter.SHOW_ELEMENT);
   }

   /**
    * @param root
    */
   public SignatureLoader(Node node)
   {
      super(node, NodeFilter.SHOW_ELEMENT);
   }

   private static Document parseFile(File file)
   {
      DocumentBuilder builder;
      try
      {
         builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         return builder.parse(file);
      }
      catch (ParserConfigurationException e)
      {
         throw new RuntimeException("Unable to parse signatures file", e);
      }
      catch (SAXException e)
      {
         throw new RuntimeException("Unable to parse signatures file", e);
      }
      catch (IOException e)
      {
         throw new RuntimeException("Unable to parse signatures file", e);
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jboss.wfk.repotree.signature.TinyTreeWalker.Traversal#processNode(java.lang.Object, org.w3c.dom.Node)
    */
   @Override
   protected void processNode(List<Signature> result, Node current)
   {
      String name = current.getNodeName();

      if ("signature".equals(name))
      {

         SignatureBuilder builder = new SignatureBuilder();

         NodeList children = current.getChildNodes();
         for (int i = 0; i < children.getLength(); i++)
         {
            Node child = children.item(i);
            if ("key".equals(child.getNodeName()))
            {
               builder.signature(child.getTextContent());
            }
            else if ("coordinates".equals(child.getNodeName()))
            {
               builder.coordinates(child.getTextContent());
            }
            else if ("artifactId".equals(child.getNodeName()))
            {
               builder.artifact(child.getTextContent());
            }
            else if ("groupId".equals(child.getNodeName()))
            {
               builder.group(child.getTextContent());
            }
            else if ("type".equals(child.getNodeName()))
            {
               builder.type(child.getTextContent());
            }
            else if ("version".equals(child.getNodeName()))
            {
               builder.version(child.getTextContent());
            }
            else if ("classifier".equals(child.getNodeName()))
            {
               builder.classifier(child.getTextContent());
            }

         }

         result.add(builder.build());
      }
   }
}
