
package library.interfaces;

import library.interfaces.EBorrowState;

public interface IBorrowUI {
	void setState(EBorrowState var1);

	void displayMemberDetails(int var1, String var2, String var3);

	void displayExistingLoan(String var1);

	void displayOverDueMessage();

	void displayAtLoanLimitMessage();

	void displayOutstandingFineMessage(float var1);

	void displayOverFineLimitMessage(float var1);

	void displayScannedBookDetails(String var1);

	void displayPendingLoan(String var1);

	void displayConfirmingLoan(String var1);

	void displayErrorMessage(String var1);
}
