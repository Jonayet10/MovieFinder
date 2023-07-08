package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Iterator;

public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;

    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = null;
        this.collector = collector;
        this.size = 0;
    }

    private TrieNode<A, V> nextNode(TrieNode<A, V> curr, K key){
        return curr.pointers.get(key);
    }


    @Override
    public boolean isPrefix(K key) {
        if(key.toString() == ""){
            return true;
        }
        TrieNode<A, V> curr = this.root;
        return isPrefixHelper(curr, key);
    }

    private boolean isPrefixHelper(TrieNode<A, V> curr, K key){
        if(curr == null){
            return false;
        }
        for(A letter : key){
            if(curr.pointers.containsKey(letter)){
                curr = curr.pointers.get(letter);
            }
            else{
                return false;
            }
        }
        return true;
    }

    public ICollection<V> getCompletions(K prefix) {
        if (this.root == null) {
            this.root = new TrieNode<>();
        }
        TrieNode<A,V> curr = this.root;
        ArrayDeque<V> vals = new ArrayDeque<>();
        for (A letter : prefix) {
            if (curr.pointers.containsKey(letter)) {
                curr = curr.pointers.get(letter);
            }
            else {
                return vals;
            }
        }
        getCompletionsHelper(curr, vals);
        return vals;
    }

    private void getCompletionsHelper(TrieNode<A, V> curr, ArrayDeque<V> vals) {
        if (curr == null) {
            return;
        }
        if (curr.value != null) {
            vals.addBack(curr.value);
        }
        for (A letter : curr.pointers.keySet()) {
            getCompletionsHelper(curr.pointers.get(letter), vals);
        }
    }


    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public V get(K key) {
        TrieNode<A, V> curr = this.root;
        return getHelper(curr, key);
    }

    private V getHelper(TrieNode<A, V> curr, K key){
        if(curr == null){
            return null;
        }
        V value = curr.value;
        for(A letter : key){
            if(curr.pointers.containsKey(letter)){
                curr = curr.pointers.get(letter);
                value = curr.value;
            }
            else{
                return null;
            }
        }
        if(value != null){
            return value;
        }
        return null;

    }
    @Override
    public V remove(K key) {
        TrieNode<A,V> curr = this.root;
        Iterator<A> it = key.iterator();
        V val = this.get(key);
        if (removeHelper(curr,it)) {
            this.root = null;
        }
        return val;
    }

    private boolean removeHelper(TrieNode<A,V> curr, Iterator<A> it) {
        if (curr == null){
            return false;
        }
        if (it.hasNext() == false) {
            V daValue = curr.value;
            if (daValue != null) {
                curr.value = null;
                this.size--;
            }
        } else {
            A letter = it.next();
            TrieNode<A,V> child = curr.pointers.get(letter);
            if (removeHelper(child, it)) {
                curr.pointers.remove(letter);
            }
        }
        return curr.pointers.isEmpty() && curr.value == null;
    }


    @Override
    public V put(K key, V value) {
        if (this.root == null) {
            this.root = new TrieNode<A,V>();
        }
        TrieNode<A,V> curr = this.root;
        for (A letter : key) {
            if (curr.pointers.containsKey(letter)) {
                curr = curr.pointers.get(letter);
            } else {
                curr.pointers.put(letter, new TrieNode<A,V>());
                curr = curr.pointers.get(letter);
            }
        }
        V old = curr.value;
        curr.value = value;
        if (old == null){
            this.size++;
        }
        return old;
    }


    @Override
    public boolean containsKey(K key) {
        if(this.get(key) == null){
            return false;
        }
        return true;
    }


    @Override
    public boolean containsValue(V value) {
        ICollection<V> vals = values();
        return vals.contains(value);
    }


    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ArrayDeque<K> accumulator = new ArrayDeque<K>();
        IDeque<A> tempacc = new ArrayDeque<A>();
        keysHelper(this.root, accumulator, tempacc);
        return accumulator;
    }

    private ICollection<K> keysHelper(TrieNode<A, V> curr, ArrayDeque<K> accumulator, IDeque<A> tempacc){
        if (curr == null){
            return null;
        }
        if (curr.value != null){
            accumulator.addBack(this.collector.apply(tempacc));
        }
        if (curr.pointers == null){
            return null;
        }
        else{
            for (A letter : curr.pointers.keySet()){
                tempacc.addBack(letter);
                keysHelper(curr.pointers.get(letter), accumulator, tempacc);
                tempacc.removeBack();
            }
        }
        return null;
    }

    @Override
    public ICollection<V> values() {
        if (this.root == null) {
            this.root = new TrieNode<>();
        }
        ArrayDeque<V> accumulator = new ArrayDeque<>();
        valuesHelper(this.root, accumulator);
        return accumulator;
    }


    private void valuesHelper(TrieNode<A,V> curr, ArrayDeque<V> accumulator) {
        getCompletionsHelper(this.root, accumulator);
    }

    @Override
    public Iterator<K> iterator() {

        return keys().iterator();
    }



    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }
}

