# jruby-mains

## vision

* install your gem and jar dependencies into some directory /tmp/myapp
* pack the gems + jars into gems.jar
* pack the whole application as it into application.jar
* run the application from these jars or put them along jruby.jar and jruby-rack.jar into WEB-INF/lib

### current situation

NOTE: at the time of writing you need to use jruby-complete.jar from [https://oss.sonatype.org/content/repositories/snapshots/org/jruby/jruby-complete/1.7.20-SNAPSHOT/](https://oss.sonatype.org/content/repositories/snapshots/org/jruby/jruby-complete/1.7.20-SNAPSHOT/) for the examples. jruby-mains itself work with old jruby versions.

first pack all the gem and jar dependencies into one jar:

	export JARS_HOME=/tmp/app/jars
	export GEM_HOME=/tmp/app
	export GEM_PATH=/tmp/app
	gem install bundler
	bundle install

	rmdir /tmp/app/doc /tmp/app/extensions /tmp/app/build_info
	rm -rf /tmp/app/cache
	jar -cvf gems.jar -C /tmp/app .

now pack the application:

	jar -cvf application.jar .

now run these jars with

	java -cp application.jar:gems.jar:jruby-complete.jar org.jruby.Main -C uri:classloader:/ -S rackup

this is equivalent to (here the classloader finds the application on the classpath ".")

	java -cp .:gems.jar:jruby-complete.jar org.jruby.Main -C uri:classloader:/ -S rackup

or very close to (the current directory is on the file system and not inside the classloader)

	java -cp .:gems.jar:jruby-complete.jar org.jruby.Main -C . -S rackup

the last can be reduced to (since ```GEM_PATH``` and ```JARS_HOME``` needs to be set)

    java -jar jruby-complete.jar $GEM_PATH/bin/rackup

the only difference is from where the application is loaded and where the current directory (-C) is pointing to. 

note: if ```GEM_PATH``` and ```JARS_HOME``` are not set then JRuby is looking for gems and jars in ```uri:classloader:/```.

ideally bundler should be only development dependency and then can be excluded.

## what about war files ?

just put the jruby-complete.jar, jruby-rack.jar, application.jar and the gems.jar into **WEB-INF/lib** and configure your **WEB-INF/web.xml** to use the classpath-layout

    <web-app>
      <context-param>
        <param-name>jruby.rack.layout_class</param-name>
        <param-value>JRuby::Rack::ClassPathLayout</param-value>
      </context-param>

      <filter>
        <filter-name>RackFilter</filter-name>
        <filter-class>org.jruby.rack.RackFilter</filter-class>
      </filter>
      <filter-mapping>
        <filter-name>RackFilter</filter-name>
        <url-pattern>/*</url-pattern>
      </filter-mapping>

      <listener>
        <listener-class>org.jruby.rack.RackServletContextListener</listener-class>
      </listener>
    </web-app>

then the web-application uses the same loading semantic as the standalone execution.

of course you can just unpack the gems.jar and application.jar into **WEB-INF/classes** which does not make a difference since from both location all the ruby sources are loaded via the jruby-classloader.

## using jruby-mains

this jruby-mains artifact expect the gems and jar dependencies be vendored at the root of the application.jar

	JARS_HOME=./jars GEMS_HOME=. GEMS_PATH=. bundle install
	jar -cvf application.jar .

and run it with

    java -cp application.jar:jruby-complete.jar:jruby-mains.jar org.jruby.Main bin/rackup

or via the **-S** switch

	java -cp application.jar:jruby-complete.jar:jruby-mains.jar org.jruby.mains.JarMain -S rackup

finally you can merge those three jar files into one and set the entry-point to **org.jruby.mains.JarMain**. this reduces the execution to

	java -jar application-uber.jar -S rackup

some executables spawn a new ruby process which is not working from a jruby-complete exectution. but in such cases you can use the entry-point **org.jruby.mains.ExtractingMain** which unpacks the jar into a temporary directory and then executes the application.

# development

use maven 3.3.x or the supplied ```mvnw``` wrappers. all integration tests in **src/it** are using the ruby pom DSL the pom.xml is just a dummy for the invoker plugin to find the test.

# license

Almost all code is under the LGPL-3 license.

# contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Added some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

# meta-fu

report issues and enjoy :) 


