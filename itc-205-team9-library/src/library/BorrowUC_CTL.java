package library;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

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

public class BorrowUC_CTL implements ICardReaderListener, IScannerListener, IBorrowUIListener {
	private ICardReader reader;
	private IScanner scanner;
	private IPrinter printer;
	private IDisplay display;
	private int scanCount = 0;
	private IBorrowUI ui;
	private EBorrowState state;
	private IBookDAO bookDAO;
	private IMemberDAO memberDAO;
	private ILoanDAO loanDAO;
	private List<IBook> bookList;
	private List<ILoan> loanList;
	private IMember borrower;
	private JPanel previous;


	public BorrowUC_CTL(ICardReader reader, IScanner scanner,
						IPrinter printer, IDisplay display,
						IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO ) {

		this.display = display;
		this.ui = new BorrowUC_UI(this);
		state = EBorrowState.CREATED;
	}

	public void initialise() {
		previous = display.getDisplay();
		display.setDisplay((JPanel) ui, "Borrow UI");
	}

	public void close() {
		display.setDisplay(previous, "Main Menu");
	}

	//@Override
	//public void cardSwiped(int memberID) {
	//	throw new RuntimeException("Not implemented yet");
	//}



	@Override
	public void cardSwiped(int memberID) {
		System.out.println("cardSwiped: got " + memberID);
		if(!this.state.equals(EBorrowState.INITIALIZED)) {
			throw new RuntimeException(String.format("BorrowUC_CTL : cardSwiped : illegal operation in state: %s", new Object[]{this.state}));
		} else {
			this.borrower = this.memberDAO.getMemberByID(memberID);
			if(this.borrower == null) {
				this.ui.displayErrorMessage(String.format("Member ID %d not found", new Object[]{Integer.valueOf(memberID)}));
			} else {
				boolean overdue = this.borrower.hasOverDueLoans();
				boolean atLoanLimit = this.borrower.hasReachedLoanLimit();
				boolean hasFines = this.borrower.hasFinesPayable();
				boolean overFineLimit = this.borrower.hasReachedFineLimit();
				boolean borrowing_restricted = overdue || atLoanLimit || overFineLimit;
				if(borrowing_restricted) {
					this.setState(EBorrowState.BORROWING_RESTRICTED);
				} else {
					this.setState(EBorrowState.SCANNING_BOOKS);
				}

				int mID = this.borrower.getID();
				String mName = this.borrower.getFirstName() + " " + this.borrower.getLastName();
				String mContact = this.borrower.getContactPhone();
				this.ui.displayMemberDetails(mID, mName, mContact);
				float loanString;
				if(hasFines) {
					loanString = this.borrower.getFineAmount();
					this.ui.displayOutstandingFineMessage(loanString);
				}

				if(overdue) {
					this.ui.displayOverDueMessage();
				}

				if(atLoanLimit) {
					this.ui.displayAtLoanLimitMessage();
				}

				if(overFineLimit) {
					System.out.println("State: " + this.state);
					loanString = this.borrower.getFineAmount();
					this.ui.displayOverFineLimitMessage(loanString);
				}

				String loanString1 = this.buildLoanListDisplay(this.borrower.getLoans());
				this.ui.displayExistingLoan(loanString1);
			}
		}
	}

	public void bookScanned(int barcode) {
		System.out.println("bookScanned: got " + barcode);
		if(this.state != EBorrowState.SCANNING_BOOKS) {
			throw new RuntimeException(String.format("BorrowUC_CTL : bookScanned : illegal operation in state: %s", new Object[]{this.state}));
		} else {
			this.ui.displayErrorMessage("");
			IBook book = this.bookDAO.getBookByID(barcode);
			if(book == null) {
				this.ui.displayErrorMessage(String.format("Book %d not found", new Object[]{Integer.valueOf(barcode)}));
			} else if(book.getState() != EBookState.AVAILABLE) {
				this.ui.displayErrorMessage(String.format("Book %d is not available: %s", new Object[]{Integer.valueOf(book.getID()), book.getState()}));
			} else if(this.bookList.contains(book)) {
				this.ui.displayErrorMessage(String.format("Book %d already scanned: ", new Object[]{Integer.valueOf(book.getID())}));
			} else {
				++this.scanCount;
				this.bookList.add(book);
				ILoan loan = this.loanDAO.createLoan(this.borrower, book);
				this.loanList.add(loan);
				this.ui.displayScannedBookDetails(book.toString());
				this.ui.displayPendingLoan(this.buildLoanListDisplay(this.loanList));
				if(this.scanCount >= 5) {
					this.setState(EBorrowState.CONFIRMING_LOANS);
				}

			}
		}
	}


	private void setState(EBorrowState state) {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void cancelled() {
		close();
	}

	@Override
	public void scansCompleted() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansConfirmed() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansRejected() {
		throw new RuntimeException("Not implemented yet");
	}

	private String buildLoanListDisplay(List<ILoan> loans) {
		StringBuilder bld = new StringBuilder();
		for (ILoan ln : loans) {
			if (bld.length() > 0) bld.append("\n\n");
			bld.append(ln.toString());
		}
		return bld.toString();
	}

}

