
package library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import library.BorrowUC_UI;
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

	public BorrowUC_CTL(ICardReader reader, IScanner scanner, IPrinter printer, IDisplay display, IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO) {
		this.bookDAO = bookDAO;
		this.memberDAO = memberDAO;
		this.loanDAO = loanDAO;
		this.ui = new BorrowUC_UI(this);
		this.reader = reader;
		reader.addListener(this);
		this.scanner = scanner;
		scanner.addListener(this);
		this.printer = printer;
		this.display = display;
		this.state = EBorrowState.CREATED;
	}

	public void initialise() {
		this.previous = this.display.getDisplay();
		this.display.setDisplay((JPanel)this.ui, "Borrow UI");
		this.setState(EBorrowState.INITIALIZED);
	}

	public void close() {
		this.display.setDisplay(this.previous, "Main Menu");
	}

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
		System.out.println("Setting state: " + state);
		this.state = state;
		this.ui.setState(state);
		switch(SWITCH_TABLE.library.interfaces.EBorrowState()[state.ordinal()]) {
			case 2:
				this.reader.setEnabled(true);
				this.scanner.setEnabled(false);
				break;
			case 3:
				this.reader.setEnabled(false);
				this.scanner.setEnabled(true);
				this.bookList = new ArrayList();
				this.loanList = new ArrayList();
				this.scanCount = this.borrower.getLoans().size();
				this.ui.displayScannedBookDetails("");
				this.ui.displayPendingLoan("");
				break;
			case 4:
				this.reader.setEnabled(false);
				this.scanner.setEnabled(false);
				this.ui.displayConfirmingLoan(this.buildLoanListDisplay(this.loanList));
				break;
			case 5:
				this.reader.setEnabled(false);
				this.scanner.setEnabled(false);
				Iterator var3 = this.loanList.iterator();

				while(var3.hasNext()) {
					ILoan loan = (ILoan)var3.next();
					this.loanDAO.commitLoan(loan);
				}

				this.printer.print(this.buildLoanListDisplay(this.loanList));
				this.close();
				break;
			case 6:
				this.reader.setEnabled(false);
				this.scanner.setEnabled(false);
				this.ui.displayErrorMessage(String.format("Member %d cannot borrow at this time.", new Object[]{Integer.valueOf(this.borrower.getID())}));
				break;
			case 7:
				this.reader.setEnabled(false);
				this.scanner.setEnabled(false);
				this.close();
				break;
			default:
				throw new RuntimeException("Unknown state");
		}

	}

	public void cancelled() {
		this.setState(EBorrowState.CANCELLED);
	}

	public void scansCompleted() {
		this.setState(EBorrowState.CONFIRMING_LOANS);
	}

	public void loansConfirmed() {
		this.setState(EBorrowState.COMPLETED);
	}

	public void loansRejected() {
		System.out.println("Loans Rejected");
		this.setState(EBorrowState.SCANNING_BOOKS);
	}

	private String buildLoanListDisplay(List<ILoan> loans) {
		StringBuilder bld = new StringBuilder();

		ILoan ln;
		for(Iterator var4 = loans.iterator(); var4.hasNext(); bld.append(ln.toString())) {
			ln = (ILoan)var4.next();
			if(bld.length() > 0) {
				bld.append("\n\n");
			}
		}

		return bld.toString();
	}
}
