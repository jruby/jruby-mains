require 'jbundler'

describe "something" do
  it "does something" do
    $CLASSPATH.size.should == 4
    $CLASSPATH.each do |lp|
      lp.should =~ /^file:#{Dir.pwd}/
    end
    Jars.home.should == Dir.pwd
    JRuby.runtime.jruby_home.should == File.join(Dir.pwd, 'META-INF/jruby.home')
    $LOAD_PATH.each do |lp|
      lp.should =~ /^#{Dir.pwd}/
    end
  end
end
