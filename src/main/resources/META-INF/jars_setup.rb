begin
  require 'jars/setup'
rescue LoadError
  # older jrubies do not come with jar-dependencies as default gem
  # we could check for existence of Jars.lock and warn. but feels too much.
end
