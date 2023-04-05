package com.example.Employees.Exceptions;

public class EmptyInputException extends RuntimeException{
	public EmptyInputException() {
		super("No Data Available");
	}

}