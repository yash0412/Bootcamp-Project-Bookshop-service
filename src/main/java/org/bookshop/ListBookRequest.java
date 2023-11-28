package org.bookshop;

import java.util.List;

public record ListBookRequest(List<String> bookIdList)
{
}
