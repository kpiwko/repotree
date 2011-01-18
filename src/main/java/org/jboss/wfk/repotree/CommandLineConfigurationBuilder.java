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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jboss.wfk.repotree.api.Configuration;
import org.jboss.wfk.repotree.api.Filter;
import org.jboss.wfk.repotree.api.ServiceLoader;
import org.jboss.wfk.repotree.artifact.MavenRepositorySystem;
import org.jboss.wfk.repotree.signature.Signatures;

public class CommandLineConfigurationBuilder
{
   private String[] args;
   private ServiceLoader service;

   public CommandLineConfigurationBuilder(String[] args)
   {
      this.args = args;
      this.service = new DynamicServiceLoader();
   }

   public Configuration build()
   {
      Configuration configuration = new Configuration();

      CommandLineParser parser = new GnuParser();
      Options options = options();
      CommandLine cmd = null;

      try
      {
         cmd = parser.parse(options, args);
      }
      catch (ParseException e)
      {
         // help
         if (cmd != null && cmd.hasOption("help"))
         {
            System.out.println("Load a directory into the Maven local repository");
            showUsage(options);
            System.exit(0);
         }

         System.out.println(e.getMessage());
         showUsage(options);
         System.exit(1);
      }

      // show help
      if (cmd.hasOption("help"))
      {
         showUsage(options);
         System.exit(0);
      }

      Collection<Filter> filters = service.all(Filter.class);
      configuration.setFilterPlugins(filters);
      if (cmd.hasOption("filter"))
      {
         List<Filter> filtered = new ArrayList<Filter>(filters.size());
         for (String option : cmd.getOptionValues("filter"))
         {
            for (Filter filter : filters)
            {
               if (filter.name().equalsIgnoreCase(option))
               {
                  filtered.add(filter);
               }
            }
         }

         if (filtered.isEmpty())
         {
            System.err.println("No such filters are available");
            showUsage(options);
            System.exit(1);
         }

         configuration.setFilterPlugins(filtered);
      }

      if (cmd.hasOption("local-repository"))
      {
         configuration.setRepositorySystem(new MavenRepositorySystem(new File(cmd.getOptionValue("local-repository"))));
      }

      Signatures signatures = new Signatures();
      if (cmd.hasOption("signature-file"))
      {
         for (String file : cmd.getOptionValues("signature-file"))
         {
            try
            {
               signatures.load(new File(file));
            }
            catch (IOException e)
            {
               System.err.println("Unable to load signatures from file: " + file);
               e.printStackTrace();
            }
         }
      }
      configuration.setSignatures(signatures);

      // directories to be traversed
      if (cmd.hasOption("directory"))
      {

         List<File> dirs = new ArrayList<File>();
         for (String dir : cmd.getOptionValues("directory"))
         {
            dirs.add(new File(dir));
         }
         configuration.setDirectories(dirs);
      }
      
      // install pom files as well
      if(cmd.hasOption("install-poms"))
      {
         configuration.setInstallingPoms(true);
      }

      return configuration;

   }

   @SuppressWarnings( { "static-access" })
   private Options options()
   {
      Options options = new Options();
      Option helpOpt = OptionBuilder.withLongOpt("help")
               .withDescription("Shows usage of the application")
               .create("h");
      options.addOption(helpOpt);

      Option targetOpt = OptionBuilder.withLongOpt("signature-file")
               .withArgName("signature-file")
               .hasArgs(Option.UNLIMITED_VALUES)
               .withValueSeparator(' ')
               .withDescription("A file where JAR signatures are retrieved from. Jar signature is SHA1-Manifest-Digest field value")
               .create('s');
      options.addOption(targetOpt);

      Option listenerOpt = OptionBuilder.withLongOpt("local-repository")
               .withArgName("local-repository")
               .hasArg()
               .withValueSeparator(' ')
               .withDescription("A path to the local repository where artifacts will be stored")
               .isRequired(true)
               .create('r');
      options.addOption(listenerOpt);

      Option directoryOpt = OptionBuilder.withLongOpt("directory")
            .withArgName("directory")
            .hasArgs(Option.UNLIMITED_VALUES)
            .withValueSeparator(' ')
            .withDescription("A path which is recursively traversed to find out artifacts")
            .isRequired(true)
            .create('d');
      options.addOption(directoryOpt);

      Option outputOpt = OptionBuilder.withLongOpt("filter")
               .withArgName("filter")
               .hasArgs(Option.UNLIMITED_VALUES)
               .withValueSeparator(' ')
               .withDescription("A name of filter to be used for resolution of Maven artifacts. If no filter is specified, all available filters are selected. Currently available filters are: " + availableFilters())
               .create('f');
      options.addOption(outputOpt);
      
      Option installPoms = OptionBuilder.withLongOpt("install-poms")
               .withDescription("Install pom.xml files into repository if available")
               .create('i');
      options.addOption(installPoms);

      return options;
   }

   private void showUsage(Options options)
   {
      HelpFormatter help = new HelpFormatter();
      help.printHelp("java -cp <required libraries> -jar <jar-name>", options, true);
   }

   private String availableFilters()
   {

      StringBuilder sb = new StringBuilder("\n\t");
      for (Filter f : service.all(Filter.class))
      {
         sb.append(f.name()).append("\n\t");
      }

      return sb.toString();
   }
}