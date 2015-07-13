# if the GEM_PATH set to uri:classloader://WEB-INF/classes then
# JRuby does ignore it. this statement sets it - assuming a single path
# element
Gem::Specification.add_dir ENV["GEM_PATH"] rescue nil

# bundler includes Bundler::SharedHelpers into its runtime
# adding the included method allows to monkey patch the runtime
# the moment it is used. i.e. no need to activate the bundler gem
module Bundler
  module Patch
    def clean_load_path
      # nothing to be done for JRuby
    end
  end
  module SharedHelpers
    def included(bundler)
      bundler.send :include, Patch
    end
  end
end
