// --== CS400 File Header Information ==--
// Name: Allistair Nathan Mascarenhas
// Email: anmascarenha@wisc.edu
// Team: DC
// Role: Back End Developer 2
// TA: Yelun Bao
// Lecturer: Gary Dahl
// Notes to Grader: File was developed for project 1

import java.util.NoSuchElementException;

public interface MapADT<KeyType,ValueType> {
    public boolean put(KeyType key, ValueType value);
    public ValueType get(KeyType key) throws NoSuchElementException;
    public int size();
    public boolean containsKey(KeyType key);
    public ValueType remove(KeyType key);
    public void clear();
}
