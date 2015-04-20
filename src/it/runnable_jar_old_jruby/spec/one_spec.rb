require 'jbundler'

describe "setup" do

  it "has the right classpath" do
    $CLASSPATH.size.should == 4
    $CLASSPATH.each do |lp|
      lp.should =~ /^file:/
    end
  end

  it 'lives in the right home' do
    Jars.home.should == Dir.pwd
    JRuby.runtime.jruby_home.should == File.join(Dir.pwd, 'META-INF/jruby.home')
  end

  it 'has the right load-path' do
    $LOAD_PATH.each do |lp|
      lp.should =~ /^#{Dir.pwd}/
    end
  end

  it 'uses the right jruby version' do
    JRUBY_VERSION.should == '1.7.12'
  end

end
