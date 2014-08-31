require("color.lua");
require "__jglrEngine__";

color = Color(1,1,1);

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

object = Engine.SceneObject();
comp = Engine.SceneComponentTest();
Engine.addComponent("test", object, comp);
Engine.addToWorld("test", object);