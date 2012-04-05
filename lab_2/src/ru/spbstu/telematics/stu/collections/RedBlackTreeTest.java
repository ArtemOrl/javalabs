package ru.spbstu.telematics.stu.collections;

import java.util.*;

/**
 * Класс для проведения теста реализованной структуры данных.
 * Запускает в цикле вставку элемента, проверку на содержание элемента в дереве
 * и удаление элемента из дерева произвольное количество раз.
 * @version 1.0
 * @author simonenko
 */
public class RedBlackTreeTest {

	/**
	 * Максимальное число генерирумое для вставки, поиска и удаления 
	 * элемента дерева.
	 */
	private final static int MAX_NUMBER = 100;
	/**
	 * Количество тестовых циклов.
	 */
	private final static int TESTS_AMONT = 5;
	/**
	 * Максимольное количество элементов, генерируемое для вставки,
	 * поиска и удаления элемента дерева.
	 */
	private final static int MAX_ELEMENTS = 20;
	private static Random rand = new Random();
	
	public static void main(String[] args) {
		
		IRedBlackTree rbt;
		int testInsertElementsAmount;
		int testContainElementsAmount;
		int testDeleteElementsAmount;
		boolean isRepeat = false;
		int num;
		
		for(int i = 0; i < TESTS_AMONT; i++) {
			
			try
			{
			
				System.out.println("\n\nStart test #" + i);
				rbt = new RedBlackTree();
				isRepeat = rand.nextBoolean();
				
				testInsertElementsAmount = rand.nextInt(MAX_ELEMENTS - 1) + 1;
				testContainElementsAmount  = rand.nextInt(MAX_ELEMENTS / 2) + 1;
				testDeleteElementsAmount = rand.nextInt(MAX_ELEMENTS - 1) + 1;
				
				System.out.println(" Insertion [" + testInsertElementsAmount + "]:");
				for(int j = 0; j < testInsertElementsAmount; j++) {
					num = rand.nextInt(MAX_NUMBER);
					System.out.println("  Insert " + num + " into tree");
					rbt.add(new Integer(num));
				}
				System.out.println("Tree:");
				RedBlackTree.printTree((RedBlackTree)rbt);
				
				System.out.println(" Contains [" + testContainElementsAmount + "]:");			
				for(int j = 0; j < testContainElementsAmount; j++) {
					num = rand.nextInt(MAX_NUMBER);
					System.out.println("  Is " + num + " contain in tree? It's " + rbt.contains(new Integer(num)));
				}
				
				System.out.println(" Deletion [" + testDeleteElementsAmount + "]:");
				for(int j = 0; j < testDeleteElementsAmount; j++) {
					num = rand.nextInt(MAX_NUMBER);
					System.out.println("  Is " + num + " delete from tree? It's " + rbt.remove(new Integer(num)));
				}
				System.out.println("Tree:");
				RedBlackTree.printTree((RedBlackTree)rbt);
			}
			catch(Exception e)
			{
				System.out.println("Got error: " + e.getMessage());
			}
		}
	}
}
