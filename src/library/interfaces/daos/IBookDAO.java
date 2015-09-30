
package library.interfaces.daos;

import java.util.List;
import library.interfaces.entities.IBook;

public interface IBookDAO {
	IBook addBook(String var1, String var2, String var3);

	IBook getBookByID(int var1);

	List<IBook> listBooks();

	List<IBook> findBooksByAuthor(String var1);

	List<IBook> findBooksByTitle(String var1);

	List<IBook> findBooksByAuthorTitle(String var1, String var2);
}
