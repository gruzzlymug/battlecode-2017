/* -*- Mode: Java; tab-width: 2; indent-tabs-mode: nil; c-basic-offset: ; c-file-style: "linux" -*- */
package fabian1.ringbuffer;

public class RingBuffer< T >
{
    private T   m_data[];
    private int m_size;
    
    private int m_clkw_indexes[];
    private int m_cclkw_indexes[];

    private int m_back_index;
    private int m_front_index;
    
    // Can't instantiate without a length
    private RingBuffer(){};

    public RingBuffer( int i_size )
    {
	//
	m_size = i_size;
	m_data = ( T[] )new Object[ m_size ];

	//
	m_clkw_indexes = new int[ m_size ];
	m_clkw_indexes[ m_size - 1 ] = 0;

	m_cclkw_indexes = new int[ m_size ];
        m_cclkw_indexes[ 0 ] = m_size - 1;	
	
	for( int index_i = 1; index_i < m_size; ++index_i )
	    {
		m_clkw_indexes[ index_i - 1 ] = index_i;
		m_cclkw_indexes[ index_i ]    = index_i - 1;
	    }
	
	//
	m_front_index = 0;
	m_back_index  = 0;
    }

    public void push_back( T i_obj )
    {	
	m_data[ m_back_index ] = i_obj;

	m_back_index = m_clkw_indexes[ m_back_index ];
	if( m_back_index == m_front_index )
	    { m_front_index = m_clkw_indexes[ m_front_index ]; }
	return;
    }

    public T pop_front()
    {
	T ret = m_data[ m_front_index ];
	//m_data[ m_front_index ] = null;

	if( m_front_index == m_back_index )
	    { return ret; }
	
	m_front_index = m_clkw_indexes[ m_front_index ];
	return ret;
    }

    public T pop_back()
    {
	if( m_front_index == m_back_index )
            { return null; }

	m_back_index = m_cclkw_indexes[ m_back_index ];
	T ret = m_data[ m_back_index ];
	//m_data[ m_back_index ] = null;

	return ret;
    }

    public void print()
    {
	System.out.print( "\n" );

	System.out.print( "m_data = [ " );
	System.out.print( m_data[0] );
	System.out.print( ", " );
	for( int i = 1; i < ( m_size - 1 ); ++i )
	    {
		System.out.print( m_data[ i ] );
		System.out.print( ", " );
	    }
	System.out.print( m_data[ m_size - 1 ] );
        System.out.print( " ]" );

	System.out.print( "\n" );
	
	System.out.print( "m_clkw_indexes = [ " );
        System.out.print( m_clkw_indexes[0] );
        System.out.print( ", " );
        for( int i = 1; i < ( m_size - 1 ); ++i )
            {
                System.out.print( m_clkw_indexes[ i ] );
                System.out.print( ", " );
            }
        System.out.print( m_clkw_indexes[ m_size - 1 ] );
        System.out.print( " ]" );

	System.out.print( "\n" );

	System.out.print( "m_cclkw_indexes = [ " );
        System.out.print( m_cclkw_indexes[0] );
        System.out.print( ", " );
        for( int i = 1; i < ( m_size - 1 ); ++i )
            {
                System.out.print( m_cclkw_indexes[ i ] );
                System.out.print( ", " );
            }
        System.out.print( m_cclkw_indexes[ m_size - 1 ] );
        System.out.print( " ]" );

	System.out.print( "\n" );

	System.out.print( "m_back_index = " );
	System.out.print( m_back_index );
	System.out.print( "\n" );
	System.out.print( "m_front_index = " );
        System.out.print( m_front_index );
	System.out.print( "\n" );
	
	System.out.print( "===========================================" );

	System.out.print( "\n" );
    }
}
