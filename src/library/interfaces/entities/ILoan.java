package library.interfaces.entities;

import java.util.Date;
import library.interfaces.entities.IBook;
import library.interfaces.entities.IMember;

public interface ILoan {
	int LOAN_PERIOD = 14;

	void commit(int var1);

	void complete();

	boolean isOverDue();

	boolean checkOverDue(Date var1);

	IMember getBorrower();

	IBook getBook();

	int getID();
}
