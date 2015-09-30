
package library.interfaces.entities;

import java.util.List;
import library.interfaces.entities.EMemberState;
import library.interfaces.entities.ILoan;

public interface IMember {
	int LOAN_LIMIT = 5;
	float FINE_LIMIT = 10.0F;

	boolean hasOverDueLoans();

	boolean hasReachedLoanLimit();

	boolean hasFinesPayable();

	boolean hasReachedFineLimit();

	float getFineAmount();

	void addFine(float var1);

	void payFine(float var1);

	void addLoan(ILoan var1);

	List<ILoan> getLoans();

	void removeLoan(ILoan var1);

	EMemberState getState();

	String getFirstName();

	String getLastName();

	String getContactPhone();

	String getEmailAddress();

	int getID();
}
