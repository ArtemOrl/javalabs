package ru.spbstu.telematics.stu.collections;

/**
 * Коллекция хранит данные в структуре Red-black tree и гарантирует 
 * логарифмическое время вставки, удаления и поиска.
 */
public interface IRedBlackTree {
	
	/**
    * Добавить элемент в дерево
    * @param o
    */
    void add(Comparable o);
    
    /**
     * Удалить элемент из дерева
     * @param o
     */
    boolean remove(Comparable o);
    
    /**
     * Возвращает true, если элемент содержится в дереве
     * @param o
     */
    boolean contains(Comparable o);
}