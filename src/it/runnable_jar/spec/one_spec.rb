require 'jbundler'

describe "something" do
  it "does something" do
p $CLASSPATH
    $CLASSPATH.size.should == 4
    Jars.home.should == 'uri:classloader://'
    Dir.pwd.should == 'uri:classloader://'
    $LOAD_PATH.each do |lp|
      lp.should =~ /^uri:classloader:|runnable.jar!\//
    end
  end
end
