# NOTE: This has to be commented out until the integration tests properly
# include the `Jars.lock` file in the archive which is required
#
#java_import 'org.slf4j.Logger'
#java_import 'org.slf4j.LoggerFactory'
#logger = LoggerFactory.getLogger('jruby-mains')
#logger.info('running')

puts "here I am: #{Dir.pwd.sub( /^.*jruby-mains-[0-9]+/, 'jruby-mains/' )}#{__FILE__.sub( /^.+:/, '' )}"
