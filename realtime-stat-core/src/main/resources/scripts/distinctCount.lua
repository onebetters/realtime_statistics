local hash = KEYS[1];
local key = ARGV[1];
local value = ARGV[2];
local ttlMis = tonumber(ARGV[3]);

local function hashcode(s)
    local h = 0;
    if (s) then
        for i = 1, string.len(s) do
            h = h * 31 + string.byte(s, i, i);
        end
    end
    return h;
end

local function rShift(x, by)
    return math.floor(x / 2 ^ by)
end

local vNum = value;
if (tonumber(value)) then
    vNum = tonumber(value);
else
    vNum = hashcode(value);
end
if (vNum > 2147483647) then
    vNum = vNum - rShift(vNum, 31) * 2147483648;
end
local hBit = rShift(vNum, 16);
local lBit = vNum - rShift(vNum, 16) * 65536;
local bitMap = "_" .. hash .. "|" .. key .. "|" .. hBit;
local bitValue = redis.call("GETBIT", bitMap, lBit);

if (0 == bitValue) then
    local existHash, existBit = nil, nil;
    if ttlMis then
        existHash = redis.call("EXISTS", hash) > 0;
        existBit = redis.call("EXISTS", bitMap) > 0;
    end
    redis.call("SETBIT", bitMap, lBit, 1);
    redis.call("ZINCRBY", hash, 1, key);
    if ttlMis then
        if (not existHash) then
            redis.call("PEXPIRE", hash, ttlMis);
        end
        if (not existBit) then
            redis.call("PEXPIRE", bitMap, ttlMis);
        end
    end
    return true;
else
    return false;
end
