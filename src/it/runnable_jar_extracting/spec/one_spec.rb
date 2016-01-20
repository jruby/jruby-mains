require 'jbundler'

describe "setup" do

  it "has the right classpath" do
    $CLASSPATH.size.should == 4
    $CLASSPATH.each do |lp|
      lp.should =~ /^file:/
    end
  end

  it 'lives in the right home' do
    java.io.File.new(Jars.home).canonical_path.should == java.io.File.new(Dir.pwd, 'jars').canonical_path
    java.io.File.new(JRuby.runtime.jruby_home).canonical_path.should == java.io.File.new(Dir.pwd, 'META-INF/jruby.home').canonical_path
  end

  it 'has the right load-path' do
    pwd = java.io.File.new(Dir.pwd).canonical_path
    $LOAD_PATH.each do |lp|
      java.io.File.new(lp).canonical_path.should =~ /^#{pwd}/
    end
  end

end
