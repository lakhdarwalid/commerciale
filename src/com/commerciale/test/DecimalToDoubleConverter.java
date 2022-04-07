package com.commerciale.test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Component;
import java.text.ParseException;

import javax.swing.JTextField;

import org.junit.jupiter.api.Test;

import com.commerciale.service.Tools;
import com.commerciale.view.CreateInvoice;

class DecimalToDoubleConverter {

	@Test
	void test() throws Throwable {
		Tools tools = new Tools();
		double output = tools.decimalToDouble("12367,89");
		assertEquals(12367.89 , output);
	}
	@Test
	void test1() throws Throwable {
		Number output = Tools.convertToNumber("12367,89");
		assertEquals(12367.89 , output);
	}
	
	@Test
	void test2() {
		double output = Tools.CurrencyConverter(2, 34);
		assertEquals(68 , output);
	}
	
    @Test 
    void test3() {
    	boolean empty = false;
    	CreateInvoice ci =  CreateInvoice.getInstance();
    	for (Component comp : ci.getContainer().getComponents()) {
			if (comp instanceof JTextField) {
				if (((JTextField) comp).getText().isEmpty()) {
					empty = true;
				}
			}
		}
    	assertEquals(true, empty);
    }
}
