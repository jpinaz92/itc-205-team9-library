
package library.interfaces.entities;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.ILoan;

public interface IBook {
	void borrow(ILoan var1);

	ILoan getLoan();

	void returnBook(boolean var1);

	void lose();

	void repair();

	void dispose();

	EBookState getState();

	String getAuthor();

	String getTitle();

	String getCallNumber();

	int getID();
}
