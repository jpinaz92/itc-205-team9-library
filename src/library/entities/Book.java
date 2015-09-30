package library.entities;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;

public class Book implements IBook {
    private String author;
    private String title;
    private String callNumber;
    private int id;
    private ILoan loan;
    private EBookState state;

    public Book(String author, String title, String callNumber, int bookID) {
        if(!this.sane(author, title, callNumber, bookID)) {
            throw new IllegalArgumentException("Member: constructor : bad parameters");
        } else {
            this.author = author;
            this.title = title;
            this.callNumber = callNumber;
            this.id = bookID;
            this.state = EBookState.AVAILABLE;
            this.loan = null;
        }
    }

    private boolean sane(String author, String title, String callNumber, int bookID) {
        return author != null && !author.isEmpty() && title != null && !title.isEmpty() && callNumber != null && !callNumber.isEmpty() && bookID > 0;
    }

    public void borrow(ILoan loan) {
        if(loan == null) {
            throw new IllegalArgumentException(String.format("Book: borrow : Bad parameter: loan cannot be null", new Object[0]));
        } else if(this.state != EBookState.AVAILABLE) {
            throw new RuntimeException(String.format("Illegal operation in state : %s", new Object[]{this.state}));
        } else {
            this.loan = loan;
            this.state = EBookState.ON_LOAN;
        }
    }

    public ILoan getLoan() {
        return this.loan;
    }

    public void returnBook(boolean damaged) {
        if(this.state != EBookState.ON_LOAN && this.state != EBookState.LOST) {
            throw new RuntimeException(String.format("Illegal operation in state : %s", new Object[]{this.state}));
        } else {
            this.loan = null;
            if(damaged) {
                this.state = EBookState.DAMAGED;
            } else {
                this.state = EBookState.AVAILABLE;
            }

        }
    }

    public void lose() {
        if(this.state != EBookState.ON_LOAN) {
            throw new RuntimeException(String.format("Illegal operation in state : %s", new Object[]{this.state}));
        } else {
            this.state = EBookState.LOST;
        }
    }

    public void repair() {
        if(this.state != EBookState.DAMAGED) {
            throw new RuntimeException(String.format("Illegal operation in state : %s", new Object[]{this.state}));
        } else {
            this.state = EBookState.AVAILABLE;
        }
    }

    public void dispose() {
        if(this.state != EBookState.AVAILABLE && this.state != EBookState.DAMAGED && this.state != EBookState.LOST) {
            throw new RuntimeException(String.format("Illegal operation in state : %s", new Object[]{this.state}));
        } else {
            this.state = EBookState.DISPOSED;
        }
    }

    public EBookState getState() {
        return this.state;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCallNumber() {
        return this.callNumber;
    }

    public int getID() {
        return this.id;
    }

    public String toString() {
        return String.format("Id: %d\nAuthor: %s\nTitle: %s\nCall Number %s", new Object[]{Integer.valueOf(this.id), this.author, this.title, this.callNumber});
    }
}

