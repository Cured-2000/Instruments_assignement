package assignment.instruments;

public class OrderedDictionary implements OrderedDictionaryADT {

    Node root;

    OrderedDictionary() {
        root = new Node();
    }

    /**
     * Returns the Record object with key k, or it returns null if such a record
     * is not in the dictionary.
     *
     * @param k
     * @return
     * @throws assignment/Instruments/DictionaryException.java
     */
    @Override
    public InstrumentRecord find(DataKey k) throws DictionaryException {
        Node current = root;
        int comparison;
        if (root.isEmpty()) {         
            throw new DictionaryException("There is no record matches the given key");
        }

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) { // key found
                return current.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    throw new DictionaryException("There is no record matches the given key");
                }
                current = current.getRightChild();
            }
        }

    }

    /**
     * Inserts r into the ordered dictionary. It throws a DictionaryException if
     * a record with the same key as r is already in the dictionary.
     *
     * @param r
     * @throws instruments.DictionaryException
     */
    @Override
    public void insert(InstrumentRecord r) throws DictionaryException {

        if(root.isEmpty()){
            //tree is empty insert new node
            root.setData(r);
            return;
        }
        Node current = root;
        Node ins =new Node(r);
        int comparison;
        while (current != null) {
            comparison = current.getData().getDataKey().compareTo(r.getDataKey());
            // current is null for some reason
            if (comparison == 0) { // key found
                throw new DictionaryException("same key found");
            }
            if (comparison == 1) {
                //less than
                if (current.getLeftChild() != null) {
                    current = current.getLeftChild();
                    // empty space not found keep traversing
                }
                else{
                    current.setLeftChild(ins);
                    // empty space found STOP and insert
                    break;
                }
            } else if (comparison == -1) {
                //greater than
                if (current.getRightChild() != null) {
                    current = current.getRightChild();
                    // empty space not found keep traversing
                }
                else{
                    current.setRightChild(ins);
                    // empty space found STOP and insert
                    break;
                }
            }
        }
        // Write this method
    }
    /**
     * Removes the record with Key k from the dictionary. It throws a
     * DictionaryException if the record is not in the dictionary.
     *
     * @param k
     * @throws instruments.DictionaryException
     */
    @Override
    public void remove(DataKey k) throws DictionaryException {
// Write this method
      if (root.isEmpty()) {
            throw new DictionaryException("There is no record matches the given key");
        }
        //check if node to delete is root
        //if so, set replacement as new root (unless root is last node to delete)
        boolean isRoot = root.getData().getDataKey().compareTo(k) == 0;

        Node current = root;
        Node parent = null;
        int comparison;

        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            //if k is less than current
            if (comparison == 1) {
                if (current.getLeftChild() != null) {
                    //set current node as parent and set current as next left child (move down one level)
                    parent = current;
                    current = current.getLeftChild();
                }
                else {
                    throw new DictionaryException("There is no record matches the given key");
                }
            }
            //if k is greater than current
            else if (comparison == -1) {
                if (current.getRightChild() != null) {
                    //set current node as parent and set current as next right child (move down one level)
                    parent = current;
                    current = current.getRightChild();
                }
                else {
                    throw new DictionaryException("There is no record matches the given key");
                }
            }
            else if (comparison == 0) {
                //if node has zero children
                //change node's parent to no longer have reference to the deleted child
                //delete node
                if (!current.hasRightChild() && !current.hasLeftChild()) {
                    //if current node is root, set it to null since it has no children
                    if (parent==null) {
                        root = new Node();
                    }
                    else if (parent.hasLeftChild() && parent.getLeftChild().getData().getDataKey().compareTo(current.getData().getDataKey()) == 0) {
                        parent.setLeftChild(null);
                    }
                    else if (parent.hasRightChild() && parent.getRightChild().getData().getDataKey().compareTo(current.getData().getDataKey()) == 0) {
                        parent.setRightChild(null);
                    }
                    break;
                }
                //if current node has only right child
                //set current node's parent to adopt node's right child (parent will adopt grandchild)
                //set right child's new parent to node's parent (grandparent becomes new parent)
                else if (!current.hasLeftChild()) {
                    if (parent==null) {
                        root = current.getRightChild();
                    }
                    else if (parent.hasLeftChild()&&parent.getLeftChild().getData().getDataKey().compareTo(current.getData().getDataKey()) == 0) {
                        parent.setLeftChild(current.getRightChild());
                    }
                    else {
                        parent.setRightChild(current.getRightChild());
                    }
                    current.getRightChild().setParent(parent);
                    break;
                }
                //if current node has only left child
                //set current node's parent to adopt node's left child (parent will adopt grandchild)
                //set right child's new parent to node's parent (grandparent becomes new parent)
                else if (!current.hasRightChild()) {
                    if (parent==null) {
                        root = current.getLeftChild();
                    }
                    else if (parent.hasLeftChild()&&parent.getLeftChild().getData().getDataKey().compareTo(current.getData().getDataKey()) == 0) {
                        parent.setLeftChild(current.getLeftChild());
                    }
                    else {
                        parent.setRightChild(current.getLeftChild());
                    }
                    current.getLeftChild().setParent(parent);
                    break;
                }
                //if node has two children
                //find smallest node in right subtree
                //change smallest's parent to no longer have reference to smallest as a child
                //set smallest's parent to current node's parent
                //set smallest's children to node's children (if smallest is current node's child, leave children alone)
                else {
                    InstrumentRecord y = successor(current.getData().getDataKey());
                    remove(y.getDataKey());
                    current.setData(y);
                    break;
                }

            }
        }
    }

        /**
         * Returns the successor of k (the record from the ordered dictionary with
         * smallest key larger than k); it returns null if the given key has no
         * successor. The given key DOES NOT need to be in the dictionary.
         *
         * @param k
         * @return
         * @throws instruments.DictionaryException
         */
    @Override
    public InstrumentRecord successor(DataKey k) throws DictionaryException{
        Node current = root;
        int comparison;

        // traverses tree for node with datakey
        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) {
                InstrumentRecord w = largest();
                if (current.getData().getDataKey().compareTo(w.getDataKey())==0) {
                    throw new DictionaryException("There is no predecessor");
                }
                //found node with data k
                if (current.hasRightChild()){
                    //step right and go left
                    current= current.getRightChild();
                    while(current.hasLeftChild()){
                        current = current.getLeftChild();
                    }
                    return current.getData();
                }
                //if node with k has no right child go up to parent node and find successor
                //if node has no successor return null
                Node par = current.getParent();
                while(par != null && current==par.getRightChild()) {
                current = par;
                par = par.getParent();
            }

            return par.getData();
        }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    return null;
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    return null;
                }
                current = current.getRightChild();

            }
        }
        // Write this method
    }

   
    /**
     * Returns the predecessor of k (the record from the ordered dictionary with
     * largest key smaller than k; it returns null if the given key has no
     * predecessor. The given key DOES NOT need to be in the dictionary.
     *
     * @param k
     * @return
     * @throws instruments.DictionaryException
     */
    @Override
    public InstrumentRecord predecessor(DataKey k) throws DictionaryException{
        Node current = root;
        int comparison;
        // traverses tree for node with datakey
        while (true) {
            comparison = current.getData().getDataKey().compareTo(k);
            if (comparison == 0) {
                InstrumentRecord w = smallest();
                if (current.getData().getDataKey().compareTo(w.getDataKey())==0) {
                    throw new DictionaryException("There is no predecessor");
                }

                //found node with data k
                if (current.hasLeftChild()){
                    //step left then recur right
                    current = current.getLeftChild();
                    while(current.hasRightChild()){
                        current = current.getRightChild();
                    }
                    return current.getData();
                }
                //if node with k has no left child go up to parent node and find successor
                //if node has no successor return null
                Node par = current.getParent();
                while(par != null && current==par.getLeftChild()) {
                    current = par;
                    par = par.getParent();
                }
                return par.getData();
            }
            if (comparison == 1) {
                if (current.getLeftChild() == null) {
                    // Key not found
                    return null;
                }
                current = current.getLeftChild();
            } else if (comparison == -1) {
                if (current.getRightChild() == null) {
                    // Key not found
                    return null;
                }
                current = current.getRightChild();
            }

        }
        // Write this method

    }

    /**
     * Returns the record with smallest key in the ordered dictionary. Returns
     * null if the dictionary is empty.
     *
     * @return
     */
    @Override
    public InstrumentRecord smallest() throws DictionaryException{
        if(root.isEmpty()){
            throw new DictionaryException("The dictionary is empty");
        }
        Node current = root;
        //recur left for smallest value
        while (current.hasLeftChild()) {
            current = current.getLeftChild();
        }
        return current.getData();
        // Write this method

    }

    /*
	 * Returns the record with largest key in the ordered dictionary. Returns
	 * null if the dictionary is empty.
     */
    @Override
    public InstrumentRecord largest() throws DictionaryException{
        if(root.isEmpty()){
            throw new DictionaryException("The dictionary is empty");
        }
        Node current = root;
        //recur right for largest value
        while (current.hasRightChild()) {
            current = current.getRightChild();
        }
        return current.getData();
        // Write this method

    }
      
    /* Returns true if the dictionary is empty, and true otherwise. */
    @Override
    public boolean isEmpty (){
        return root.isEmpty();
    }
}
