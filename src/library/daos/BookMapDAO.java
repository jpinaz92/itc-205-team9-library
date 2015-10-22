package library.daos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookMapDAO implements IBookDAO {
    private int nextId;
    private Map<Integer, IBook> bookMap;
    private IBookHelper helper;

    public BookMapDAO(IBookHelper helper) {
        if(helper == null) {
            throw new IllegalArgumentException(String.format("BookDAO : constructor : helper cannot be null.", new Object[0]));
        } else {
            this.nextId = 1;
            this.helper = helper;
            this.bookMap = new HashMap();
        }
    }

    public BookMapDAO(IBookHelper helper, Map<Integer, IBook> bookMap) {
        this(helper);
        if(helper == null) {
            throw new IllegalArgumentException(String.format("BookDAO : constructor : bookMap cannot be null.", new Object[0]));
        } else {
            this.bookMap = bookMap;
        }
    }

    public IBook addBook(String author, String title, String callNo) {
        int id = this.getNextId();
        IBook book = this.helper.makeBook(author, title, callNo, id);
        this.bookMap.put(Integer.valueOf(id), book);
        return book;
    }

    public IBook getBookByID(int id) {
        return this.bookMap.containsKey(Integer.valueOf(id))?(IBook)this.bookMap.get(Integer.valueOf(id)):null;
    }

    public List<IBook> listBooks() {
        ArrayList list = new ArrayList(this.bookMap.values());
        return Collections.unmodifiableList(list);
    }

    public List<IBook> findBooksByAuthor(String author) {
        if(author != null && !author.isEmpty()) {
            ArrayList list = new ArrayList();
            Iterator var4 = this.bookMap.values().iterator();

            while(var4.hasNext()) {
                IBook b = (IBook)var4.next();
                if(author.equals(b.getAuthor())) {
                    list.add(b);
                }
            }

            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(String.format("BookDAO : findBooksByAuthor : author cannot be null or blank", new Object[0]));
        }
    }

    public List<IBook> findBooksByTitle(String title) {
        if(title != null && !title.isEmpty()) {
            ArrayList list = new ArrayList();
            Iterator var4 = this.bookMap.values().iterator();

            while(var4.hasNext()) {
                IBook b = (IBook)var4.next();
                if(title.equals(b.getTitle())) {
                    list.add(b);
                }
            }

            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(String.format("BookDAO : findBooksByAuthor : author cannot be null or blank", new Object[0]));
        }
    }

    public List<IBook> findBooksByAuthorTitle(String author, String title) {
        if(title != null && !title.isEmpty() && author != null && !author.isEmpty()) {
            ArrayList list = new ArrayList();
            Iterator var5 = this.bookMap.values().iterator();

            while(var5.hasNext()) {
                IBook b = (IBook)var5.next();
                if(author.equals(b.getAuthor()) && title.equals(b.getTitle())) {
                    list.add(b);
                }
            }

            return Collections.unmodifiableList(list);
        } else {
            throw new IllegalArgumentException(String.format("BookDAO : findBooksByAuthor : author and title cannot be null or blank", new Object[0]));
        }
    }

    private int getNextId() {
        return this.nextId++;
    }
}
