package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private Node<E> first;
    private int size;
    private Node<E> last;
    private static class Node<E>{
        private Node<E> prev;
        private final E data;
        private Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next){
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
        public Node(Node<E> prev, E data){
            this(prev, data, null);
        }
        public Node(E data, Node<E> next){
            this(null, data, next);
        }
        public Node(E data){
            this(null, data, null);
        }
    }
    public LinkedDeque(){
        this.first = null;
        this.size = 0;
        this.last = null;
    }

    @Override
    public void addFront(E e) {
        if (this.size < 1){
            this.first = new Node<E>(e);
            this.last = this.first;
            this.size++;
        }
        else {
            if (this.first.prev == null){
                this.first.prev = new Node<E>(e, this.first);
                this.first = this.first.prev;
                this.size++;
            }
        }
    }

    @Override
    public void addBack(E e) {
        if (this.size < 1){
            this.last = new Node<E>(e);
            this.first = this.last;
            this.size++;
        }
        else {
            if (this.last.next == null){
                this.last.next = new Node<E>(this.last, e);
                this.last = this.last.next;
                this.size++;

            }
        }

    }

    @Override
    public E removeFront() {
        if (this.size == 0){
            return null;
        }
        E oldFront = this.first.data;
        if (this.size == 1){
            this.first = null;
            this.last = null;
            this.size--;
            return oldFront;
        }
        else {
            this.first = this.first.next;
            this.first.prev = null;
            this.size--;
        }
        return oldFront;
    }

    @Override
    public E removeBack() {
        if(this.size == 0){
            return null;
        }
        E oldBack = this.last.data;
        if(this.size == 1){
            this.first = null;
            this.last = null;
            this.size--;
            return oldBack;
        }
        else{
            this.last = this.last.prev;
            this.last.next = null;
            this.size--;
        }
        return oldBack;
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
        return this.first.data;
    }

    @Override
    public E peekBack() {
        if(this.size == 0){
            return null;
        }
        return this.last.data;
    }

    @Override
    public E peek() {
        if(this.size == 0){
            return null;
        }
        return this.last.data;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
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
        Node<E> curr = this.first;
        while(curr != null){
            res += curr.data + ", ";
            curr = curr.next;
        }
        res = res.substring(0, res.length() - 2);
        return res += "]";

    }

    private class LinkedDequeIterator implements Iterator<E>{
        private Node<E> curr;

        public LinkedDequeIterator(){
            this.curr = LinkedDeque.this.first;
        }

        @Override
        public boolean hasNext() {
            if(this.curr != null){
                return true;
            }
            return false;
        }
        public E next(){
            E val = this.curr.data;
            this.curr = this.curr.next;
            return val;
        }
    }
}
