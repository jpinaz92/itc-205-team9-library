
package library.interfaces;

public interface IBorrowUIListener {
	void cancelled();

	void scansCompleted();

	void loansConfirmed();

	void loansRejected();
}
