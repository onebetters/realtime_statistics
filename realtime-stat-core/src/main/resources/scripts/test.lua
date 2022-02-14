print("version: ", _VERSION)
print("string.byte: ", string.byte("test", 1, #"test"));
print("二进制转十进制", tonumber("1101", '2'))

print("当前秒数: ", os.time())
print("clock: ", os.clock())

c = { 1, 2, 3 };

print("table.unpack: ", table.unpack(c));

for k, v in pairs(c) do
    print("key: ", k, "value: ", v)
end

function lshift(x, by)
    return x * 2 ^ by
end

function rshift(x, by)
    return math.floor(x / 2 ^ by)
end

function hashcode(s)
    local h = 0;
    if (s) then
        for i = 1, string.len(s) do
            h = h * 31 + string.byte(s, i, i);
        end
    end
    return h;
end

local INT32_MAX, INT32_MIN = 2147483648, -2147483648;
local toInt32 = function(i)
    if i < INT32_MIN then
        return INT32_MAX - (INT32_MIN - i) % (INT32_MAX - INT32_MIN)
    end
    return INT32_MIN + (i - INT32_MIN) % (INT32_MAX - INT32_MIN)
end


local x = tonumber("6466802");

local hc = hashcode("A1064929");
print("hashcode", hc)

if (hc > 2147483648) then
    print(hc - rshift(hc, 31) * 2147483648)
end

--- 高16位
print("高16位: ", rshift(x, 16) )

--- 低16位
print("低16位: ", x - rshift(x, 16) * 65536)



local keys = {'h', 'k1', 'k2', 'k3'};
print(#keys);

for i = 2, #keys - 1 do
    print(keys[i]);
end