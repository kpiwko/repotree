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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * 
 */
public class XmlUtils
{
   private static final DocumentBuilderFactory dbf;
   private static final TransformerFactory tf;

   static
   {
      dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      tf = TransformerFactory.newInstance();
      tf.setAttribute("indent-number", 3);
   }

   public static final Document create()
   {
      try
      {
         DocumentBuilder db = dbf.newDocumentBuilder();
         DOMImplementation impl = db.getDOMImplementation();
         return impl.createDocument("http://maven.apache.org/POM/4.0.0", "project", null);
      }
      catch (ParserConfigurationException e)
      {
         throw new RuntimeException(e);
      }
   }

   public static final File wrapAsFile(Document doc)
   {
      try
      {
         Transformer transformer = tf.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

         File tmp = File.createTempFile("bom", "xml");
         StreamResult result = new StreamResult(new FileWriter(tmp));
         DOMSource source = new DOMSource(doc);
         transformer.transform(source, result);

         return tmp;
      }
      catch (TransformerConfigurationException e)
      {
         throw new RuntimeException(e);
      }
      catch (TransformerFactoryConfigurationError e)
      {
         throw new RuntimeException(e);
      }
      catch (TransformerException e)
      {
         throw new RuntimeException(e);
      }
      catch (IOException e)
      {
         throw new RuntimeException(e);
      }

   }

   public static final String wrapAsString(Document doc)
   {
      try
      {
         Transformer transformer = tf.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

         StreamResult result = new StreamResult(new StringWriter());
         DOMSource source = new DOMSource(doc);
         transformer.transform(source, result);

         return result.getWriter().toString();
      }
      catch (TransformerConfigurationException e)
      {
         throw new RuntimeException(e);
      }
      catch (TransformerFactoryConfigurationError e)
      {
         throw new RuntimeException(e);
      }
      catch (TransformerException e)
      {
         throw new RuntimeException(e);
      }
   }

}
