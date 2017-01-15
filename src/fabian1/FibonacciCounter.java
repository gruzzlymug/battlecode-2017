/* -*- Mode: Java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: ; c-file-style: "linux" -*- */
package fabian1.fibonaccicounter;

public class FibonacciCounter< T >
{
    private int m_prev;
    private int m_value;
    private int m_next;
    
    private FibonacciCounter()
    {
	m_prev  = 0;
	m_value = 0;
	m_next  = 1;

	return;
    }

    public void incr()
    {
	m_prev  = m_value;
	m_value = m_next;
	m_next  = m_prev + m_value;

	return;
    }

    public void decr()
    {
	if( m_value == 0 )
	    { return; }

	m_value = m_prev;
	m_next  = m_value;
	m_prev  = m_next - m_value;

	return;
    }

    public int value()
    { return m_value; }
    
    public int prev()
    { return m_prev; }
    
    public int next()
    { return m_next; }
}
