local key = KEYS[1];
local value = ARGV[1];
local ttlMis = tonumber(ARGV[2]);

local r = redis.call("SADD", key, value);
if (not r or 0 == r) then
    return false;
end

if (ttlMis) then
    local oTtlMis = redis.call("PTTL", key);
    if (oTtlMis <= 0) then
        redis.call("PEXPIRE", key, ttlMis);
    end
end

return true;