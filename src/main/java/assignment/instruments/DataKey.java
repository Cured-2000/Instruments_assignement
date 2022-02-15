package assignment.instruments;

public class DataKey {
	private String instrumentName;
	private int instrumentType;

	// default constructor
	public DataKey() {
		this(null, 0);
	}
        
	public DataKey(String name, int size) {
		instrumentName = name;
		instrumentType = size;
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public int getInstrumentType() {
		return instrumentType;
	}

	/**
	 * Returns 0 if this DataKey is equal to k, returns -1 if this DataKey is smaller
	 * than k, and it returns 1 otherwise. 
	 */
	public int compareTo(DataKey k) {
            if (this.getInstrumentType() == k.getInstrumentType()) {
                int compare = this.instrumentName.compareTo(k.getInstrumentName());
                if (compare == 0){
                     return 0;
                } 
                else if (compare < 0) {
                    return -1;
                }
            }
            else if(this.getInstrumentType() < k.getInstrumentType()){
                    return -1;
            }
            return 1;
            
	}
}
