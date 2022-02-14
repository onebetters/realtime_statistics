local keys = KEYS;

local result = {};
for _, key in pairs(keys) do
    if (1 == redis.call("EXISTS", key)) then
        result[#result + 1] = key;
    end
end
return result;