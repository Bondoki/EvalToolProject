package EvalToolProject.tools;
/*
 * @(#)Iterable.java	1.3 03/12/19
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

//package java.lang;

//import java.util.Iterator;

/** Implementing this interface allows an object to be the target of
 *  the "foreach" statement.
 */
public interface IntIterable {

    /**
     * Returns an iterator over a set of elements of type T.
     * 
     * @return an Iterator.
     */
    IntIterator iterator();
}
