#-*- mode: ruby -*-

id 'de.saumya.mojo:jruby-mains:0.1.1-SNAPSHOT'

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
url 'http://github.com/#{github}'

description 'a set of main method to launch a jruby application from within a jar or war file or start jetty as executable'

scope :provided do
  pom 'org.jruby:jruby:1.7.20-SNAPSHOT'
  jar 'org.eclipse.jetty:jetty-server:${jetty.version}'
  jar 'org.eclipse.jetty:jetty-webapp:${jetty.version}'
end

properties( 'jetty.version' => '8.1.14.v20131031',
            'project.build.sourceEncoding' => 'utf-8',
            'polyglot.dump.pom' => 'pom.xml' )

plugin :compiler, '2.3.2', :target => '1.7', :source => '1.7'

distribution_management do
  snapshot_repository :id => 'sonatype-nexus-snapshots', :url =>  'https://oss.sonatype.org/content/repositories/snapshots'
  repository :id => 'sonatype-nexus-staging', :url =>  'https://oss.sonatype.org/service/local/staging/deploy/maven2'
end

profile :id => 'release' do
  activation do
    property :name => 'performRelease', :value => 'true'
  end

  build do
    default_goal :deploy
  end

  plugin :source, '2.4' do
    execute_goal 'jar-no-fork', :id => 'attach-sources'
  end
  plugin :javadoc, '2.10.1' do
    execute_goal :jar, :id => 'attach-javadocs'
  end
  plugin :gpg, '1.5' do
    execute_goal :sign, :id => 'sign-artifacts', :phase => 'verify'
  end
end

plugin :invoker, '1.8' do
  execute_goals( :install, :run,
                 :id => 'integration-test',
                 :goals => [:verify],
                 :streamLogs => true,
                 :cloneProjectsTo => '${project.build.directory}',
                 :properties => { 'artifact.version' => '${project.version}',
                   'jruby.version' => '${jruby.version}',
                   'jruby.plugins.version' => '${jruby.plugins.version}',
                   'bundler.version' => '1.9.2',
                   # dump pom for the time being - for travis
                   'polyglot.dump.pom' => 'pom.xml'} )
end
