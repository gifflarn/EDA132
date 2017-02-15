
public class Coords {
	public int x;
	public int y;
	public int value;

	public Coords(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + (char) (x + 'a') + (y+1) + ")";
	}

	@Override
	public boolean equals(Object o) {
	    if (o == null) {
	        return false;
	    }
	    if (!(o instanceof Coords)) {
	        return false;
	    }
	    return (x == ((Coords) o).x && y == ((Coords) o).y);
	}
	@Override
	public int hashCode()
	{
	    return new StringBuilder("").append(x+'a'-1).append(y).toString().hashCode();
	}
}
