# jruby-mains

## vision

* vendor your gems into ./vendor
* pack the whole application as it into jar file
* run the application from the jar

jruby does provide this:

    JARS_HOME=vendor GEMS_HOME=vendor GEMS_PATH=vendor bundle install
	jar -cvf application.jar .

not run it with

	export JARS_HOME=uri:classloader:/vendor
	export GEMS_HOME=uri:classloader:/vendor
	export GEMS_PATH=uri:classloader:/vendor
	java -cp application.jar:jruby-complete.jar org.jruby.Main -C uri:classloader:/ vendor/bin/rackup

this is the same execution then

    java -jar jruby-complete.jar -C uri:classloader:/ vendor/bin/rackup

the only difference is where the application loaded from but the current working directory is always at the root of the classloader.

## what about war files ?

well just put the jruby-complete.jar, jruby-rack.jar and the application.jar into **WEB-INF/lib** and configure your **WEB-INF/web.xml** to use the classpath-layout

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

then the web-application uses the same loading as the standalone execution.

of course you can just unpack the application.jar into **WEB-INF/classes** which does not make a difference since at both location all the ruby sources are loaded via the classloader.

## using jruby-mains

this jruby-mains artifact expect the gems and jar dependencies be vendored at the root of the application.jar

	JARS_HOME=. GEMS_HOME=. GEMS_PATH=. bundle install
	jar -cvf application.jar .

and run it with

    java -cp application.jar:jruby-complete.jar:jruby-mains.jar org.jruby.Main bin/rackup

packing the bin/* files into ```META-INF/jruby.home/bin``` will allow to execute them via the **-S**

	java -cp application.jar:jruby-complete.jar:jruby-mains.jar de.saumya.mojo.mains.JarMain -S rackup

finally you can merge those three jar files into one and set the entry-point to **de.saumya.mojo.mains.JarMain**. this reduces the execution to

	java -jar application-uber.jar -S rackup

some executables spawn a new ruby process which is not working from a jruby-complete exectution. but in such cases you can use the entry-point **de.saumya.mojo.mains.ExtractingMain** which unpacks the jar into a temporary directory and then executes the application.

# to be continued


