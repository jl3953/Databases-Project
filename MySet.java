import java.util.ArrayList;

/**
  * A set of logical and terms
  *
  * @author Jennifer Lam jl3953
  */
public MySet{

    private ArrayList<AndTerm> set;

    public MySet(){
	this.set = new ArrayList<AndTerm>();
    }
    
    public MySet(ArrayList<AndTerm> set){
	this.set = set;
    }

    public void add(AndTerm andterm){
	this.set.add(andterm);
    }

    public boolean contains(AndTerm andterm){
	return this.set.contains(andterm);
    }

    public ArrayList<AndTerm> set(){
	return this.set;
    }

    public MySet union(MySet sprime) {
    	ArrayList<AndTerm> result = new ArrayList<AndTerm>(this.set);

	//check for duplicates in the current set with new set before adding term
	Iterator it = sprime.set().iterator();
	while(it.hasNext()){
	    AndTerm current = it.next();
	    if (!result.contains(current)){
		result.add(current);
	    }
	}
	
	return new MySet(result);
    }

    public MySet Intersection(MySet sprime){

	ArrayList<AndTerm> result = new ArrayList<AndTerm>();

	Iterator it = sprime.set().iterator();
	while(it.hasNext()){
	    AndTerm current = it.next();
	    if (this.set.contains(current)){
		result.add(current);
	    }
	}

	if (result.isEmpty())
	    return null;
	
	return new MySet(result);
    }
}
