java_import 'org.slf4j.LoggerFactory'
logger = LoggerFactory.getLogger('jruby-mains')
logger.info('running')

puts "here I am: #{Dir.pwd.sub( /^.*jruby-mains-[0-9]+/, 'jruby-mains/' )}#{__FILE__.sub( /^.+:/, '' )}"
