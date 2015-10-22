package library.daos;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import library.daos.LoanMapDAO;

import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;

import library.interfaces.entities.ILoan;
import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class TestLoanDAO {

	ILoanHelper _helper;
	ILoanDAO _dao;
	

	@Before
	public void setUp() throws Exception {
		_helper = mock(ILoanHelper.class);
		_dao = new LoanMapDAO(_helper);
	}

	@After
	public void tearDown() throws Exception {
		_helper = null;
		_dao = null;
	}

	@Test
	public void testConstructor() {
		LoanMapDAO dao = new LoanMapDAO(_helper);
		assertTrue(dao instanceof ILoanDAO);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorHelperNull() {
		LoanMapDAO dao = new LoanMapDAO(null);
	}
	
	@Test
	public void testCreateLoan() {
		
		//setup
		IMember member = mock(IMember.class);
		IBook book = mock(IBook.class);
		ILoan expectedLoan = mock(ILoan.class);
		
		when(_helper.makeLoan(eq(book), eq(member), any(Date.class), any(Date.class))).thenReturn(expectedLoan);
		
		//execute
		ILoan actualLoan = _dao.createLoan(member, book);
		
		//verifies and asserts
		verify(_helper).makeLoan(eq(book), eq(member), any(Date.class), any(Date.class));
		assertEquals(expectedLoan, actualLoan);		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateLoanBookNull() {
		
		//setup
		IMember member = mock(IMember.class);
		
		//execute
		ILoan actualLoan = _dao.createLoan(member, null);
		
		//verifies and asserts
		fail("Should have thrown exception");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateLoanMemberNull() {
		
		//setup
		IMember member = mock(IMember.class);
		IBook book = mock(IBook.class);		
		
		//execute
		ILoan actualLoan = _dao.createLoan(null, book);
		
		//verifies and asserts
		fail("Should have thrown exception");
	}
	
	@Test
	public void testCommit() {
		//setup
		ILoan loan = mock(ILoan.class);
		
		//execute
		_dao.commitLoan(loan);
		
		//verifies and asserts
		verify(loan).commit(1);
		ILoan actual = _dao.getLoanByID(1);		
		assertEquals(loan, actual);		
	}
	
	@Test
	public void testCommit2() {
		//setup
		ILoan loan = mock(ILoan.class);
		//Map<Integer, ILoan> spyMap = spy(new HashMap<Integer,ILoan>());
		Map<Integer, ILoan> mockMap = mock(Map.class);
		_dao = new LoanMapDAO(_helper, mockMap);
		when(mockMap.get(Integer.valueOf(1))).thenReturn(loan);
		
		//execute
		_dao.commitLoan(loan);
		
		//verifies and asserts
		verify(loan).commit(1);
		verify(mockMap).put(1, loan);
		
		ILoan actual = _dao.getLoanByID(1);
		
		verify(mockMap).get(Integer.valueOf(1));
		
		assertEquals(loan,actual);
		
	}
	
	
	
}
