
/**
  * Represents a cost object
  * 
  * @author Jennifer Lam jl3953
  */
public CostObject implements Comparable<CostObject>{

    private float p; //probability
    private double c; //cost
    private MySet left; //left branch
    private MySet right; //right branch

    public CostObject(float p, double c, MySet left, MySet right){
	this.p = p;
	this.c = c;
	this.left = left;
	this.right = right;
    }

    public float p(){
	return this.p;
    }

    public double c(){
	return this.c;
    }

    public setC(double cost){
	this.c = cost;
    }

    public setLeft(MySet l){
	this.left = l;
    }

    public setRight(MySet r){
	this.right = r;
    }

    public int compareTo(CostObject other){
	if (this.c < other.c())
	    return -1;
	else if (this.c == other.c())
	    return 0;
	else
	    return 1;
    }
}

