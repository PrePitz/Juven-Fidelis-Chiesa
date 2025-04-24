package com.mycompany.libraryapp;

import java.util.*;

public class BookService {
private final Map<Long, book> bookMap = new HashMap<>();
private long nextId = 1;

public List<book> getAll() {
return new ArrayList<>(bookMap.values());
}

public book get(long id) {
return bookMap.get(id);
}

public book add(book book) {
book.setId(nextId++);
bookMap.put(book.getId(), book);
return book;
}

public book update(long id, book book) {
book.setId(id);
bookMap.put(id, book);
return book;
}

public boolean delete(long id) {
return bookMap.remove(id) != null;
}
}