package library.daos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public class LoanMapDAO implements ILoanDAO {
    private int nextID;
    private Map<Integer, ILoan> loanMap;
    private ILoanHelper helper;
    private Calendar cal;

    public LoanMapDAO(ILoanHelper helper) {
        if(helper == null) {
            throw new IllegalArgumentException(String.format("LoanMapDAO : constructor : helper cannot be null.", new Object[0]));
        } else {
            this.nextID = 0;
            this.helper = helper;
            this.loanMap = new HashMap();
            this.cal = Calendar.getInstance();
        }
    }

    public LoanMapDAO(ILoanHelper helper, Map<Integer, ILoan> loanMap) {
        this(helper);
        if(loanMap == null) {
            throw new IllegalArgumentException(String.format("LoanMapDAO : constructor : loanMap cannot be null.", new Object[0]));
        } else {
            this.loanMap = loanMap;
        }
    }

    public ILoan getLoanByID(int id) {
        return this.loanMap.containsKey(Integer.valueOf(id))?(ILoan)this.loanMap.get(Integer.valueOf(id)):null;
    }

    public ILoan getLoanByBook(IBook book) {
        if(book == null) {
            throw new IllegalArgumentException(String.format("LoanMapDAO : getLoanByBook : book cannot be null.", new Object[0]));
        } else {
            Iterator var3 = this.loanMap.values().iterator();

            while(var3.hasNext()) {
                ILoan loan = (ILoan)var3.next();
                IBook tempBook = loan.getBook();
                if(book.equals(tempBook)) {
                    return loan;
                }
            }

            return null;
        }
    }

    public List<ILoan> listLoans() {
        ArrayList list = new ArrayList(this.loanMap.values());
        return Collections.unmodifiableList(list);
    }

    public List<ILoan> findLoansByBorrower(IMember borrower) {
        if(borrower == null) {
            throw new IllegalArgumentException(String.format("LoanMapDAO : findLoansByBorrower : borrower cannot be null.", new Object[0]));
        } else {
            ArrayList list = new ArrayList();
            Iterator var4 = this.loanMap.values().iterator();

            while(var4.hasNext()) {
                ILoan loan = (ILoan)var4.next();
                if(borrower.equals(loan.getBorrower())) {
                    list.add(loan);
                }
            }

            return Collections.unmodifiableList(list);
        }
    }

    public List<ILoan> findLoansByBookTitle(String title) {
        if(title != null && !title.isEmpty()) {
            ArrayList list = new ArrayList();
            Iterator var4 = this.loanMap.values().iterator();

            while(var4.hasNext()) {
                ILoan loan = (ILoan)var4.next();
                String tempTitle = loan.getBook().getTitle();
                if(title.equals(tempTitle)) {
                    list.add(loan);
                }
            }

            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(String.format("LoanMapDAO : findLoansByBookTitle : title cannot be null or blank.", new Object[0]));
        }
    }

    public void updateOverDueStatus(Date currentDate) {
        Iterator var3 = this.loanMap.values().iterator();

        while(var3.hasNext()) {
            ILoan loan = (ILoan)var3.next();
            loan.checkOverDue(currentDate);
        }

    }

    public List<ILoan> findOverDueLoans() {
        ArrayList list = new ArrayList();
        Iterator var3 = this.loanMap.values().iterator();

        while(var3.hasNext()) {
            ILoan loan = (ILoan)var3.next();
            if(loan.isOverDue()) {
                list.add(loan);
            }
        }

        return Collections.unmodifiableList(list);
    }

    private int getNextId() {
        return ++this.nextID;
    }

    public ILoan createLoan(IMember borrower, IBook book) {
        if(borrower != null && book != null) {
            Date borrowDate = new Date();
            this.cal.setTime(borrowDate);
            this.cal.add(5, 14);
            Date dueDate = this.cal.getTime();
            ILoan loan = this.helper.makeLoan(book, borrower, borrowDate, dueDate);
            return loan;
        } else {
            throw new IllegalArgumentException(String.format("LoanMapDAO : createLoan : borrower and book cannot be null.", new Object[0]));
        }
    }

    public void commitLoan(ILoan loan) {
        int id = this.getNextId();
        loan.commit(id);
        this.loanMap.put(Integer.valueOf(id), loan);
    }
}