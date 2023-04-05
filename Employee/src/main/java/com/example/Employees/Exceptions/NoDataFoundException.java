package com.example.Employees.Exceptions;

public class NoDataFoundException extends RuntimeException{
	public NoDataFoundException() {
		super("No Data Available");
	}

}
