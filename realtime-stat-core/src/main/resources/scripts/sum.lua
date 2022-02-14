local hash = KEYS[1];
local key = ARGV[1];
local score = ARGV[2];
local ttlMis = tonumber(ARGV[3]);

local existKey = redis.call("EXISTS", hash) > 0;

redis.call("ZINCRBY", hash, score, key);

if (ttlMis and not existKey) then
    redis.call("PEXPIRE", hash, ttlMis);
end

return true;