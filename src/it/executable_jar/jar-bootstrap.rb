require_relative 'test'

other = org.jruby.embed.IsolatedScriptingContainer.new(org.jruby.embed.LocalContextScope::THREADSAFE)
other.setCurrentDirectory('uri:classloader:/')

# HACK for jruby-1.7.21
other.runScriptlet("ENV.replace(JRuby.runtime.instance_config.environment)")

other.runScriptlet("$other = #{JRuby.runtime.object_id}")

# TODO needs fix in jruby
#other.runScriptlet( "require_relative 'test_other'" )
other.runScriptlet( "require 'uri:classloader:test_other'" )

other.terminate
