public class TrieSTNew<Value>
{
    private static final int R = 256;        // extended ASCII

    private Node root;

    private static class Node {
        private Object val;
        private Node [] next = new Node[R];
        private int degree;  // how many children?
    }

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }

    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

   /****************************************************
    * Insert key-value pair into the symbol table.
    ****************************************************/
    // The first argument (String key) is the means by which to search for
    // the second argument (arbitrary type Value). After this method succeeds
    // the key can and value can be accessed using the get() and 
    // searchPrefix() methods. In particular, note the searchPrefix() 
    // method.
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        if (x.next[c] == null)
        	x.degree++;
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

	// This method will return one of four possible values:
	// 0 if the key is neither found in the TrieST nor a prefix to
	//      a key in the TrieST
	// 1 if the key is a prefix to some string in the TrieST but it
	//      is not itself in the TrieST
	// 2 if the key is found in the TrieST but it is not a prefix to
	//      a longer string within the TrieST
	// 3 if the key is found in the TrieST and is also a prefix to added
	//      longer string within the TrieST
	// This method will be key in pruning your execution tree for your
	// anagram problem.  If you are interested in how the method works
	// you may read the additional comments below, but the different
	// return values are the important part of this method.
	public int searchPrefix(String key)
	{
		int ans = 0;
		Node curr = root;  // Start at the root Node
		boolean done = false;
		int loc = 0;       // index within the key -- start at 0
		
		// Loop until we get to the "end" loc or until we get to a
		// null pointer
		while (curr != null && !done)
		{
			// A key would be found in the node AFTER the last
			// pointer corresponding to a character in the key.  This
			// would be at the loc value == length of the key
			if (loc == key.length())
			{
				if (curr.val != null) // if Node has a value then 
				{					  // the key is a word in the 
									  // TrieST
					ans += 2;
				}
				if (curr.degree > 0) // if Node has at least one child
				{					 // then the key is a prefix to
				                     // some longer key in the TrieST
					ans += 1;
				}
				done = true;
			}
			else  // Move down to the next node using the current
			{	  // character in the key
				curr = curr.next[key.charAt(loc)];
				loc++;
			}
		}
		return ans;
	}
}