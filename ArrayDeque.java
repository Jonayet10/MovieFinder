package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private static final int DEF_CAPACITY = 10;
    private static final int FACTOR = 2;
    private E[] datadeq;
    private int size;
    public ArrayDeque(int initialCapacity){
        this.datadeq = (E[])new Object[initialCapacity];
        this.size = 0;
    }
    public ArrayDeque(){
        this(DEF_CAPACITY);
    }

    @Override
    public void addFront(E e) {
        int newsize = this.size + 1;
        if(newsize <= this.datadeq.length){
            E[] newdata = (E[])new Object[this.datadeq.length];
            newdata[0] = e;
            for(int i = 1; i < newsize; i++){
                newdata[i] = this.datadeq[i-1];
            }
            this.datadeq = newdata;
            this.size++;
        }
        else if(newsize > this.datadeq.length){
            E[] newdata = (E[])new Object[this.datadeq.length*2];
            newdata[0] = e;
            for(int i = 1; i < newsize; i++){
                newdata[i] = this.datadeq[i-1];
            }
            this.datadeq = newdata;
            this.size++;
        }
    }

    @Override
    public void addBack(E e) {
        int newsize = this.size + 1;
        if(newsize < this.datadeq.length){
            this.datadeq[this.size] = e;
            this.size++;
        }
        else if(newsize >= this.datadeq.length){
            E[] newdata = (E[])new Object[this.datadeq.length*FACTOR];
            for(int i = 0; i < newsize-1; i++){
                newdata[i] = this.datadeq[i];
            }
            newdata[this.size] = e;
            this.datadeq = newdata;
            this.size++;
        }

    }

    @Override
    public E removeFront() {
        E val = null;
        if(this.size == 0){
            return null;
        }
        else{
            E[] newdata = (E[])new Object[this.datadeq.length];
            val = this.datadeq[0];
            for(int i = 1; i < this.size; i++){
                newdata[i-1] = this.datadeq[i];
            }
            this.datadeq = newdata;
            this.size--;
        }
        return val;
    }

    @Override
    public E removeBack() {
        E val = null;
        if(this.size == 0){
            return null;
        }
        else{
            val = this.datadeq[this.size - 1];
            this.datadeq[this.size - 1] = null;
            this.size--;
        }
        return val;
    }

    @Override
    public boolean enqueue(E e) {
        this.addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        E val = this.removeBack();
        return val;
    }

    @Override
    public boolean push(E e) {
        this.addBack(e);
        return true;
    }

    @Override
    public E pop() {
        E val = this.removeBack();
        return val;
    }

    @Override
    public E peekFront() {
        if(this.size == 0){
            return null;
        }
        return this.datadeq[0];
    }

    @Override
    public E peekBack() {
        if(this.size == 0){
            return null;
        }
        return this.datadeq[this.size - 1];
    }

    @Override
    public E peek() {
        if(this.size == 0){
            return null;
        }
        return this.datadeq[this.size - 1];
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public int size() {
        return this.size;
    }
    public String toString(){
        if(this.size() == 0){
            return "[]";
        }
        String res = "[";
        for(int i = 0; i < this.size; i++){
            if(i == this.size - 1){
                res += this.datadeq[i] + "";
            }
            else{
                res += this.datadeq[i] + ", ";
            }
        }
        return res += "]";
    }
    private class ArrayDequeIterator implements Iterator<E>{
        private int ind;

        public ArrayDequeIterator(){
            this.ind = 0;
        }
        public boolean hasNext(){
            if(this.ind < ArrayDeque.this.size()){
                return true;
            }
            return false;
        }
        public E next(){
            E val = ArrayDeque.this.datadeq[this.ind];
            this.ind++;
            return val;
        }
    }
}

