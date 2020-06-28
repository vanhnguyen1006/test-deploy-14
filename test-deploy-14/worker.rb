STDOUT.sync = true
puts "Starting up"

trap('TERM') do
  puts "Ignoring TERM signal - not a good idea"
end

loop do
  puts "Pretending to do work"
  sleep 3
end