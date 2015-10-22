package library.entities;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

import library.entities.Loan;

import java.util.Date;
import java.util.Calendar;


public class TestLoan {
	
	IBook _book;
	IMember _member;
	ILoan _loan;
	Date _borrowDate, _dueDate;
	Calendar _cal;

	@Before
	public void setUp() throws Exception {
		_book = mock(IBook.class);
		_member = mock(IMember.class);
		
		_cal = Calendar.getInstance();
		_borrowDate = _cal.getTime();
		_cal.add(Calendar.DATE, ILoan.LOAN_PERIOD);
		_dueDate = _cal.getTime();

		_loan = new Loan( _book, _member, _borrowDate, _dueDate);

	}

	@After
	public void tearDown() throws Exception {
		_loan = null;
	}
	
	@Test
	public void testCreate() {
		ILoan loan = new Loan( _book, _member, _borrowDate, _dueDate);
		assertTrue( loan instanceof Loan);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateBadParamBookNull() {
		ILoan loan = new Loan( null, _member, _borrowDate, _dueDate);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateBadParamMemberNull() {
		ILoan loan = new Loan( _book, null, _borrowDate, _dueDate);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateBadParamBorrowDateNull() {
		ILoan loan = new Loan( _book, _member, null, _dueDate);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateBadParamDueDateNull() {
		ILoan loan = new Loan( _book, _member, _borrowDate, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateBadParamDueDateLessThanBorrowDate() {
		ILoan loan = new Loan( _book, _member, _dueDate, _borrowDate);
	}
	
	@Test
	public void testCreateBadParamDueDateEqualToBorrowDate() {
		ILoan loan = new Loan( _book, _member, _borrowDate, _borrowDate);
		assertTrue( loan instanceof Loan);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCommitBadParamLoanIdNegative() {
		_loan.commit(-1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCommitBadParamLoanIdZero() {

		_loan.commit(-1);
	}
	
	@Test
	public void testIsCurrentPending() {

		assertFalse(_loan.isCurrent());
	}
	
	@Test
	public void testCommit() {
		//setup
		int id = 1;
		
		//execute
		_loan.commit(id);
		
		//verifies and asserts
		verify(_book).borrow(_loan);
		verify(_member).addLoan(_loan);
		
		assertTrue(_loan.isCurrent());
		int actual = _loan.getID();
		assertEquals(id, actual);
	}
	
	@Test(expected=RuntimeException.class)
	public void testCommitWhenCurrent() {
		_loan.commit(1);
		_loan.commit(1);
	}
	
	
	

	@Test
	public void testComplete() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsCurrent() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsOverDue() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckOverDue() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBorrower() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBook() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetID() {
		fail("Not yet implemented");
	}

}
