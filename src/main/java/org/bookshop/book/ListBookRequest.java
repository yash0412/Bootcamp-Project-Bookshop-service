package org.bookshop.book;

import java.util.List;

public record ListBookRequest(List<String> bookIdList)
{
}
