#-*- mode: ruby -*-

id 'de.saumya.mojo:jruby-mains:0.1.0'

repository( :id => 'rso-public-grid',
            :url => 'https://repository.sonatype.org/content/groups/sonatype-public-grid',
            :releases => true, :snapshots => true )

license :name => 'MIT'

developer( :name => 'Christian Meier',
           :email => 'm.kristian@web.de' )

github = 'mkristian/jruby-mains'
scm( :developerConnection => "scm:git:ssh://git@github.com:#{github}.git",
     :connection => "scm:git:https://github.com/#{github}.git",
     :url => "https://github.com/#{github}" )

scope :provided do
  pom 'org.jruby:jruby:1.7.19'
  jar 'org.eclipse.jetty:jetty-server:${jetty.version}'
  jar 'org.eclipse.jetty:jetty-webapp:${jetty.version}'
end

properties 'jetty.version' => '8.1.14.v20131031', 'project.build.sourceEncoding' => 'utf-8'

plugin :compiler, '2.3.2', :target => '1.7', :source => '1.7'

properties 'tesla.dump.pom' => 'pom.xml', 'tesla.dump.readonly' => true

distribution_management do
  snapshot_repository :id => 'sonatype-nexus-snapshots', :url =>  'https://oss.sonatype.org/content/repositories/snapshots'
  repository :id => 'sonatype-nexus-staging', :url =>  'https://oss.sonatype.org/service/local/staging/deploy/maven2'
end

plugin :invoker, '1.8' do
  execute_goals( :install, :run,
                 :id => 'integration-test',
                 :goals => [:verify],
                 :streamLogs => true,
                 :cloneProjectsTo => '${project.build.directory}' )
end
