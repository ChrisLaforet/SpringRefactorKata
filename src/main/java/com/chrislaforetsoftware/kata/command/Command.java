package com.chrislaforetsoftware.kata.command;

public interface Command<T, U> {

	U handle(T command);
}
