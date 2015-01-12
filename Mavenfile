#-*- mode: ruby -*-

id 'de.saumya.mojo:jruby-mains:0.1.0-SNAPSHOT'

scope :provided do
  pom 'org.jruby:jruby:1.7.18'
  jar 'org.eclipse.jetty:jetty-server:${jetty.version}'
  jar 'org.eclipse.jetty:jetty-webapp:${jetty.version}'
end

properties 'jetty.version' => '8.1.14.v20131031', 'project.build.sourceEncoding' => 'utf-8'

plugin :compiler, '2.3.2', :target => '1.7', :source => '1.7'

properties 'tesla.dump.pom' => 'pom.xml', 'tesla.dump.readonly' => true
