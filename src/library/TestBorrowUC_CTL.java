package library;


import javax.swing.*;
import java.util.List;

import library.BorrowUC_UI;
import library.panels.borrow.ABorrowPanel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import library.BorrowUC_CTL;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;




public class TestBorrowUC_CTL {
    private ICardReader reader;
    private IScanner scanner;
    private IPrinter printer;
    private IDisplay display;
    private int scanCount = 0;
    private BorrowUC_UI ui;
    private EBorrowState state;
    private IBookDAO bookDAO;
    private IMemberDAO memberDAO;
    private ILoanDAO loanDAO;
    private List<IBook> bookList;
    private List<ILoan> loanList;
    private IMember borrower;
    private JPanel previous;

    private BorrowUC_CTL ctl;


    @Before
    public void setUp() throws Exception {

        reader    = mock(library.interfaces.hardware.ICardReader.class);
        scanner   = mock(library.interfaces.hardware.IScanner.class);
        printer   = mock(library.interfaces.hardware.IPrinter.class);
        display   = mock(library.interfaces.hardware.IDisplay.class);
        bookDAO   = mock(library.interfaces.daos.IBookDAO.class);
        memberDAO = mock(library.interfaces.daos.IMemberDAO.class);
        loanDAO   = mock(library.interfaces.daos.ILoanDAO.class);
        ui = mock(library.BorrowUC_UI.class);


        ctl = new BorrowUC_CTL(reader, scanner, printer, display, bookDAO, loanDAO, memberDAO, ui);


    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void MemberIDInvalid() throws Exception {
        ctl.initialise();
        ctl.cardSwiped(96);
        verify(ui).displayErrorMessage("Member ID 96 not found");
    }

    @Test
    public void MemberIDValid() throws Exception {

        when(memberDAO.getMemberByID(1)).thenReturn(mock(IMember.class));

        ctl.initialise();
        ctl.cardSwiped(1);

        verify(ui, never()).displayErrorMessage(anyString());

    }


}