
package library.interfaces.daos;

import java.util.Date;
import java.util.List;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public interface ILoanDAO {
	ILoan createLoan(IMember var1, IBook var2);

	void commitLoan(ILoan var1);

	ILoan getLoanByID(int var1);

	ILoan getLoanByBook(IBook var1);

	List<ILoan> listLoans();

	List<ILoan> findLoansByBorrower(IMember var1);

	List<ILoan> findLoansByBookTitle(String var1);

	void updateOverDueStatus(Date var1);

	List<ILoan> findOverDueLoans();
}

