require 'jbundler'

describe "setup" do

  it "has the right classpath" do
    $CLASSPATH.size.should == 4
    $CLASSPATH.each do |lp|
      lp.should =~ /^file:/
    end
  end

  it 'lives in the right home' do
    Jars.home.should == 'uri:classloader://jars'
    Dir.pwd.should == 'uri:classloader://'
  end

  it 'has the right load-path' do
    $LOAD_PATH.each do |lp|
      lp.should =~ /^uri:classloader:|runnable.jar!\//
    end
  end

end
