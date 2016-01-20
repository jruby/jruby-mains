require 'jbundler'

describe "something" do
  it "does something" do
    $CLASSPATH.size.should == 4
    $CLASSPATH.each do |lp|
      lp.should =~ /^file:/
    end
    Jars.home.should == 'uri:classloader:/WEB-INF/classes/jars'
    Dir.pwd.should == 'uri:classloader://WEB-INF/classes/'
    $LOAD_PATH.each do |lp|
      lp.should =~ /^uri:classloader:\/\/?WEB-INF\/classes|uri:classloader:\/META-INF\/jruby.home\/lib\/ruby/
    end
  end
end
