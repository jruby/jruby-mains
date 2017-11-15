puts "here I am: #{Dir.pwd.sub( /^.*jruby-mains-[0-9]+/, 'jruby-mains/' )}#{__FILE__.sub( /^.+:/, '' )}"
puts "#{ARGV[0]} Bundler defined: #{defined?(Bundler) ? true : false}"
