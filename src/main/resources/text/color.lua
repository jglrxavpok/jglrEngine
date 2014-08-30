function Color(r,g,b)
	local self = {
	red = r;
	blue = b;
	green = g;
	};

	function self.getRed()
		return self.red;
	end;

	function self.getGreen()
		return self.green;
	end;

	function self.getBlue()
		return self.blue;
	end;

	function self.setBlue(newBlue)
		self.blue = newBlue;
		return self;
	end;

	function self.setRed(newRed)
		self.red = newRed;
		return self;
	end;

	function self.setGreen(newGreen)
		self.green = newGreen;
		return self;
	end;
	return self;
end;

WHITE = Color(1,1,1);
RED = Color(1,0,0);
GREEN = Color(0,1,0);
BLUE = Color(0,0,1);
BLACK = Color(0,0,0);
GRAY = Color(0.5,0.5,0.5);

print(WHITE);