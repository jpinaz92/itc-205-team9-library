
package library;

import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.panels.borrow.ABorrowPanel;
import library.panels.borrow.ConfirmLoanPanel;
import library.panels.borrow.RestrictedPanel;
import library.panels.borrow.ScanningPanel;
import library.panels.borrow.SwipeCardPanel;

public class BorrowUC_UI extends JPanel implements IBorrowUI {
	private static final long serialVersionUID = 1L;
	private IBorrowUIListener listener;
	private EBorrowState state;
	private Map<EBorrowState, IBorrowUI> panels;

	public BorrowUC_UI(IBorrowUIListener listener) {
		this.listener = listener;
		this.panels = new HashMap();
		this.setLayout(new CardLayout());
		this.addPanel(new SwipeCardPanel(listener), EBorrowState.INITIALIZED);
		this.addPanel(new ScanningPanel(listener), EBorrowState.SCANNING_BOOKS);
		this.addPanel(new RestrictedPanel(listener), EBorrowState.BORROWING_RESTRICTED);
		this.addPanel(new ConfirmLoanPanel(listener), EBorrowState.CONFIRMING_LOANS);
	}

	private void addPanel(ABorrowPanel panel, EBorrowState state) {
		this.panels.put(state, panel);
		this.add(panel, state.toString());
	}

	public void setState(EBorrowState state) {
		CardLayout cl = (CardLayout)this.getLayout();
		switch(SWITCH_TABLE.library.interfaces.EBorrowState()[state.ordinal()]) {
			case 2:
				cl.show(this, state.toString());
				break;
			case 3:
				cl.show(this, state.toString());
				break;
			case 4:
				cl.show(this, state.toString());
			case 5:
			case 7:
				break;
			case 6:
				cl.show(this, state.toString());
				break;
			default:
				throw new RuntimeException("Unknown state");
		}

		this.state = state;
	}

	public void displayMemberDetails(int memberID, String memberName, String memberPhone) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayMemberDetails(memberID, memberName, memberPhone);
	}

	public void displayOverDueMessage() {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayOverDueMessage();
	}

	public void displayAtLoanLimitMessage() {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayAtLoanLimitMessage();
	}

	public void displayOutstandingFineMessage(float amountOwing) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayOutstandingFineMessage(amountOwing);
	}

	public void displayOverFineLimitMessage(float amountOwing) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayOverFineLimitMessage(amountOwing);
	}

	public void displayExistingLoan(String loanDetails) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayExistingLoan(loanDetails);
	}

	public void displayScannedBookDetails(String bookDetails) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayScannedBookDetails(bookDetails);
	}

	public void displayPendingLoan(String loanDetails) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayPendingLoan(loanDetails);
	}

	public void displayConfirmingLoan(String loanDetails) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayConfirmingLoan(loanDetails);
	}

	public void displayErrorMessage(String errorMesg) {
		IBorrowUI ui = (IBorrowUI)this.panels.get(this.state);
		ui.displayErrorMessage(errorMesg);
	}
}
