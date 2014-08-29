days = {"Sunday", "Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday"};
function test(nbr)
	for i,day in ipairs(days) do
		print("At "..i.." we have " .. day);
	end;
end;