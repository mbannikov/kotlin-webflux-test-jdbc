math.randomseed( os.time() )

-- Utils
function get_alpha_char()
    selection = math.random(1, 3)
    if selection == 1 then return string.char(math.random(65, 90)) end
    if selection == 2 then return string.char(math.random(97, 122)) end
    return string.char(math.random(48, 57))
end

function get_random_string(length)
    length = length or 1
    if length < 1 then return nil end

    local array = {}
    for i = 1, length do
        array[i] = get_alpha_char()
    end

    return table.concat(array)
end


-- Request factories
function get_register_request(username, email, password)
    return wrk.format(
        'POST', 
	'/register', 
	{ ['Content-Type'] = 'application/json' }, 
	'{"username": "' .. username .. '", "email": "' .. email ..'", "password": "' .. password .. '"}'
    )
end

function get_login_request(email, password)
    return wrk.format(
        'POST', 
	'/login', 
	{ ['Content-Type'] = 'application/json' }, 
	'{"email": "' .. email ..'", "password": "' .. password .. '"}'
    )
end


-- wrk
thread_id = 1

setup = function(thread)
   thread:set('id', thread_id)
   thread_id = thread_id + 1
end

request = function()
    local username = id .. "_" .. get_random_string(12)
    local email = username .. '@gmail.com'
    local password = username

    local list = {
        get_register_request(username, email, password),
        get_login_request(email, password)
    }

    return table.concat(list)
end

response = function(status, headers, body)
    local response_status = body:match('"status":"(.-)"')
    if response_status ~= 'success' then
    	print(body)
    end
end
