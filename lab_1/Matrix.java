package javalabs.lab_1;

import java.io.*;
import java.util.*;


// Класс Matrix. 
// Реализует матрицу целых чисел.
class Matrix
{
	private int rows_;     // Количество строк матрицы.
	private int columns_;  // Количество столбцов матрицы.
	private int data_[][]; // Двумерный массив, описывающий содержимое матрицы.

	// Конструктор
	public Matrix(int rows, int columns)
	{
		rows_ = rows;
		columns_ = columns;
		data_ = new int [rows_][columns_];
		for(int i = 0; i < rows_; i++)
		{
			for(int j = 0; j < columns_; j++)
			{
				data_[i][j] = 0;
			}
		}
	}

	// Конструктор копий.
	public Matrix(Matrix matrix)
	{
		rows_ = matrix.rows_;
	        columns_ = matrix.columns_;
                data_ = new int [rows_][columns_];
                for(int i = 0; i < rows_; i++)
                {
                        for(int j = 0; j < columns_; j++)
                        {
                                data_[i][j] = matrix.data_[i][j];
                        }
                }

	}

	// Метод, позволяющий осуществить ввод элементов матрицы.
	public void input() throws IOException
	{
		Scanner s = new Scanner(System.in);
		System.out.println("Input matrix data (space as delimiter):");
		for(int i = 0; i < rows_; i++)
		{
			System.out.print("Input " + (i + 1) + " string of matrix: ");
			for(int j = 0; j < columns_; j++)
			{
				data_[i][j] = s.nextInt();
			}
		}
		System.out.print("\n");
	}

	// Метеод, позволяющий выводить матрицу на экран.
	public void print()
	{
		for(int i = 0; i < rows_; i++)
		{
			for(int j = 0; j < columns_; j++)
			{
				System.out.print(data_[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}

	// Метод, позволяющий транспонировать матрицу.
	public void transpose()
	{
		Matrix temp = new Matrix(this);

		rows_ = temp.columns_;
		columns_ = temp.rows_;
		data_ = new int [rows_][columns_];

		for(int i = 0; i < rows_; i++)
		{
			for(int j = 0; j < columns_; j++)
			{
				data_[i][j] = temp.data_[j][i];
			}
		}
	}
}
