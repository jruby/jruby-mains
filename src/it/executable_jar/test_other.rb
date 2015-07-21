require 'minitest/autorun'

describe 'ENV' do

  it 'has $other' do
    $other.wont_equal JRuby.runtime.object_id
  end

  it 'has first entry LOAD_PATH' do
    # TODO needs trailing /
    $LOAD_PATH.first.must_equal 'uri:classloader:'
  end

  it 'has GEM_HOME set' do
    ENV['GEM_HOME'].must_equal 'uri:classloader://'
  end

  it 'has GEM_PATH set' do
    ENV['GEM_PATH'].must_equal 'uri:classloader://'
  end

  it 'has JARS_HOME set' do
    ENV['JARS_HOME'].must_equal 'uri:classloader://jars'
  end

end
