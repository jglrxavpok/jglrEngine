require("color.lua");

local color = WHITE;
print(color.getRed(), color.getBlue(), color.getGreen());

days = {"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};
function test(nbr)
	for i,day in ipairs(days) do
		print("At "..i.." we have " .. day);
	end;
end;

function test2(nbr)
	print(nbr);
	return nbr;
end;

function getColor()
	return color;
end;