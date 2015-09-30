package library.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import library.interfaces.entities.EMemberState;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public class Member implements IMember {
    private final String firstName;
    private final String lastName;
    private final String contactPhone;
    private final String emailAddress;
    private final int id;
    private EMemberState state;
    private List<ILoan> loanList;
    private float totalFines;

    public Member(String firstName, String lastName, String contactPhone, String email, int memberID) {
        if(!this.sane(firstName, lastName, contactPhone, email, memberID)) {
            throw new IllegalArgumentException("Member: constructor : bad parameters");
        } else {
            this.firstName = firstName;
            this.lastName = lastName;
            this.contactPhone = contactPhone;
            this.emailAddress = email;
            this.id = memberID;
            this.loanList = new ArrayList();
            this.totalFines = 0.0F;
            this.state = EMemberState.BORROWING_ALLOWED;
        }
    }

    private boolean sane(String firstName, String lastName, String contactPhone, String emailAddress, int memberID) {
        return firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty() && contactPhone != null && !contactPhone.isEmpty() && emailAddress != null && !emailAddress.isEmpty() && memberID > 0;
    }

    public boolean hasOverDueLoans() {
        Iterator var2 = this.loanList.iterator();

        while(var2.hasNext()) {
            ILoan loan = (ILoan)var2.next();
            if(loan.isOverDue()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasReachedLoanLimit() {
        boolean b = this.loanList.size() >= 5;
        return b;
    }

    public boolean hasFinesPayable() {
        boolean b = this.totalFines > 0.0F;
        return b;
    }

    public boolean hasReachedFineLimit() {
        boolean b = this.totalFines >= 10.0F;
        return b;
    }

    public float getFineAmount() {
        return this.totalFines;
    }

    public void addFine(float fine) {
        if(fine < 0.0F) {
            throw new RuntimeException(String.format("Member: addFine : fine cannot be negative", new Object[0]));
        } else {
            this.totalFines += fine;
            this.updateState();
        }
    }

    public void payFine(float payment) {
        if(payment >= 0.0F && payment <= this.totalFines) {
            this.totalFines -= payment;
            this.updateState();
        } else {
            throw new RuntimeException(String.format("Member: addFine : payment cannot be negative or greater than totalFines", new Object[0]));
        }
    }

    public void addLoan(ILoan loan) {
        if(!this.borrowingAllowed().booleanValue()) {
            throw new RuntimeException(String.format("Member: addLoan : illegal operation in state: %s", new Object[]{this.state}));
        } else {
            this.loanList.add(loan);
            this.updateState();
        }
    }

    public List<ILoan> getLoans() {
        return Collections.unmodifiableList(this.loanList);
    }

    public void removeLoan(ILoan loan) {
        if(loan != null && this.loanList.contains(loan)) {
            this.loanList.remove(loan);
            this.updateState();
        } else {
            throw new RuntimeException(String.format("Member: removeLoan : loan is null or not found in loanList", new Object[0]));
        }
    }

    public EMemberState getState() {
        return this.state;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public int getID() {
        return this.id;
    }

    public String toString() {
        return String.format("Id: %d\nName: %s %s\nContact Phone: %s\nEmail: %s\nOutstanding Charges: %0.2f", new Object[]{Integer.valueOf(this.id), this.firstName, this.lastName, this.contactPhone, this.emailAddress, Float.valueOf(this.totalFines)});
    }

    private Boolean borrowingAllowed() {
        boolean b = !this.hasOverDueLoans() && !this.hasReachedFineLimit() && !this.hasReachedLoanLimit();
        return Boolean.valueOf(b);
    }

    private void updateState() {
        if(this.borrowingAllowed().booleanValue()) {
            this.state = EMemberState.BORROWING_ALLOWED;
        } else {
            this.state = EMemberState.BORROWING_DISALLOWED;
        }

    }
}