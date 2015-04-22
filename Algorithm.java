import java.util.HashMap;

public class Algorithm {

    //TODO  CostObject
    private HashMap<MySet, CostObject> A; //array indexed by subset

    public Algorithm(){
	this.A = initialize();
    }
    public void Algorithm (){
	//Initialize all CostObjects considering plans without branching and
	NoBranchingAndInitialize(this.A);

	//Consider branching plans
	Iterator it = this.A.entrySet.iterator();
	while (it.hasNext()){
	    //current s
	    Map.Entry entry = (Map.Entry) it.next();
	    MySet s = entry.getKey();

	    //current sprime
	    Iterator it2 = this.A.entrySet.iterator();
	    while (it2.hasNext()){
		Map.Entry entry2 = (Map.Entry) it2.next();
		MySet sprime = entry2.getKey();

		//check that intersection is null
		if (sprime.intersection(s) != null){
		    continue;
		}
		CostObject currentCostObject = entry2.getValue(); //cost associated with sprime

		//sPrime is the right child of && plan being considered
		CMetric cmetric = new CMetric(sPrime);
		Dmetric dmetric = new Dmetric(sPrime);
		if (cmetric.leftmostTermDominates()){
		    //do nothing--suboptimal by lemma 4.8
		} else if (currentCostObject.p() <= 0.5 && dmetric.otherTermDominates()){
		    //do nothing--suboptimal by lemma 4.9
		} else {
		    CostObject c = computeCost(sprime, s);
		    MySet key = sprime.union(s);
		    CostObject value = this.A.get(key);

		    if (c < value){
			value.setC(c.c());
			value.setLeft(sprime);
			value.setRight(s);
		    }
		}
	    }
	}
    }


    public void NoBranchingAndInitialize(){
	/**
	 * Iterating through all entries of the hash map computing the CostObject for each
	 * plan.
	 */
	Iterator it = this.A.entrySet.iterator();
	while(it.hasNext()){
	    Map.Entry entry = (Map.Entry)it.next();
	    MySet key = entry.getKey();
	    CostObject logicalAndCostObject = computeCostObject(A, "l"); //compute CostObject for logical-and
	    //TODO remember to set b or no b
	    CostObject noBranchCostObject = computeCostObject(A, "n"); //compute CostObject for no-branch
	    if (noBranchCostObject < logicalAndCostObject){
		this.A.put(key, noBranchCostObject);
	    } else {
		this.A.put(key, logicalAndCostObject);
	    }
	}
    }

    //TODO define this method more thoroughly
    private HashMap<MySet, CostObject> initialize(){
	HashMap<MySet, CostObject> result = new HashMap<MySet, CostObject>();
	return result;
    }
}

