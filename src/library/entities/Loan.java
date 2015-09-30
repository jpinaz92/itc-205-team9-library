
package library.entities;

import java.text.DateFormat;
import java.util.Date;
import library.interfaces.entities.ELoanState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public class Loan implements ILoan {
    private int id;
    private final IMember borrower;
    private final IBook book;
    private Date borrowDate;
    private Date dueDate;
    private ELoanState state;

    public Loan(IBook book, IMember borrower, Date borrowDate, Date returnDate) {
        if(!this.sane(book, borrower, borrowDate, returnDate)) {
            throw new IllegalArgumentException("Loan: constructor : bad parameters");
        } else {
            this.book = book;
            this.borrower = borrower;
            this.borrowDate = borrowDate;
            this.dueDate = returnDate;
            this.state = ELoanState.PENDING;
        }
    }

    private boolean sane(IBook book, IMember borrower, Date borrowDate, Date returnDate) {
        return book != null && borrower != null && borrowDate != null && returnDate != null && borrowDate.compareTo(returnDate) <= 0;
    }

    public void commit(int loanId) {
        if(this.state != ELoanState.PENDING) {
            throw new RuntimeException(String.format("Loan : commit : incorrect state transition  : %s -> %s\n", new Object[]{this.state, ELoanState.CURRENT}));
        } else if(loanId <= 0) {
            throw new RuntimeException(String.format("Loan : commit : id must be a positive integer  : %d\n", new Object[]{Integer.valueOf(loanId)}));
        } else {
            this.id = loanId;
            this.state = ELoanState.CURRENT;
            this.book.borrow(this);
            this.borrower.addLoan(this);
        }
    }

    public void complete() {
        if(this.state != ELoanState.CURRENT && this.state != ELoanState.OVERDUE) {
            throw new RuntimeException(String.format("Loan : complete : incorrect state transition  : %s -> %s\n", new Object[]{this.state, ELoanState.COMPLETE}));
        } else {
            this.state = ELoanState.COMPLETE;
        }
    }

    public boolean isOverDue() {
        return this.state == ELoanState.OVERDUE;
    }

    public boolean checkOverDue(Date currentDate) {
        if(this.state != ELoanState.CURRENT && this.state != ELoanState.OVERDUE) {
            throw new RuntimeException(String.format("Loan : checkOverDue : incorrect state transition  : %s -> %s\n", new Object[]{this.state, ELoanState.OVERDUE}));
        } else {
            if(currentDate.compareTo(this.dueDate) > 0) {
                this.state = ELoanState.OVERDUE;
            }

            return this.isOverDue();
        }
    }

    public IMember getBorrower() {
        return this.borrower;
    }

    public IBook getBook() {
        return this.book;
    }

    public int getID() {
        return this.id;
    }

    public ELoanState getState() {
        return this.state;
    }

    public String toString() {
        return String.format("Loan ID:  %d\nAuthor:   %s\nTitle:    %s\nBorrower: %s %s\nBorrowed: %s\nDue Date: %s", new Object[]{Integer.valueOf(this.id), this.book.getAuthor(), this.book.getTitle(), this.borrower.getFirstName(), this.borrower.getLastName(), DateFormat.getDateInstance().format(this.borrowDate), DateFormat.getDateInstance().format(this.dueDate)});
    }
}

    public IMember getBorrower() {
        return this.borrower;
    }

    public IBook getBook() {
        return this.book;
    }

    public int getID() {
        return this.id;
    }

    public ELoanState getState() {
        return this.state;
    }

    public String toString() {
        return String.format("Loan ID:  %d\nAuthor:   %s\nTitle:    %s\nBorrower: %s %s\nBorrowed: %s\nDue Date: %s", new Object[]{Integer.valueOf(this.id), this.book.getAuthor(), this.book.getTitle(), this.borrower.getFirstName(), this.borrower.getLastName(), DateFormat.getDateInstance().format(this.borrowDate), DateFormat.getDateInstance().format(this.dueDate)});
    }
}