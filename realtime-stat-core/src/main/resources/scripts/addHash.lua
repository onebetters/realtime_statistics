local hashKey = KEYS[1];
local key = KEYS[2];
local value = ARGV[1];
local ttlMis = tonumber(ARGV[2]);

local r = redis.call("HSET", hashKey, key, value);

if (not r or 0 == r) then
    return false;
end

if (ttlMis) then
    local oTtlMis = redis.call("PTTL", hashKey);
    if (oTtlMis <= 0) then
        redis.call("PEXPIRE", hashKey, ttlMis);
    end
end

return true;